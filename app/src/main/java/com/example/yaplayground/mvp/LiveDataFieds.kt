package com.example.yaplayground.mvp

import androidx.lifecycle.LiveData
import com.example.yaplayground.repo.Searcher

class LiveDataFields {
    class RXQueryLive : LiveData<CharSequence>("") {
        fun submit(query: CharSequence) = postValue(query)
    }
    class SearcherLive : LiveData<Searcher.ApiResult?>() {
        fun submit(result: Searcher.ApiResult?) = postValue(result)
    }

    val queryLive = RXQueryLive()
    val searcherLive = SearcherLive()
}