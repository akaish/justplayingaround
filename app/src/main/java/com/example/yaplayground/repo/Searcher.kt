package com.example.yaplayground.repo

fun interface Searcher {

    sealed class ApiResult(val query: String, val timestamp: Long) {
        class Error(val tr: Throwable, query: String, timestamp: Long) : ApiResult(query, timestamp)
        class Success(val searchResult: SearchResult, query: String, timestamp: Long) : ApiResult(query, timestamp)
    }

    data class SearchResult(val searchQuery: String, val timestamp: Long, val results: List<Entry>) {
        data class Entry(val title: String, val url: String, val description: String)
    }

    fun search(query: String) : ApiResult
}