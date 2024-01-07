package com.sec.instagramclone.ui.main.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sec.instagramclone.ui.main.profile.profilePosts.ProfilePostsFragment
import com.sec.instagramclone.ui.main.profile.profileReels.ProfileReelsFragment


class ProfileAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ProfilePostsFragment()


        }
        return ProfileReelsFragment()
    }


}
