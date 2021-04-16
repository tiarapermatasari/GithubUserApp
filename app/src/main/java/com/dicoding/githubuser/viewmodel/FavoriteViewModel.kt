package com.dicoding.githubuser.viewmodel

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.UserFavorite
import com.dicoding.githubuser.db.DatabaseContract
import com.dicoding.githubuser.db.UserHelper

class FavoriteViewModel(private val context: Context) : ViewModel() {
    private var fav: UserFavorite? = null
    private lateinit var favHelper: UserHelper

    fun setFavorite(username: String, avatar: String) {
        favHelper = UserHelper.getInstance(context)
        favHelper.open()
        val values = ContentValues()
        fav
        values.put(DatabaseContract.UserColumns.USERNAME, username)
        values.put(DatabaseContract.UserColumns.AVATAR, avatar)
        val result = favHelper.insert(values)
        fav?.id = result.toInt()
    }

    fun deleteFavorite(id: String) {
        favHelper = UserHelper.getInstance(context)
        favHelper.open()
        favHelper.deleteById(id)
    }

    fun showRecyclerFavorite() {
        favHelper = UserHelper.getInstance(context)
        favHelper.open()
    }

    fun closeTheDatabase() {
        favHelper = UserHelper.getInstance(context)
        favHelper.open()
        favHelper.close()
    }
}