package com.example.testing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testing.MainActivity.Companion.TAG
import kotlinx.android.synthetic.main.list_item_test_item.view.*
import kotlin.random.Random

class PlaceholderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val items: MutableList<TestItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_test_item, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitItems(items: List<TestItem>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: TestItem) {
            itemView.hephoi.text = Random.nextInt(1, 100).toString()
        }

    }
}