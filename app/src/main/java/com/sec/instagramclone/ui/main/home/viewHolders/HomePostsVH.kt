package com.sec.instagramclone.ui.main.home.viewHolders

import android.content.Intent
import android.view.View
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.sec.instagramclone.R
import com.sec.instagramclone.data.body.MediaBody
import com.sec.instagramclone.databinding.CellHomePostsBinding
import com.sec.instagramclone.ui.common.extensions.setImageCenteredCropped
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.sec.instagramclone.ui.common.viewHolder.BindingViewHolder

class HomePostsVH (binding: CellHomePostsBinding) :
BindingViewHolder<CellHomePostsBinding>(binding) {
    fun bind(media: MediaBody) {
        var flag=true
        if(media.videoUrl!=""){
            binding.videoView.visibility=View.VISIBLE
            binding.postImg.visibility=View.GONE
            binding.videoView.setVideoPath(media.videoUrl)
            binding.videoView.setOnPreparedListener {
                it.start()
            }

        }else{
            binding.postImg.setImageCenteredCropped(media.postUrl)
        }

        binding.name.text= media.mediaUsername
        try {
            val text = TimeAgo.using(media.time.toLong())
            binding.time.text=text
        }catch (e:Exception){
            binding.time.text=""
        }


        binding.profileImg.setImageCenteredCropped(media.mediaUserImage)
       if(media.caption.isNullOrBlank()){
           binding.captionTxt.visibility= View.GONE
       }else{
           binding.captionTxt.text= media.caption
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
            i.putExtra(Intent.EXTRA_TEXT, media.postUrl)
            context.startActivity(i)
        }




    }

}