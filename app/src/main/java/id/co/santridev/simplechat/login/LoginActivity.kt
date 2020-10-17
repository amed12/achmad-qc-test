/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:51 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:50 PM
 *
 */

package id.co.santridev.simplechat.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.domain.model.UserData
import id.co.santridev.simplechat.core.utils.dialog.LoadingDialog
import id.co.santridev.simplechat.core.utils.extension.afterTextChanged
import id.co.santridev.simplechat.core.utils.extension.anyNotNull
import id.co.santridev.simplechat.core.utils.extension.disable
import id.co.santridev.simplechat.core.utils.ui.ViewModelFactory
import id.co.santridev.simplechat.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val factory by lazy { ViewModelFactory.getInstance(this) }
    private val loginViewModel by lazy {
        ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }
    private val loadingDialog by lazy { LoadingDialog(this) }
    private var userData = UserData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel.start(LoginViewModel.USER_B)
        loginViewModel.errorMessage.observe(this, {
            if (it == "isLoginA") {
                loginViewModel.login(userData.email, userData.password, userData.name, true)
            } else {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
        loginViewModel.stateHomeData.observe(this, {
            if (it) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                edt_email_login?.apply {
                    setText(LoginViewModel.USER_B)
                    disable()
                }
                edt_password_register?.text?.clear()
                edt_username_register?.text?.clear()
                edt_password_register?.apply {
                    setText(getString(R.string.default_password_b))
                    disable()
                }
            }
        })
        loginViewModel.stateLoading.observe(this, {
            loadingDialog.show(it)
        })

        changeStateBtnLogin()
        edt_password_register.apply {
            afterTextChanged {
                changeStateBtnLogin()
            }
        }
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
                    if (userEmail != LoginViewModel.USER_B) {
                        userData = UserData(userName, userEmail, password)
                    }
                    loginViewModel.login(
                        email = userEmail,
                        name = userName,
                        password = password,
                        isLoginA = false
                    )
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