package com.example.yaplayground.mvp.rxfrag

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.yaplayground.SimpleApplication
import com.example.yaplayground.mvp.DuckDuckGoContract
import com.example.yaplayground.mvp.LiveDataFields
import com.example.yaplayground.repo.HTTPSearch
import com.example.yaplayground.repo.Searcher
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DuckDuckGoPresenter : DuckDuckGoContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var view: DuckDuckGoContract.View
    private lateinit var searchViewObserver: Observer<CharSequence?>
    private val searchObserver = Observer<Searcher.ApiResult?> {
        view.applyResults(it)
    }

    @Inject lateinit var liveDataFields: LiveDataFields

    init {
        SimpleApplication.component.inject(this)
    }

    override fun subscribe() {
        view.applyResults(liveDataFields.searcherLive.value)
        view.searchViewLiveData().observeForever(searchViewObserver)
        liveDataFields.searcherLive.observe(this.view as LifecycleOwner, searchObserver)
    }

    override fun unsubscribe() {
        liveDataFields.searcherLive.removeObservers(view as LifecycleOwner)
        view.searchViewLiveData().removeObserver(searchViewObserver)
    }

    override fun onDestroy() {
        view.searchViewLiveData().removeObserver(searchViewObserver)
        compositeDisposable.dispose()
    }

    override fun attach(view: DuckDuckGoContract.View) = apply {
        this.view = view
        view.restore(liveDataFields.searcherLive.value)
        val disposable = Observable.create<CharSequence> {
            searchViewObserver = Observer<CharSequence?> { fromView ->
                if(fromView == null)
                    it.onNext("")
                else it.onNext(fromView)
            }
        }.filter { it.isNotBlank() && it.isNotEmpty() }
            .debounce(1, TimeUnit.SECONDS)
            .distinctUntilChanged()
            .switchMap { query ->
                Observable.fromCallable {
                    HTTPSearch().search(query.toString())
                }
            }.subscribeOn(Schedulers.io())
            .subscribe {
                liveDataFields.searcherLive.submit(it)
            }
        compositeDisposable.add(disposable)
    }
}