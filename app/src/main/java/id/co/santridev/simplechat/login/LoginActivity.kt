/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:40 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 1:40 PM
 *
 */

package id.co.santridev.simplechat.login

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.utils.dialog.LoadingDialog
import id.co.santridev.simplechat.core.utils.extension.afterTextChanged
import id.co.santridev.simplechat.core.utils.extension.anyNotNull
import id.co.santridev.simplechat.core.utils.ui.ViewModelUserFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val factory by lazy { ViewModelUserFactory.getInstance(this) }
    private val loginViewModel by lazy {
        ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }
    private val loadingDialog by lazy { LoadingDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel.start(LoginViewModel.USER_B)
        loginViewModel.errorMessage.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        loginViewModel.stateHomeData.observe(this, {
            if (it)
                Toast.makeText(this, "Login yeah", Toast.LENGTH_SHORT).show()
            else {
                edt_email_login?.apply {
                    setText(LoginViewModel.USER_B)
                    isFocusable = false
                    isEnabled = false
                    isCursorVisible = false
                    keyListener = null
                    setBackgroundColor(Color.TRANSPARENT)
                }
                edt_password_register?.text?.clear()
                edt_username_register?.text?.clear()
                edt_password_register?.apply {
                    setText(getString(R.string.default_password_b))
                    isFocusable = false
                    isEnabled = false
                    isCursorVisible = false
                    keyListener = null
                    setBackgroundColor(Color.TRANSPARENT)
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
                    loginViewModel.login(userEmail, password, userName)
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