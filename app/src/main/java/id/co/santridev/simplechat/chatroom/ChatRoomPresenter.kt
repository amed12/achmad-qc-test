/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 9:51 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 9:51 AM
 *
 */

package id.co.santridev.simplechat.chatroom

import androidx.core.util.Pair
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.local.QiscusCacheManager
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.qiscus.sdk.chat.core.data.model.QiscusRoomMember
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi
import com.qiscus.sdk.chat.core.data.remote.QiscusResendCommentHelper
import com.qiscus.sdk.chat.core.presenter.QiscusChatRoomEventHandler
import com.qiscus.sdk.chat.core.util.QiscusAndroidUtil
import id.co.santridev.simplechat.core.utils.extension.QiscusPresenter
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import retrofit2.HttpException
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func2
import rx.schedulers.Schedulers
import java.util.*

class ChatRoomPresenter(view: View, private var room: QiscusChatRoom) :
    QiscusPresenter<ChatRoomPresenter.View>(
        view
    ), QiscusChatRoomEventHandler.StateListener {
    val qiscusAccount: QiscusAccount by lazy { QiscusCore.getQiscusAccount() }
    val commentComparator by lazy {
        Func2 { lhs: QiscusComment, rhs: QiscusComment ->
            rhs.time.compareTo(lhs.time)
        }
    }

    private var pendingTask: MutableMap<QiscusComment, Subscription> = mutableMapOf()

    val roomEventHandler: QiscusChatRoomEventHandler by lazy {
        QiscusChatRoomEventHandler(
            room,
            this
        )
    }

    init {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    private fun commentSuccess(qiscusComment: QiscusComment) {
        pendingTask.remove(qiscusComment)
        qiscusComment.state = QiscusComment.STATE_ON_QISCUS
        val savedQiscusComment = QiscusCore.getDataStore().getComment(qiscusComment.uniqueId)
        if (savedQiscusComment != null && savedQiscusComment.state > qiscusComment.state) {
            qiscusComment.state = savedQiscusComment.state
        }
        QiscusCore.getDataStore().addOrUpdate(qiscusComment)
    }

    private fun mustFailed(throwable: Throwable, qiscusComment: QiscusComment): Boolean {
        //Error response from server
        //Means something wrong with server, e.g user is not member of these room anymore
        return throwable is HttpException && throwable.code() >= 400 ||  //if throwable from JSONException, e.g response from server not json as expected
                throwable is JSONException ||  // if attachment type
                qiscusComment.isAttachment
    }

    private fun commentFail(throwable: Throwable, qiscusComment: QiscusComment) {
        pendingTask.remove(qiscusComment)
        if (!QiscusCore.getDataStore().isContains(qiscusComment)) { //Have been deleted
            return
        }
        var state = QiscusComment.STATE_PENDING
        if (mustFailed(throwable, qiscusComment)) {
            qiscusComment.isDownloading = false
            state = QiscusComment.STATE_FAILED
        }

        //Kalo ternyata comment nya udah sukses dikirim sebelumnya, maka ga usah di update
        val savedQiscusComment = QiscusCore.getDataStore().getComment(qiscusComment.uniqueId)
        if (savedQiscusComment != null && savedQiscusComment.state > QiscusComment.STATE_SENDING) {
            return
        }

        //Simpen statenya
        qiscusComment.state = state
        QiscusCore.getDataStore().addOrUpdate(qiscusComment)
    }

    fun cancelPendingComment(qiscusComment: QiscusComment?) {
        if (pendingTask.containsKey(qiscusComment)) {
            val subscription = pendingTask[qiscusComment]
            if (!subscription!!.isUnsubscribed) {
                subscription.unsubscribe()
            }
            pendingTask.remove(qiscusComment)
        }
    }

    private fun sendComment(qiscusComment: QiscusComment) {
        view.onSendingComment(qiscusComment)
        val subscription = QiscusApi.getInstance().sendMessage(qiscusComment)
            .doOnSubscribe { QiscusCore.getDataStore().addOrUpdate(qiscusComment) }
            .doOnNext { quincesComment: QiscusComment ->
                commentSuccess(
                    quincesComment
                )
            }
            .doOnError { throwable: Throwable ->
                commentFail(
                    throwable,
                    qiscusComment
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe({ commentSend ->
                if (commentSend.roomId.toInt() == room.id.toInt()) {
                    view.onSuccessSendComment(commentSend)
                }
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                if (qiscusComment.roomId == room.id) {
                    view.onFailedSendComment(qiscusComment)
                }
            })
        pendingTask[qiscusComment] = subscription
    }

    fun sendComment(content: String?) {
        val qiscusComment = QiscusComment.generateMessage(room.id, content)
        sendComment(qiscusComment)
    }

    fun resendComment(qiscusComment: QiscusComment) {
        qiscusComment.state = QiscusComment.STATE_SENDING
        qiscusComment.time = Date()
        sendComment(qiscusComment)
    }

    fun deleteComment(qiscusComment: QiscusComment) {
        cancelPendingComment(qiscusComment)
        QiscusResendCommentHelper.cancelPendingComment(qiscusComment)

        // this code for delete from local
        QiscusAndroidUtil.runOnBackgroundThread {
            QiscusCore.getDataStore().delete(qiscusComment)
        }
        if (view != null) {
            view.dismissLoading()
            view.onCommentDeleted(qiscusComment)
        }
        Observable.from(arrayOf(qiscusComment))
            .map { obj: QiscusComment -> obj.uniqueId }
            .toList()
            .flatMap { uniqueIds: List<String>? ->
                QiscusApi.getInstance().deleteMessages(uniqueIds)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe({ _ ->
                if (view != null) {
                    view.dismissLoading()
                    view.onCommentDeleted(qiscusComment)
                }
            }, {
                if (view != null) {
                    view.dismissLoading()
                }
            })
    }

    private fun getInitRoomData(): Observable<Pair<QiscusChatRoom?, List<QiscusComment>>>? {
        return QiscusApi.getInstance().getChatRoomWithMessages(room.id)
            .doOnError { throwable: Throwable ->
                throwable.printStackTrace()
                QiscusAndroidUtil.runOnUIThread {
                    if (view != null) {
                        view.onLoadCommentsError(throwable)
                    }
                }
            }
            .doOnNext { roomData: Pair<QiscusChatRoom, List<QiscusComment>> ->
                roomEventHandler.setChatRoom(roomData.first)
                Collections.sort(
                    roomData.second
                ) { lhs: QiscusComment, rhs: QiscusComment ->
                    rhs.time.compareTo(lhs.time)
                }
                QiscusCore.getDataStore().addOrUpdate(roomData.first)
            }
            .doOnNext { roomData: Pair<QiscusChatRoom?, List<QiscusComment?>> ->
                for (qiscusComment in roomData.second!!) {
                    QiscusCore.getDataStore().addOrUpdate(qiscusComment)
                }
            }
            .subscribeOn(Schedulers.io())
            .onErrorReturn { throwable: Throwable? -> null }
    }

    private fun getCommentsFromNetwork(lastCommentId: Long): Observable<MutableList<QiscusComment?>> {
        return QiscusApi.getInstance().getPreviousMessagesById(room.id, 20, lastCommentId)
            .doOnNext { qiscusComment: QiscusComment ->
                QiscusCore.getDataStore().addOrUpdate(qiscusComment)
                qiscusComment.roomId = room.id
            }
            .toSortedList(commentComparator)
            .subscribeOn(Schedulers.io())
    }

    private fun getLocalComments(
        count: Int
    ): Observable<List<QiscusComment>> {
        return QiscusCore.getDataStore().getObservableComments(room.id, 2 * count)
            .flatMap { iterable ->
                Observable.from(
                    iterable
                )
            }
            .toSortedList(commentComparator)
            .map { comments ->
                if (comments.size > count) {
                    return@map comments.subList(0, count)
                }
                comments
            }
            .subscribeOn(Schedulers.io())
    }

    fun loadLocalComments(count: Int): List<QiscusComment?>? {
        return QiscusCore.getDataStore().getComments(room.id, count)
    }

    fun loadComments(count: Int) {
        Observable.merge(getInitRoomData(), getLocalComments(count)
            .map { comments: List<QiscusComment> ->
                Pair.create(
                    room,
                    comments
                )
            })
            .filter { qiscusChatRoomListPair -> qiscusChatRoomListPair != null }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe({ roomData ->
                if (view != null) {
                    room = roomData.first!!
                    view.initRoomData(roomData.first!!, roomData.second!!)
                    view.dismissLoading()
                }
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                if (view != null) {
                    view.onLoadCommentsError(throwable)
                    view.dismissLoading()
                }
            })
    }

    private fun cleanFailedComments(qiscusComments: List<QiscusComment>): List<QiscusComment> {
        val comments: MutableList<QiscusComment> = ArrayList()
        for (qiscusComment in qiscusComments) {
            if (qiscusComment.id != -1L) {
                comments.add(qiscusComment)
            }
        }
        return comments
    }

    private fun isValidOlderComments(
        qiscusComments: List<QiscusComment>,
        lastQiscusComment: QiscusComment
    ): Boolean {
        var quintusComments = qiscusComments
        if (quintusComments.isEmpty()) return false
        quintusComments = cleanFailedComments(quintusComments)
        var containsLastValidComment = quintusComments.isEmpty() || lastQiscusComment.id == -1L
        val size = quintusComments.size
        if (size == 1) {
            return quintusComments[0].commentBeforeId == 0L && lastQiscusComment.commentBeforeId == quintusComments[0].id
        }
        for (i in 0 until size - 1) {
            if (!containsLastValidComment && quintusComments[i].id == lastQiscusComment.commentBeforeId) {
                containsLastValidComment = true
            }
            if (quintusComments[i].commentBeforeId != quintusComments[i + 1].id) {
                return false
            }
        }
        return containsLastValidComment
    }

    private fun isValidChainingComments(qiscusComments: List<QiscusComment>): Boolean {
        var quintusComments = qiscusComments
        quintusComments = cleanFailedComments(quintusComments)
        val size = quintusComments.size
        for (i in 0 until size - 1) {
            if (quintusComments[i].commentBeforeId != quintusComments[i + 1].id) {
                return false
            }
        }
        return true
    }

    fun loadOlderCommentThan(qiscusComment: QiscusComment) {
        view.showLoadMoreLoading()
        QiscusCore.getDataStore().getObservableOlderCommentsThan(qiscusComment, room.id, 40)
            .flatMap { iterable ->
                Observable.from(
                    iterable
                )
            }
            .filter { qiscusComment1 -> qiscusComment.id == -1L || qiscusComment1.id < qiscusComment.id }
            .toSortedList(commentComparator)
            .map { comments ->
                if (comments.size >= 20) {
                    return@map comments.subList(0, 20)
                }
                comments
            }
            .doOnNext { comments ->
                updateRepliedSender(
                    comments
                )
            }
            .flatMap { comments ->
                if (isValidOlderComments(comments, qiscusComment)) Observable.from(
                    comments
                )
                    .toSortedList(commentComparator) else getCommentsFromNetwork(qiscusComment.id).map<List<QiscusComment?>> { comments1: MutableList<QiscusComment?> ->
                    for (localComment in comments) {
                        if (localComment != null) {
                            if (localComment.state <= QiscusComment.STATE_SENDING) {
                                comments1.add(localComment)
                            }
                        }
                    }
                    comments1
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe({ comments ->
                if (view != null) {
                    view.onLoadMore(comments)
                    view.dismissLoading()
                }
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                if (view != null) {
                    view.onLoadCommentsError(throwable)
                    view.dismissLoading()
                }
            })
    }

    private fun updateRepliedSender(comments: List<QiscusComment>) {
        for (comment in comments) {
            if (comment.type == QiscusComment.Type.REPLY) {
                val repliedComment = comment.replyTo
                if (repliedComment != null) {
                    for (qiscusRoomMember in room.member) {
                        if (repliedComment.senderEmail == qiscusRoomMember.email) {
                            repliedComment.sender = qiscusRoomMember.username
                            comment.replyTo = repliedComment
                            break
                        }
                    }
                }
            }
        }
    }

    fun loadCommentsAfter(comment: QiscusComment) {
        QiscusApi.getInstance().getNextMessagesById(room.id, 20, comment.id)
            .doOnNext { qiscusComment: QiscusComment -> qiscusComment.roomId = room.id }
            .toSortedList(commentComparator)
            .doOnNext { list ->
                list.reverse()
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .subscribe({ comments ->
                if (view != null) {
                    view.onLoadMore(comments)
                }
            }, { obj: Throwable -> obj.printStackTrace() })
    }

    private fun onGotNewComment(qiscusComment: QiscusComment) {
        if (qiscusComment.senderEmail.equals(qiscusAccount.email, ignoreCase = true)) {
            QiscusAndroidUtil.runOnBackgroundThread { commentSuccess(qiscusComment) }
        } else {
            roomEventHandler.onGotComment(qiscusComment)
        }
        if (qiscusComment.roomId == room.id) {
            QiscusAndroidUtil.runOnBackgroundThread {
                if (!qiscusComment.senderEmail.equals(qiscusAccount.email, ignoreCase = true)
                    && QiscusCacheManager.getInstance().lastChatActivity.first!!
                ) {
                    QiscusPusherApi.getInstance().markAsRead(room.id, qiscusComment.id)
                }
            }
            view.onNewComment(qiscusComment)
        }
    }

    private fun clearUnreadCount() {
        room.unreadCount = 0
        room.lastComment = null
        QiscusCore.getDataStore().addOrUpdate(room)
    }

    override fun detachView() {
        roomEventHandler.detach()
        clearUnreadCount()
        room = QiscusChatRoom()
        EventBus.getDefault().unregister(this)
    }

    override fun onChatRoomNameChanged(name: String?) {
        room.name = name
        QiscusAndroidUtil.runOnUIThread {
            if (view != null) {
                view.onRoomChanged(room)
            }
        }
    }

    override fun onChatRoomMemberAdded(member: QiscusRoomMember?) {
        if (!room.member.contains(member)) {
            room.member.add(member)
            QiscusAndroidUtil.runOnUIThread {
                if (view != null) {
                    view.onRoomChanged(room)
                }
            }
        }
    }

    override fun onChatRoomMemberRemoved(member: QiscusRoomMember?) {
        val x = room.member.indexOf(member)
        if (x >= 0) {
            room.member.removeAt(x)
            QiscusAndroidUtil.runOnUIThread {
                if (view != null) {
                    view.onRoomChanged(room)
                }
            }
        }
    }

    override fun onUserTypng(email: String?, typing: Boolean) {
        QiscusAndroidUtil.runOnUIThread {
            if (view != null) {
                view.onUserTyping(email!!, typing)
            }
        }
    }

    override fun onChangeLastDelivered(lastDeliveredCommentId: Long) {
        QiscusAndroidUtil.runOnUIThread {
            if (view != null) {
                view.updateLastDeliveredComment(lastDeliveredCommentId)
            }
        }
    }

    override fun onChangeLastRead(lastReadCommentId: Long) {
        QiscusAndroidUtil.runOnUIThread {
            if (view != null) {
                view.updateLastReadComment(lastReadCommentId)
            }
        }
    }

    interface View : QiscusPresenter.View {

        override fun showError(errorMessage: String?)

        override fun showLoading()

        override fun dismissLoading()

        fun showLoadMoreLoading()

        fun initRoomData(qiscusChatRoom: QiscusChatRoom, comment: List<QiscusComment>)

        fun onRoomChanged(qiscusChatRoom: QiscusChatRoom)

        fun onLoadMore(qiscusComment: List<QiscusComment>)

        fun onSendingComment(qiscusComment: QiscusComment)

        fun onSuccessSendComment(qiscusComment: QiscusComment)

        fun onFailedSendComment(qiscusComment: QiscusComment)

        fun onNewComment(qiscusComment: QiscusComment)

        fun onCommentDeleted(qiscusComment: QiscusComment)

        fun refreshComment(qiscusComment: QiscusComment)

        fun notifyDataChanged()

        fun updateLastDeliveredComment(lastDeliveredCommentId: Long)

        fun updateLastReadComment(lastReadCommentId: Long)

        fun onUserTyping(user: String, typing: Boolean)

        fun showCommentsAndScrollToTop(qiscusComments: List<QiscusComment>)

        fun onRealtimeStatusChanged(connected: Boolean)

        fun onLoadCommentsError(throwable: Throwable)

        fun clearCommentsBefore(timestamp: Long)
    }
}