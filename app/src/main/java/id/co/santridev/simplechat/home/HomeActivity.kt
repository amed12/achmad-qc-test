/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 8:33 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 8:32 PM
 *
 */

package id.co.santridev.simplechat.home

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import id.co.santridev.simplechat.R


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val s = SpannableString("My Simple Chat")
        s.setSpan(
            ForegroundColorSpan(Color.parseColor("#000000")), 0, s.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        supportActionBar?.title = s
    }
}