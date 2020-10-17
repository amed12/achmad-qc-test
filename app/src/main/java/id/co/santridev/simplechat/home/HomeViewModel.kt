/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 5:32 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 5:32 AM
 *
 */

package id.co.santridev.simplechat.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.usecase.IChatRoomUseCase
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.Action
import id.co.santridev.simplechat.login.LoginViewModel

class HomeViewModel(
    private val chatRoomUseCase: IChatRoomUseCase,
    private val userUseCase: IUserUseCase
) :
    ViewModel() {
    private val _errorMessageString = MutableLiveData<String>()
    private val _chatRoomList = MutableLiveData<List<QiscusChatRoom>>()
    private val _chatRoomObject = MutableLiveData<QiscusChatRoom>()
    val errorMessageString: LiveData<String> = _errorMessageString
    var qiscusChatRoom: LiveData<List<QiscusChatRoom>> = _chatRoomList
    var qiscusChatRoomObject: LiveData<QiscusChatRoom> = _chatRoomObject


    fun loadChatRoom() {
        chatRoomUseCase.getChatRoom(onSuccess = object : Action<List<QiscusChatRoom>> {
            override fun call(t: List<QiscusChatRoom>) {
                _chatRoomList.postValue(t)
            }
        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                _errorMessageString.postValue(t.message.toString())
            }
        })
    }

    fun chatWithB() {
        userUseCase.getCurrentUser(LoginViewModel.USER_B, onSuccess = object : Action<User> {
            override fun call(t: User) {
                chatRoomUseCase.createChatRoom(t, onSuccess = object : Action<QiscusChatRoom> {
                    override fun call(t: QiscusChatRoom) {
                        _chatRoomObject.postValue(t)
                    }

                }, onError = object : Action<Throwable> {
                    override fun call(t: Throwable) {
                        _errorMessageString.postValue(t.message.toString())
                    }
                })
            }

        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                _errorMessageString.postValue(t.message.toString())
            }
        })
    }

}
