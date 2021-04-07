package com.dicoding.githubuser

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.item_row_user.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.fragment_1,
            R.string.fragment_2
        )
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel = DetailViewModel()

        setData()


    }

    private fun setData(){
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        detailViewModel.setUserDetail(user.name)
        detailViewModel.getUserDetail().observe(this,{
            binding.apply {
                Glide.with(this@UserDetailActivity)
                    .load(it.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgAvatar)
                txtName.text = it.name
                txtUserName.text = it.username
                txtCompany.text = it.company
                txtLocation.text = it.location
                txtFollowers.text = it.followers
                txtFollowing.text = it.following
                txtRepository.text = it.repository

            }
        })
        initTabLayout(userName = String())

        user.name?.let {
            initTabLayout(it)
            detailViewModel.setUserDetail(it)
        }
    }

    private fun initTabLayout(userName: String) {

        val pagerAdapter = SectionPagerAdapter(this)
        pagerAdapter.username = userName
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
}







