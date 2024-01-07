package com.sec.instagramclone.ui.main.reels

import android.view.View
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.databinding.CellReelsViewBinding
import com.sec.instagramclone.ui.common.extensions.setImageUrl
import com.sec.instagramclone.ui.common.viewHolder.BindingViewHolder

class ReelsVH(binding: CellReelsViewBinding) :
    BindingViewHolder<CellReelsViewBinding>(binding) {
    fun bind(reel: ReelBody) {
        binding.profileImg.setImageUrl(reel.profileLink)
        binding.captionEdtTxt.text = reel.reelCaption
        binding.progressBar.visibility= View.GONE
        binding.videoView.setVideoPath(reel.reelUrl)
        binding.videoView.setOnPreparedListener {
            it.start()
        }
    }


}