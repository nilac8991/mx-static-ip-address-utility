package com.zebra.nilac.mx.staticipaddressutility

import android.app.Application
import android.content.SharedPreferences

class DefaultApplication : Application() {

    init {
        INSTANCE = this
    }

    companion object {
        @Volatile
        private var INSTANCE: DefaultApplication? = null

        fun getInstance(): DefaultApplication {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = DefaultApplication()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}