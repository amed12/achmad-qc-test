/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:28 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:28 PM
 *
 */

package id.co.santridev.simplechat.core.domain.usecase


import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.repository.IChatRoomRepository
import id.co.santridev.simplechat.core.utils.Action

class ChatRoomInteractor(private val chatRoomRepository: IChatRoomRepository) : IChatRoomUseCase {
    override fun getChatRoom(onSuccess: Action<List<QiscusChatRoom>>, onError: Action<Throwable>) {
        chatRoomRepository.getChatRoom(onSuccess, onError)
    }

    override fun createChatRoom(
        user: User,
        onSuccess: Action<QiscusChatRoom>,
        onError: Action<Throwable>
    ) {
        chatRoomRepository.createChatRoom(user, onSuccess, onError)
    }
}