package com.example.yaplayground

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yaplayground.mvp.DuckDuckGoContract
import com.example.yaplayground.mvp.DuckDuckGoView
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    @field:[Inject Named("RXPresenter")] lateinit var rxPresenter : DuckDuckGoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SimpleApplication.component.inject(this)
        supportFragmentManager.beginTransaction().replace(R.id.fcontainer, DuckDuckGoView().withPresenter(rxPresenter), "RX").commit()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        SimpleApplication.component.inject(this)
        supportFragmentManager.beginTransaction().replace(R.id.fcontainer, DuckDuckGoView().withPresenter(rxPresenter), "RX").commit()
    }
}