/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 6:56 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 6:55 AM
 *
 */

package id.co.santridev.simplechat.chatroom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import id.co.santridev.simplechat.R

class PersonalChatRoomActivity : AppCompatActivity() {

    companion object {
        private const val CHAT_ROOM_KEY = "personal-chat-room"
        fun generateIntent(context: Context, chatRoom: QiscusChatRoom): Intent {
            return Intent(context, PersonalChatRoomActivity::class.java).apply {
                putExtra(CHAT_ROOM_KEY, chatRoom)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_chat_room)
    }
}