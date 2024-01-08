package com.sec.instagramclone.ui.main.search

import com.google.common.io.Resources
import com.sec.instagramclone.R
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.Resource
import com.sec.instagramclone.databinding.CellSearchItemBinding
import com.sec.instagramclone.ui.common.extensions.setImageCenteredCropped
import com.sec.instagramclone.ui.common.viewHolder.BindingViewHolder

class SearchVH(binding: CellSearchItemBinding) :
    BindingViewHolder<CellSearchItemBinding>(binding) {
    fun bind(user: UserBody) {
        binding.profileImg.setImageCenteredCropped(user.userImage)
        binding.userName.text=user.name

binding.followBtn.setOnClickListener {

    if(user.isFollowed){
        binding.followBtn.text= context.resources.getText(R.string.un_follow)
        user.isFollowed= false
    }else{
        binding.followBtn.text= context.resources.getText(R.string.follow)
        user.isFollowed= true
    }

}
    }


}