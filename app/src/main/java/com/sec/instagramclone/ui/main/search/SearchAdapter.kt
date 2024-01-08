package com.sec.instagramclone.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.databinding.CellSearchItemBinding

class SearchAdapter (var userList: ArrayList<UserBody>) : RecyclerView.Adapter<SearchVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val binding =
            CellSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchVH(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        var user = userList[position]
        holder.bind(user)

    }
}