/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 10:33 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 10:04 PM
 *
 */

package id.co.santridev.simplechat.core.utils.ui;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created on : February 03, 2017
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public interface QiscusProgressView {
    int getProgress();

    void setProgress(int progress);

    int getFinishedColor();

    void setFinishedColor(int finishedColor);

    int getUnfinishedColor();

    void setUnfinishedColor(int unfinishedColor);

    void setVisibility(@Visibility int visibility);

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {
    }
}
