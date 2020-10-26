package com.example.yaplayground.dependency

import com.example.yaplayground.Icons
import com.example.yaplayground.MainActivity
import com.example.yaplayground.mvp.DuckDuckGoContract
import com.example.yaplayground.mvp.LiveDataFields
import com.example.yaplayground.mvp.ResultItem
import com.example.yaplayground.mvp.rxfrag.DuckDuckGoPresenter
import com.example.yaplayground.repo.HTTPSearch
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [Module::class]
)
interface Component {
    @Named("url") fun apiUrlPattern() : String
    fun httpClient() : OkHttpClient
    fun liveDataFields() : LiveDataFields
    @Named("RXPresenter") fun rxPresenter() : DuckDuckGoContract.Presenter
    fun icons() : Icons

    fun inject(p: HTTPSearch)
    fun inject(p: DuckDuckGoPresenter)
    fun inject(p: ResultItem.ViewHolder)
    fun inject(p: MainActivity)

}