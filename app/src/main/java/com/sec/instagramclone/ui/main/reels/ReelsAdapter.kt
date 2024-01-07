package com.sec.instagramclone.ui.main.reels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.databinding.CellReelsViewBinding

class ReelsAdapter(var reelList: ArrayList<ReelBody>) : RecyclerView.Adapter<ReelsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelsVH {
        val binding =
            CellReelsViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReelsVH(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: ReelsVH, position: Int) {
        var reel = reelList[position]
        holder.bind(reel)

    }
}