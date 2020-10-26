package com.example.yaplayground.repo

import com.example.yaplayground.SimpleApplication
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.lang.RuntimeException
import java.net.URLEncoder
import java.text.MessageFormat.format
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class HTTPSearch : Searcher {

    init {
        SimpleApplication.component.inject(this)
    }

    class HTTPStatusException(val status: Int) : RuntimeException()

    @field:[Named("url") Inject] lateinit var apiUrlPattern: String
    @Inject lateinit var client: OkHttpClient

    private fun executeSearchQuery(query: String) : String {
        val request = Request.Builder().url(format(apiUrlPattern, URLEncoder.encode(query, "UTF-8"))).build()
        val response = client.newCall(request).execute()
        response.use { response ->
            if (response.code != 200)
                throw HTTPStatusException(response.code)
            return response.body!!.string()
        }
    }

    private fun parseQueryResults(body: String) : List<Searcher.SearchResult.Entry> {
        val document = Jsoup.parseBodyFragment(body)
        val results = document.getElementsByClass("result results_links results_links_deep web-result ")
        val out = LinkedList<Searcher.SearchResult.Entry>()
        for(result in results) {
            val title: String
            val url: String
            val description: String
            val titles = result.getElementsByClass("result__title")
            if(titles.isNotEmpty()) {
                val link = titles[0].select("a")
                url = link.attr("href")
                title = link.text()
            } else continue
            val body = result.getElementsByClass("result__snippet")
            if(body.isNotEmpty())
                description = body.text()
            else continue
            out.add(Searcher.SearchResult.Entry(title, url, description))
        }
        return out
    }

    override fun search(query: String): Searcher.ApiResult {
        val timestamp = System.currentTimeMillis()
        return try {
            val body = executeSearchQuery(query)
            val entries = parseQueryResults(body)
            Searcher.ApiResult.Success(Searcher.SearchResult(query, timestamp, entries), query, timestamp)
        } catch (tr: Throwable) {
            Searcher.ApiResult.Error(tr, query, timestamp)
        }
    }
}