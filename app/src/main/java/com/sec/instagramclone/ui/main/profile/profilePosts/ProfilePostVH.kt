package com.sec.instagramclone.ui.main.profile.profilePosts

import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.databinding.CellPostItemBinding
import com.sec.instagramclone.ui.common.extensions.setImageCenteredCropped
import com.sec.instagramclone.ui.common.viewHolder.BindingViewHolder

class ProfilePostVH(binding: CellPostItemBinding) :
    BindingViewHolder<CellPostItemBinding>(binding) {
        fun bind(post: PostBody){
            binding.postImg.setImageCenteredCropped(post.postUrl)
        }


}