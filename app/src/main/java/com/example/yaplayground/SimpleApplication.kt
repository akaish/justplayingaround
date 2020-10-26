package com.example.yaplayground

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.yaplayground.dependency.Component
import com.example.yaplayground.dependency.DaggerComponent
import com.example.yaplayground.dependency.Module

class SimpleApplication : MultiDexApplication() {

    companion object {
        @JvmStatic lateinit var component: Component
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerComponent.builder().module(Module(applicationContext)).build()
    }
}