/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 11:46 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 11:45 PM
 *
 */

package id.co.santridev.simplechat.core.data.source

import android.content.Context
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import id.co.santridev.simplechat.core.data.source.pref.PreferenceHelper
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.repository.IUserRepository
import id.co.santridev.simplechat.core.utils.Action
import id.co.santridev.simplechat.core.utils.AvatarUtil
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class UserRepository private constructor(private val context: Context) : IUserRepository {
    private val preferenceHelpers by lazy {
        PreferenceHelper(context)
    }

    override fun setCurrentUser(user: User) {
        preferenceHelpers.setCurrentUser(user)
    }

    override fun getCurrentUser(): User = preferenceHelpers.getCurrentUser()
    override fun login(
        email: String,
        password: String,
        name: String,
        onSuccess: Action<User>,
        onError: Action<Throwable>
    ) {
        QiscusCore.setUser(email, password)
            .withUsername(name)
            .withAvatarUrl(AvatarUtil.generateAvatar(name))
            .save()
            .map { this.mapFromQiscusAccount(it) }
            .doOnNext { this.setCurrentUser(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.call(it) }) { onError.call(it) }
    }

    private fun mapFromQiscusAccount(qiscusAccount: QiscusAccount): User = User(
        id = qiscusAccount.email,
        name = qiscusAccount.username,
        avatarUrl = qiscusAccount.avatar
    )
}