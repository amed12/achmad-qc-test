/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 7:46 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 7:45 AM
 *
 */

package id.co.santridev.simplechat.chatroom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.request.RequestOptions
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusRoomMember
import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.utils.extension.Nirmana
import kotlinx.android.synthetic.main.activity_personal_chat_room.*
import rx.Observable

class PersonalChatRoomActivity : AppCompatActivity() {
    private lateinit var chatRoom: QiscusChatRoom
    private var opponentEmail = ""

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
        chatRoom = intent.getParcelableExtra(CHAT_ROOM_KEY) ?: QiscusChatRoom()
        if (chatRoom.name.isNullOrEmpty()) {
            finish()
            return
        }
        avatar?.let {
            Nirmana.getInstance().get()
                .setDefaultRequestOptions(RequestOptions().apply {
                    placeholder(R.drawable.ic_qiscus_avatar)
                    error(R.drawable.ic_qiscus_avatar)
                    dontAnimate()
                })
                .load(chatRoom.avatarUrl)
                .into(it)
        }

        room_name?.text = chatRoom.name
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                ChatRoomFragment.newInstance(chatRoom),
                ChatRoomFragment::class.simpleName
            ).commit()

        getOpponentIfNotGroupEmail()
        listenUser()
        bt_back?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        unlistenUser()
        super.onDestroy()
    }

    private fun listenUser() {
        if (!chatRoom.isGroup && opponentEmail.isNotEmpty()) {
            QiscusPusherApi.getInstance().subscribeUserOnlinePresence(opponentEmail)
        }
    }

    private fun getOpponentIfNotGroupEmail() {
        if (!chatRoom.isGroup) {
            opponentEmail = Observable.from(chatRoom.member)
                .map(QiscusRoomMember::getEmail)
                .filter { email -> email != QiscusCore.getQiscusAccount().email }
                .first()
                .toBlocking()
                .single()
        }
    }

    private fun unlistenUser() {
        if (!chatRoom.isGroup && opponentEmail.isNotEmpty()) {
            QiscusPusherApi.getInstance().unsubscribeUserOnlinePresence(opponentEmail)
        }
    }
}