package com.sec.instagramclone.ui.main.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.ui.common.extensions.toBinding
import com.sec.instagramclone.ui.main.home.viewHolders.HomePostsVH

class HomeAdapter(
    var items: ArrayList<PostBody>,
    var onItemClicked: (item: PostBody) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            //add story VH


            PostBody::class.java.hashCode() -> HomePostsVH(parent.toBinding())

            else -> throw UnsupportedOperationException("Unsupported")
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is HomePostsVH -> {
                holder.bind(item as PostBody)

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position]::class.java.hashCode()
    }
}