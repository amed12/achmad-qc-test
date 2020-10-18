/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 10:11 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 10:07 AM
 *
 */

package id.co.santridev.simplechat.core.utils.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QiscusChatScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val listener: Listener
) :
    RecyclerView.OnScrollListener() {
    private var onTop = false
    private var onBottom = true
    private var onMiddle = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (linearLayoutManager.findFirstVisibleItemPosition() <= 0 && !onTop) {
            listener.onBottomOffListMessage()
            onBottom = true
            onTop = false
            onMiddle = false
        } else if (linearLayoutManager.findLastVisibleItemPosition() >= linearLayoutManager.itemCount - 1 && !onBottom) {
            listener.onTopOffListMessage()
            onTop = true
            onBottom = false
            onMiddle = false
        } else if (!onMiddle) {
            listener.onMiddleOffListMessage()
            onMiddle = true
            onTop = false
            onBottom = false
        }
    }

    interface Listener {
        fun onTopOffListMessage()
        fun onMiddleOffListMessage()
        fun onBottomOffListMessage()
    }
}
