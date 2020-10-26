package com.example.yaplayground.mvp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yaplayground.R
import com.example.yaplayground.repo.Searcher
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.search.view.*


class DuckDuckGoView : Fragment(), DuckDuckGoContract.View {

    lateinit var presenter : DuckDuckGoContract.Presenter
    lateinit var rootView : View

    // Fast adapter
    private lateinit var fastAdapter: FastAdapter<ResultItem>
    private lateinit var itemAdapter: ItemAdapter<ResultItem>
    private val searchLiveData  = MutableLiveData<CharSequence>()

    //----------------------------------------------------------------------------------------------
    // Common
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.search, container, false)
        itemAdapter = ItemAdapter.items()
        fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.withSelectable(false)
        fastAdapter.setHasStableIds(false)
        // On click action (key emulation)
        fastAdapter.withOnClickListener { _, _, item, _ ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https:" + item.entry.url))
            startActivity(browserIntent)
            true
        }
        // Default icon click
        fastAdapter.withEventHook(object : ClickEventHook<ResultItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder) =
                if (viewHolder is ResultItem.ViewHolder) viewHolder.shareLayout else null

            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<ResultItem>, item: ResultItem) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_SUBJECT, item.entry.title)
                share.putExtra(Intent.EXTRA_TEXT, "https:" + item.entry.url)
                startActivity(Intent.createChooser(share, "Share link"))
            }
        })
        rootView.recycler.layoutManager = LinearLayoutManager(context)
        rootView.recycler.adapter = fastAdapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        rootView.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                searchLiveData.postValue(s?.toString() ?: "")
            }
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden) presenter.unsubscribe()
        else presenter.subscribe()
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun withPresenter(presenter: DuckDuckGoContract.Presenter) = apply {
        this.presenter = presenter
    }

    override fun searchViewLiveData() = searchLiveData

    override fun applyResults(result: Searcher.ApiResult?) {
        itemAdapter.clear()
        if(result is Searcher.ApiResult.Success) {
            itemAdapter.add(result.searchResult.results.map {
                ResultItem(it)
            })
        }
    }

    override fun restore(result: Searcher.ApiResult?) {
        rootView.search.setText(result?.query ?: "")
        itemAdapter.clear()
        if(result is Searcher.ApiResult.Success) {
            itemAdapter.add(result.searchResult.results.map {
                ResultItem(it)
            })
        }
    }
}