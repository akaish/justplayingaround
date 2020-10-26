package com.example.yaplayground.mvp

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.yaplayground.repo.Searcher

class DuckDuckGoContract {

    interface View : MVPContract.View {
        fun searchViewLiveData() : LiveData<CharSequence>
        fun applyResults(result: Searcher.ApiResult?)
        fun restore(result: Searcher.ApiResult?)
        fun withPresenter(presenter: DuckDuckGoContract.Presenter) : Fragment
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun onDestroy()
    }
}