/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 10:33 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 10:23 PM
 *
 */

package id.co.santridev.simplechat.core.utils.extension;

import android.content.res.Resources;

import androidx.annotation.RestrictTo;

/**
 * @author Yuana andhikayuana@gmail.com
 * @since Aug, Thu 02 2018 11.48
 **/
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class QiscusConverterUtil {

    private QiscusConverterUtil() {
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
