package com.sec.instagramclone.ui.main.reels

import android.view.View
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.databinding.CellReelsViewBinding
import com.sec.instagramclone.ui.common.extensions.setImageUrl
import com.sec.instagramclone.ui.common.viewHolder.BindingViewHolder

class ReelsVH(binding: CellReelsViewBinding) :
    BindingViewHolder<CellReelsViewBinding>(binding) {
    fun bind(reel: ReelBody) {
        binding.profileImg.setImageUrl(reel.profileLink)
        binding.captionTxt.text = reel.reelCaption
        try {
            val text = TimeAgo.using(reel._time.toLong())
            binding.timeTxt.text=text
        }catch (e:Exception){
            binding.timeTxt.text=""
        }
        binding.progressBar.visibility= View.GONE
        binding.videoView.setVideoPath(reel.reelUrl)
        binding.videoView.setOnPreparedListener {
            it.start()
        }
    }


}