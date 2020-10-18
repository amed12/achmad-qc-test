/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 5:24 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 5:24 AM
 *
 */

package id.co.santridev.simplechat.core.data.source

import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.repository.IChatRoomRepository
import id.co.santridev.simplechat.core.utils.Action
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChatRoomRepository : IChatRoomRepository {
    companion object {
        @Volatile
        private var instance: ChatRoomRepository? = null

        fun getInstance(): ChatRoomRepository =
            instance ?: synchronized(this) {
                instance ?: ChatRoomRepository()
            }
    }

    override fun getChatRoom(onSuccess: Action<List<QiscusChatRoom>>, onError: Action<Throwable>) {
        Observable.from(QiscusCore.getDataStore().getChatRooms(100))
            .filter { chatRoom -> chatRoom.lastComment != null }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.call(it) }) { onError.call(it) }

        QiscusApi.getInstance()
            .getAllChatRooms(true, false, true, 1, 100)
            .flatMap { Observable.from(it) }
            .doOnNext { qiscusChatRoom -> QiscusCore.getDataStore().addOrUpdate(qiscusChatRoom) }
            .filter { chatRoom -> chatRoom.lastComment.id.toInt() != 0 }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.call(it) }) { onError.call(it) }
    }

    override fun createChatRoom(
        user: User,
        onSuccess: Action<QiscusChatRoom>,
        onError: Action<Throwable>
    ) {
        val savedChatRoom = QiscusCore.getDataStore().getChatRoom(user.id)
        if (savedChatRoom != null) {
            onSuccess.call(savedChatRoom)
            return
        }
        QiscusApi.getInstance()
            .chatUser(user.id, null)
            .doOnNext { chatRoom -> QiscusCore.getDataStore().addOrUpdate(chatRoom) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.call(it) }) { onError.call(it) }
    }
}