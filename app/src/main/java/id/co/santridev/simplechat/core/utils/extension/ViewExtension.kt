/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 8:00 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 8:00 PM
 *
 */

package id.co.santridev.simplechat.core.utils.extension

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.disable() {
    this.apply {
        isFocusable = false
        isEnabled = false
        isCursorVisible = false
        keyListener = null
        setBackgroundColor(Color.TRANSPARENT)
    }
}