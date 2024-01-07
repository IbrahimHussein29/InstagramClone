package com.sec.instagramclone.ui.main.profile.profileReels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.databinding.CellPostItemBinding

class ProfileReelsAdapter(var reelList: ArrayList<ReelBody>) : RecyclerView.Adapter<ProfileReelsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileReelsVH {
        val binding =
            CellPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileReelsVH(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: ProfileReelsVH, position: Int) {
        var reel=reelList[position]
        holder.bind(reel)

    }
}