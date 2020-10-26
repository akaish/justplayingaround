package com.example.yaplayground.mvp

import android.view.View
import com.example.yaplayground.Icons
import com.example.yaplayground.R
import com.example.yaplayground.SimpleApplication
import com.example.yaplayground.repo.Searcher
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.entry.view.*
import javax.inject.Inject

class ResultItem(val entry: Searcher.SearchResult.Entry) : AbstractItem<ResultItem, ResultItem.ViewHolder>() {
    override fun getType() = R.id.result_id
    override fun getViewHolder(v: View) = ViewHolder(v)
    override fun getLayoutRes() = R.layout.entry

    class ViewHolder(val view: View): FastAdapter.ViewHolder<ResultItem>(view) {

        @Inject lateinit var icons: Icons

        val shareLayout: View = view.findViewById(R.id.share_layout)

        override fun unbindView(item: ResultItem) {
            view.share_button.setImageDrawable(null)
            view.title.text = null
            view.description.text = null
        }

        override fun bindView(item: ResultItem, payloads: MutableList<Any>) {
            SimpleApplication.component.inject(this)
            view.share_button.setImageDrawable(icons.shareIcon)
            view.title.text = item.entry.title
            view.description.text = item.entry.description
        }
    }
}