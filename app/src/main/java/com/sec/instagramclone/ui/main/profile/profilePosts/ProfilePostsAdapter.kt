package com.sec.instagramclone.ui.main.profile.profilePosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.databinding.CellPostItemBinding

class ProfilePostsAdapter(var postList: ArrayList<PostBody>) : RecyclerView.Adapter<ProfilePostVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostVH {
        val binding =
            CellPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfilePostVH(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ProfilePostVH, position: Int) {
        var post=postList[position]
        holder.bind(post)

    }
}