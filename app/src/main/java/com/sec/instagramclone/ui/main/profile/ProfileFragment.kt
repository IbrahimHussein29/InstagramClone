package com.sec.instagramclone.ui.main.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sec.instagramclone.R
import com.sec.instagramclone.data.body.UserBody
import com.sec.instagramclone.data.common.onSuccess
import com.sec.instagramclone.databinding.FragmentProfileBinding
import com.sec.instagramclone.ui.common.extensions.collectLatestLifecycleFlow
import com.sec.instagramclone.ui.common.extensions.setOnSafeClickListener
import com.sec.instagramclone.ui.main.profile.adapter.ViewPagerAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var user: UserBody
    private var _binding: FragmentProfileBinding? = null
    private val viewModel by viewModels<ProfileVM>()
    private val binding get() = _binding!!
    val tabsArray = arrayOf("posts", "reels")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(layoutInflater)
        viewModel.getUserData()
        collectUserProfileData()
        bindData()
        setTabLayout()
findNavController().popBackStack(R.id.updateProfileFragment, true)
        return binding.root
    }

    private fun setTabLayout() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val myAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        viewPager.adapter = myAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsArray[position]

        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    setStyleForTab(it, Typeface.NORMAL)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    setStyleForTab(it, Typeface.BOLD)
                }
            }

            fun setStyleForTab(tab: TabLayout.Tab, style: Int) {
                tab.view.children.find { it is TextView }?.let { tv ->
                    (tv as TextView).post {
                        tv.setTypeface(null, style)
                    }
                }
            }
        })
    }

    private fun bindData() {
        binding.editProfileBtn.setOnSafeClickListener{

            findNavController().navigate(R.id.updateProfileFragment)
        }
    }

    private fun collectUserProfileData() {
        collectLatestLifecycleFlow(viewModel.userData) { it ->
            it?.onSuccess {
                user=it
                binding.name.text=user.name
                binding.bio.text=user.email
                if(!user.userImage.isNullOrEmpty()){
                    Picasso.get().load(user.userImage).into(binding.profileImg)
                }

            }
        }

    }





}