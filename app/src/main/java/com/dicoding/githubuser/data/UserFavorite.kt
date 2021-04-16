package com.dicoding.githubuser.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class UserFavorite(
    @PrimaryKey
    var id: Int = 0,

    var name: String = "",
    var avatar: String? = null,
    var username: String? = null,
    var repository: String? = null,
    var company: String? = null,
    var location: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var statusFavorite: Boolean = false,
) : Parcelable