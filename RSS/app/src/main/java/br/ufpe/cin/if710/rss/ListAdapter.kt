package br.ufpe.cin.if710.rss

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import br.ufpe.cin.if710.rss.R.layout.itemlista
import kotlinx.android.synthetic.main.itemlista.view.*

class ListAdapter(private val items: List<ItemRSS>, val clickListener: (ItemRSS) -> Unit, private val context: Context) : Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(item: ItemRSS, clickFunc: (ItemRSS) -> Unit) {
            itemView.item_titulo.text = item.title
            itemView.item_data.text = item.pubDate
            itemView.setOnClickListener { clickFunc(item)}
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemlista, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position], clickListener);
    }
}

