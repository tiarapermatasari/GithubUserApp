package com.dicoding.githubuser

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.adapter.SectionPagerAdapter
import com.dicoding.githubuser.data.User
import com.dicoding.githubuser.data.UserFavorite
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.COMPANY
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.FAVORITE
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.LOCATION
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.NAME
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.REPOSITORY
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.dicoding.githubuser.db.UserHelper
import com.dicoding.githubuser.helper.MappingFavoriteHelper
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_row_user.*

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.fragment_1,
            R.string.fragment_2
        )
        const val EXTRA_USER = "extra_user"
        const val FAV_DATA = "fav_data"
        const val POSITION_DATA = "position_data"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userHelper: UserHelper
    private lateinit var uriWithId: Uri
    private lateinit var imgAvatar: String
    private var position: Int = 0
    private var user: UserFavorite? = null
    private var statusFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnShare.setOnClickListener(this)
        detailViewModel = DetailViewModel()
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        setData()

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        checkDatabaseFav()

        setStatusFavorite(statusFavorite)
        binding.btnLove.setOnClickListener {
            statusFavorite = !statusFavorite
            btnFav()

        }
    }

    private fun setData() {
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        detailViewModel.setUserDetail(user.name)
        detailViewModel.getUserDetail().observe(this, {
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

    private fun checkDatabaseFav() {
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        val myFavorites = MappingFavoriteHelper.mapCursorToArrayList(cursor)
        for (data in myFavorites) {
            if (user?.id == data.id) {
                statusFavorite = true
                setFavData()
            }
        }
    }

    private fun setFavData() {
        user = intent.getParcelableExtra(FAV_DATA)

        if (user != null) {
            position = intent.getIntExtra(POSITION_DATA, 0)

            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user?.username)
            val cursor = contentResolver.query(uriWithId, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                user = MappingFavoriteHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        } else {
            user = UserFavorite()
        }
    }

    private fun btnFav() {
        if (statusFavorite == true) {
            userHelper.deleteById(user?.username.toString())
            statusFavorite = false
        } else {
            val avatar = imgAvatar
            val username = binding.txtUserName.text.toString()
            val name = binding.txtName.text.toString()
            val repository = binding.txtRepository.text.toString()
            val company = binding.txtCompany.text.toString()
            val location = binding.txtLocation.text.toString()
            val fav = "1"

            val values = ContentValues()
            values.put(AVATAR, avatar)
            values.put(USERNAME, username)
            values.put(NAME, name)
            values.put(REPOSITORY, repository)
            values.put(COMPANY, company)
            values.put(LOCATION, location)
            values.put(FAVORITE, fav)

            statusFavorite = true
            contentResolver.insert(CONTENT_URI, values)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.btnLove.setImageResource(R.drawable.ic_nofavorite)
            Toast.makeText(this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show()}
        else {
            binding.btnLove.setImageResource(R.drawable.ic_favorite)
            Toast.makeText(this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show()}
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
            }
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








