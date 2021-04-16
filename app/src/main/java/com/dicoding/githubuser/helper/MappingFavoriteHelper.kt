package com.dicoding.githubuser.helper

import android.database.Cursor
import com.dicoding.githubuser.data.UserFavorite
import com.dicoding.githubuser.db.DatabaseContract


object MappingFavoriteHelper {
    fun mapCursorToArrayList(favoritCursor: Cursor?): ArrayList<UserFavorite> {
        val favlist = ArrayList<UserFavorite>()

        favoritCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                favlist.add(UserFavorite(id, username, avatar))
            }
        }
        return favlist
    }

    fun mapCursorToObject(favoritCursor: Cursor?): UserFavorite {
        var userFavorit = UserFavorite()
        favoritCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
            userFavorit = UserFavorite(id, username, avatar)
        }
        return userFavorit
    }

}