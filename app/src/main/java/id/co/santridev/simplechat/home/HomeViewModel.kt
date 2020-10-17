/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 4:59 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 4:59 AM
 *
 */

package id.co.santridev.simplechat.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import id.co.santridev.simplechat.core.domain.usecase.IChatRoomUseCase
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.Action

class HomeViewModel(private val chatRoomUseCase: IChatRoomUseCase, userUseCase: IUserUseCase) :
    ViewModel() {
    private val _errorMessageString = MutableLiveData<String>()
    private val _chatRoomList = MutableLiveData<List<QiscusChatRoom>>()
    val errorMessageString: LiveData<String> = _errorMessageString
    var qiscusChatRoom: LiveData<List<QiscusChatRoom>> = _chatRoomList


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

}
