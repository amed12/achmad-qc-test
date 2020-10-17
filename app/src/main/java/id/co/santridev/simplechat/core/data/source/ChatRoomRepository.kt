/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:22 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:22 PM
 *
 */

package id.co.santridev.simplechat.core.data.source

import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.repository.IChatRoomRepository
import id.co.santridev.simplechat.core.utils.Action

class ChatRoomRepository : IChatRoomRepository {
    override fun getChatRoom(onSuccess: Action<List<QiscusChatRoom>>, onError: Action<Throwable>) {
        TODO("Not yet implemented")
    }

    override fun createChatRoom(
        user: User,
        onSuccess: Action<QiscusChatRoom>,
        onError: Action<Throwable>
    ) {
        TODO("Not yet implemented")
    }
}