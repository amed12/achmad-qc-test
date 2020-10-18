/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 12:53 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 12:52 PM
 *
 */

package id.co.santridev.simplechat.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.chatroom.PersonalChatRoomActivity
import id.co.santridev.simplechat.core.utils.adapter.ChatRoomAdapter
import id.co.santridev.simplechat.core.utils.adapter.OnItemClickListener
import id.co.santridev.simplechat.core.utils.extension.showToast
import id.co.santridev.simplechat.core.utils.ui.ViewModelFactory
import id.co.santridev.simplechat.login.LoginActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), OnItemClickListener {
    private val factory by lazy { ViewModelFactory.getInstance(this) }
    private val homeViewModel by lazy {
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }
    private val chatRoomAdapter by lazy { ChatRoomAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        recyclerview.setHasFixedSize(true)
        chatRoomAdapter.setOnItemClickListener(this)
        recyclerview.adapter = chatRoomAdapter
        btn_log_out.setOnClickListener {
            homeViewModel.logOut()
        }
        create_chat.setOnClickListener {
            homeViewModel.chatWithB()
        }
        homeViewModel.qiscusChatRoom.observe(this, {
            if (it.isNotEmpty()) {
                recyclerview.visibility = View.VISIBLE
                linEmptyChatRooms.visibility = View.GONE
            } else {
                recyclerview.visibility = View.GONE
                linEmptyChatRooms.visibility = View.VISIBLE
            }
            chatRoomAdapter.addOrUpdate(it)
        })
        homeViewModel.errorMessageString.observe(this, {
            if (it == getString(R.string.action_log_out)) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
            showToast(it)
        })
        homeViewModel.qiscusChatRoomObject.observe(this, {
            startActivity(PersonalChatRoomActivity.generateIntent(this, it))
        })

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadChatRoom()
    }

    override fun onItemClick(position: Int) {
        homeViewModel.chatWithOtherExisting(chatRoomAdapter.data[position])
    }
}