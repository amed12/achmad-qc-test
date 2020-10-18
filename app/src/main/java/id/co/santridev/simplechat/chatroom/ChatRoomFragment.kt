/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 12:31 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 12:27 PM
 *
 */

package id.co.santridev.simplechat.chatroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiscus.sdk.chat.core.data.local.QiscusCacheManager
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.utils.adapter.CommentsAdapter
import id.co.santridev.simplechat.core.utils.extension.QiscusChatScrollListener
import id.co.santridev.simplechat.core.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_chat_room.*


private const val KEY_CHAT_ROOM = "param1"

class ChatRoomFragment : Fragment(), ChatRoomPresenter.View, QiscusChatScrollListener.Listener {
    private var chatRoom = QiscusChatRoom()

    private lateinit var chatPresenter: ChatRoomPresenter
    private val commentsAdapter by lazy { CommentsAdapter(activity) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatRoom = it.getParcelable(KEY_CHAT_ROOM) ?: QiscusChatRoom()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(qiscusChatRoom: QiscusChatRoom) =
            ChatRoomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_CHAT_ROOM, qiscusChatRoom)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (chatRoom.name.isEmpty()) {
            throw RuntimeException("Please provide chat room")
        }
        chatRoom.let {
            chatPresenter = ChatRoomPresenter(this, it)
        }
        button_send?.setOnClickListener {
            field_message?.apply {
                if (this.text.toString().isNotEmpty()) {
                    chatPresenter.sendComment(this.text.toString())
                    this.text.clear()
                }
            }
        }
        val linearManager = LinearLayoutManager(activity)
        linearManager.reverseLayout = true
        recyclerview.apply {
            layoutManager = linearManager
            setHasFixedSize(true)
            addOnScrollListener(QiscusChatScrollListener(linearManager, this@ChatRoomFragment))
            adapter = commentsAdapter
        }
        chatPresenter.loadComments(20)
    }

    private fun loadMoreComments() {
        if (progress_bar.visibility == View.GONE && commentsAdapter.itemCount > 0) {
            val comment = commentsAdapter.data[commentsAdapter.itemCount - 1]
            if (comment.id == -1L || comment.commentBeforeId > 0) {
                chatPresenter.loadOlderCommentThan(comment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        chatRoom.id.let { QiscusCacheManager.getInstance().setLastChatActivity(true, it) }
        notifyLatestRead()
    }

    override fun onPause() {
        super.onPause()
        chatRoom.id.let { QiscusCacheManager.getInstance().setLastChatActivity(false, it) }
    }

    private fun notifyLatestRead() {
        val comment = commentsAdapter.latestSentComment
        if (comment != null) {
            QiscusPusherApi.getInstance()
                .markAsRead(chatRoom.id, comment.id)
        }
    }

    override fun showError(errorMessage: String?) {
        context?.showToast(errorMessage.toString())
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun showLoadMoreLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun initRoomData(qiscusChatRoom: QiscusChatRoom, comment: List<QiscusComment>) {
        chatRoom = qiscusChatRoom
        commentsAdapter.addOrUpdate(comment)
        if (comment.isEmpty()) {
            empty_chat.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
        } else {
            empty_chat.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
        }
        notifyLatestRead()
    }

    override fun onRoomChanged(qiscusChatRoom: QiscusChatRoom) {
        chatRoom = qiscusChatRoom
    }

    override fun onLoadMore(qiscusComment: List<QiscusComment>) {
        commentsAdapter.addOrUpdate(qiscusComment)
    }

    override fun onSendingComment(qiscusComment: QiscusComment) {
        commentsAdapter.addOrUpdate(qiscusComment)
        recyclerview.smoothScrollToPosition(0)
        if (empty_chat.isShown) {
            empty_chat.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
        }
    }

    override fun onSuccessSendComment(qiscusComment: QiscusComment) {
        commentsAdapter.addOrUpdate(qiscusComment)
    }

    override fun onFailedSendComment(qiscusComment: QiscusComment) {
        commentsAdapter.addOrUpdate(qiscusComment)
    }

    override fun onNewComment(qiscusComment: QiscusComment) {
        commentsAdapter.addOrUpdate(qiscusComment)
        if ((recyclerview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() <= 2) {
            recyclerview.smoothScrollToPosition(0)
        }

        if (empty_chat.isShown) {
            empty_chat.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
        }
    }

    override fun onCommentDeleted(qiscusComment: QiscusComment) {
        commentsAdapter.remove(qiscusComment)
    }

    override fun updateLastDeliveredComment(lastDeliveredCommentId: Long) {
        commentsAdapter.updateLastDeliveredComment(lastDeliveredCommentId)
    }

    override fun updateLastReadComment(lastReadCommentId: Long) {
        commentsAdapter.updateLastReadComment(lastReadCommentId)
    }

    override fun onUserTyping(user: String, typing: Boolean) {}

    override fun onLoadCommentsError(throwable: Throwable) {
        throwable.printStackTrace()
        Log.e("ChatRoomFragment", throwable.message.toString())
    }

    override fun onTopOffListMessage() {
        loadMoreComments()
    }

    override fun onMiddleOffListMessage() {}

    override fun onBottomOffListMessage() {}

    override fun onDestroyView() {
        super.onDestroyView()
        chatPresenter.detachView()
    }
}