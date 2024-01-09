package com.sec.instagramclone.ui.main.home
import android.view.ViewGroup
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.sec.instagramclone.data.body.MediaBody
import com.sec.instagramclone.ui.common.extensions.toBinding
import com.sec.instagramclone.ui.main.home.viewHolders.HomePostsVH

class HomeAdapter(
    var items: ArrayList<MediaBody>,
    var players:ArrayList<ExoPlayer>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            //add story VH


            MediaBody::class.java.hashCode() -> HomePostsVH(parent.toBinding())

            else -> throw UnsupportedOperationException("Unsupported")
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val player=players[position]

        when (holder) {
            is HomePostsVH -> {
                holder.bind(item, player)

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position]::class.java.hashCode()
    }
}