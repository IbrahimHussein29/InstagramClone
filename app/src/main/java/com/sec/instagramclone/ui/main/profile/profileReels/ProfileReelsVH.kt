package com.sec.instagramclone.ui.main.profile.profileReels

import com.sec.instagramclone.data.body.ReelBody
import com.sec.instagramclone.databinding.CellPostItemBinding
import com.sec.instagramclone.ui.common.extensions.setReelUri
import com.sec.instagramclone.ui.common.viewHolder.BindingViewHolder

class ProfileReelsVH (binding: CellPostItemBinding) :
    BindingViewHolder<CellPostItemBinding>(binding) {
    fun bind(reel: ReelBody){
        binding.postImg.setReelUri(reel.reelUrl)
    }


}