/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 5:37 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 5:37 AM
 *
 */

package id.co.santridev.simplechat.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.utils.adapter.ChatRoomAdapter
import id.co.santridev.simplechat.core.utils.adapter.OnItemClickListener
import id.co.santridev.simplechat.core.utils.extension.showToast
import id.co.santridev.simplechat.core.utils.ui.ViewModelFactory
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
            showToast(it)
        })
        homeViewModel.qiscusChatRoomObject.observe(this, {
            showToast(it.name)
        })

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadChatRoom()
    }

    override fun onItemClick(position: Int) {
        showToast(chatRoomAdapter.data[position].name)
    }
}