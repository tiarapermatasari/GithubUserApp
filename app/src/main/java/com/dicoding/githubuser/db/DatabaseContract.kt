package com.dicoding.githubuser.db

import android.net.Uri
import android.provider.BaseColumns


internal class DatabaseContract {

    companion object {
        const val AUTHORITY = "com.dicoding.githubuser"
        const val SCHEME = "content"
    }


    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val NAME = "name"
            const val REPOSITORY = "repository"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val FAVORITE = "isFav"


            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}
