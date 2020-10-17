/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:40 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 1:39 PM
 *
 */

package id.co.santridev.simplechat.core.data.source

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import id.co.santridev.simplechat.core.data.source.pref.PrefsHelper
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.repository.IUserRepository
import id.co.santridev.simplechat.core.utils.Action
import id.co.santridev.simplechat.core.utils.AvatarUtil
import rx.Emitter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class UserRepository(context: Context) : IUserRepository {
    private val mPrefs: SharedPreferences = PrefsHelper.init(context)
    private val gson: Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(context)
            }
    }

    override fun getCurrentUser(key: String, onSuccess: Action<User>, onError: Action<Throwable>) {
        getCurrentUserObservable(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.call(it) }) { onError.call(it) }
    }


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
            .doOnNext { setCurrentUser(email, it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess.call(it) }) { onError.call(it) }
    }

    private fun mapFromQiscusAccount(qiscusAccount: QiscusAccount): User = User(
        id = qiscusAccount.email,
        name = qiscusAccount.username,
        avatarUrl = qiscusAccount.avatar
    )

    private fun getCurrentUser(keyString: String): User = gson.fromJson(
        mPrefs.getString(keyString, ""),
        User::class.java
    ) ?: User()

    private fun setCurrentUser(keyString: String, user: User) {
        mPrefs.edit()?.putString(keyString, gson.toJson(user))?.apply()
    }

    private fun getCurrentUserObservable(keyString: String): Observable<User> =
        Observable.create({ subscriber ->
            try {
                subscriber.onNext(getCurrentUser(keyString))
            } catch (e: Exception) {
                subscriber.onError(e)
            } finally {
                subscriber.onCompleted()
            }
        }, Emitter.BackpressureMode.BUFFER)

}
