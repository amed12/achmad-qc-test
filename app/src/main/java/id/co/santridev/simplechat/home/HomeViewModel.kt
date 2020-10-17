/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:50 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:49 PM
 *
 */

package id.co.santridev.simplechat.home

import androidx.lifecycle.ViewModel
import id.co.santridev.simplechat.core.domain.usecase.IChatRoomUseCase
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase

class HomeViewModel(chatRoomUseCase: IChatRoomUseCase, userUseCase: IUserUseCase) : ViewModel()
