package com.example.yaplayground.dependency

import android.content.Context
import com.example.yaplayground.Icons
import com.example.yaplayground.mvp.DuckDuckGoContract
import com.example.yaplayground.mvp.LiveDataFields
import com.example.yaplayground.mvp.rxfrag.DuckDuckGoPresenter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module class Module(private val appContext: Context) {
    @Provides @Singleton @Named("url") fun apiUrlPattern() = "https://html.duckduckgo.com/html/?q={0}"

    @Provides fun httpClient() : OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .callTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides @Singleton fun liveDataFields() = LiveDataFields()

    @Provides @Singleton @Named("RXPresenter") fun rxPresenter() : DuckDuckGoContract.Presenter {
        return DuckDuckGoPresenter()
    }

    @Provides @Singleton fun icons() = Icons(appContext)
}