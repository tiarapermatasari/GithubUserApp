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
    private lateinit var adapter: ListUserAdapter
    private val listUser = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        adapter = ListUserAdapter()
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel = DetailViewModel()
        detailViewModel.setUserDetail(user.username)
        detailViewModel.getUserDetail().observe(this, {
            binding.apply {
                Glide.with(this@UserDetailActivity)
                    .load(user.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgAvatar)
                txtName.text = user.name
                txtUserName.text = user.username
                txtCompany.text = user.company
                txtLocation.text = user.location
                txtFollowers.text = user.followers
                txtFollowing.text = user.following
                txtRepository.text = user.repository
            }
        })

        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun setData(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
    }

    private fun bind(userItems: User) {
        with(binding) {
            Glide.with(this.root)
                .load(userItems.avatar)
                .apply(RequestOptions().override(55, 55))
                .into(imgAvatar)
            binding.txtName.text = userItems.name
            binding.txtUserName.text = userItems.username
            binding.txtCompany.text = userItems.company
        }
    }
}







