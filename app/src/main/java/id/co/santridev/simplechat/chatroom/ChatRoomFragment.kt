/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 10:11 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 10:07 AM
 *
 */

package id.co.santridev.simplechat.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.utils.extension.QiscusPresenter


private const val KEY_CHAT_ROOM = "param1"

class ChatRoomFragment : Fragment(), QiscusPresenter.View {
    private var chatRoom: QiscusChatRoom? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatRoom = it.getParcelable(KEY_CHAT_ROOM)
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

    override fun showError(errorMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun dismissLoading() {
        TODO("Not yet implemented")
    }
}