package com.dicoding.githubuser.viewmodel

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.User
import com.dicoding.githubuser.db.DatabaseContract
import com.dicoding.githubuser.db.UserHelper
import com.dicoding.githubuser.db.UserHelper.Companion.getInstance
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class DetailViewModel : ViewModel() {

    private val userDetail = MutableLiveData<User>()
    private val followersDetail = MutableLiveData<ArrayList<User>>()
    private val followingDetail = MutableLiveData<ArrayList<User>>()
    private lateinit var repository: UserHelper

    fun setUserDetail(username: String?) {
        val url = "https://api.github.com/users/$username"
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = String(responseBody)
                try {
                    Log.d("ViewModel", result)
                    val response = JSONObject(result)
                    val user = User()
                        user.name = response.getString("name")
                        user.username = response.getString("login")
                        user.avatar = response.getString("avatar_url")
                        user.company = response.getString("company")
                        user.location = response.getString("location")
                        user.followers = response.getString("followers")
                        user.following = response.getString("following")
                        user.repository = response.getString("public_repos")
                        userDetail.postValue(user)
                } catch (e: Exception) {
                    Log.d("Exception", e.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("DetailViewModel", errorMessage)
            }

        })
    }

    fun getUserDetail(): LiveData<User> {
        return userDetail
    }

    fun setFollowers(username: String?) {
        val followersUser = ArrayList<User>()
        val asyncClient = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                    try {
                        val result = String(responseBody)
                        Log.d("ViewModel", result)
                        val response = JSONArray(result)
                        for (i in 0 until response.length()) {
                            val item = response.getJSONObject(i)
                            val user = User()
                                user.name = item.getString("login")
                                user.avatar = item.getString("avatar_url")
                                followersUser.add(user)
                            }
                        followersDetail.postValue(followersUser)
                    } catch (e: Exception) {
                        Log.d("Exception", e.toString())
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    Log.d("onFailure", error!!.message.toString())
                }
            })
    }
    fun getFollowers(): LiveData<ArrayList<User>> {
        return followersDetail
    }

    fun setFollowing(username: String?) {
        val followingUser = ArrayList<User>()
        val asyncClient = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    Log.d("ViewModel", result)
                    val response = JSONArray(result)
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val user = User()
                        user.name = item.getString("login")
                        user.avatar = item.getString("avatar_url")
                        followingUser.add(user)
                    }
                    followingDetail.postValue(followingUser)
                } catch (e: Exception) {
                    Log.d("Exception", e.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("Data error", error!!.message.toString())
            }
        })
    }

    fun getFollowing(): LiveData<ArrayList<User>> {
        return followingDetail
    }


}