/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:58 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:57 AM
 *
 */

package id.co.santridev.simplechat.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.co.santridev.simplechat.MyApplication
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.utils.dialog.LoadingDialog
import id.co.santridev.simplechat.core.utils.extension.afterTextChanged
import id.co.santridev.simplechat.core.utils.extension.anyNotNull
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), ILoginPresenter {

    //    private val appInstance by lazy { MyApplication.getInstance() }
    private val loginPresenter by lazy {
        LoginPresenter(
            this,
            MyApplication.getInstance().getUserUseCase()
        )
    }
    private val loadingDialog by lazy { LoadingDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginPresenter.start()
        changeStateBtnLogin()
        edt_password_register.apply {
            afterTextChanged {
                changeStateBtnLogin()
            }
        }
    }

    override fun showLoading() {
        loadingDialog.show(true)
    }

    override fun dismissLoading() {
        loadingDialog.show(false)
    }

    override fun showHomePage() {
        Toast.makeText(this, "Yeah login", Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun changeStateBtnLogin() {
        val userEmail = edt_email_login.text.toString()
        val userName = edt_username_register.text.toString()
        val password = edt_password_register.text.toString()
        login.apply {
            if (anyNotNull(
                    userName,
                    userEmail,
                    password
                )
            ) {
                isEnabled = true
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    background.setTint(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.colorAccent
                        )
                    )
                } else {
                    visibility = View.GONE
                }
                setOnClickListener {
                    loginPresenter.login(userEmail, password, userName)
                }
            } else {
                isEnabled = false
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    background.setTint(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.colorAccentSecondary
                        )
                    )
                } else {
                    visibility = View.GONE
                }
                setOnClickListener {
                    //
                }
            }
        }
    }
}