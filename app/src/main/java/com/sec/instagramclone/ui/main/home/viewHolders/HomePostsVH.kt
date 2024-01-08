package com.sec.instagramclone.ui.main.home.viewHolders

import android.content.Intent
import android.view.View
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.sec.instagramclone.R
import com.sec.instagramclone.data.body.PostBody
import com.sec.instagramclone.databinding.CellHomePostsBinding
import com.sec.instagramclone.ui.common.extensions.setImageCenteredCropped
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.sec.instagramclone.ui.common.viewHolder.BindingViewHolder

class HomePostsVH (binding: CellHomePostsBinding) :
BindingViewHolder<CellHomePostsBinding>(binding) {
    fun bind(post: PostBody) {
        var flag=true
     binding.profileImg.setImageCenteredCropped(post.postUserImage)
        binding.name.text=post.postUsername
        try {
            val text = TimeAgo.using(post._time.toLong())
            binding.time.text=text
        }catch (e:Exception){
            binding.time.text=""
        }

        binding.postImg.setImageCenteredCropped(post.postUrl)
       if(post.caption.isNullOrBlank()){
           binding.captionTxt.visibility= View.GONE
       }else{
           binding.captionTxt.text=post.caption
       }
binding.likeImg.setOnSafeClickListener{
    if(flag){
       binding.likeImg.setImageResource(R.drawable.ic_like_2)
        flag= false
    }else{
        binding.likeImg.setImageResource(R.drawable.ic_like)
        flag= true
    }


}
        binding.shareImg.setOnSafeClickListener{
            val i = Intent(Intent.ACTION_SEND)
            i.type="text/plain"
            i.putExtra(Intent.EXTRA_TEXT,post.postUrl)
            context.startActivity(i)
        }




    }

}