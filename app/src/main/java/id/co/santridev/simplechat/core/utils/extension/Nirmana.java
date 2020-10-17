/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 10:33 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 10:01 PM
 *
 */

package id.co.santridev.simplechat.core.utils.extension;

import android.annotation.SuppressLint;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class Nirmana {
    @SuppressLint({"StaticFieldLeak"})
    private static Nirmana instance;
    private RequestManager requestManager;

    private Nirmana(Context context) {
        this.requestManager = Glide.with(context.getApplicationContext());
    }

    public static void init(Context context) {
        if (instance == null) {
            Class var1 = Nirmana.class;
            synchronized (Nirmana.class) {
                if (instance == null) {
                    instance = new Nirmana(context);
                }
            }
        }

    }

    public static Nirmana getInstance() {
        if (instance == null) {
            Class var0 = Nirmana.class;
            synchronized (Nirmana.class) {
                if (instance == null) {
                    throw new RuntimeException("Please init Nirmana before. Call Nirmana.init(context)");
                }
            }
        }

        return instance;
    }

    public RequestManager get() {
        return this.requestManager;
    }
}
