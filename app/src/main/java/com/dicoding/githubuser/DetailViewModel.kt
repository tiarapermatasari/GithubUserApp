package com.dicoding.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class DetailViewModel : ViewModel() {

    private val userDetail = MutableLiveData<ArrayList<User>>()
    private val followersDetail = MutableLiveData<ArrayList<User>>()
    private val followingDetail = MutableLiveData<ArrayList<User>>()

    fun setUserDetail(username: String?) {
        val detailUser = ArrayList<User>()
        val url = "https://api.github.com/users/$username"
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "d57ba3014f40b4e75a07a12d1d4b1037e5f04de2")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    Log.d("ViewModel", result.toString())
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
                        detailUser.add(user)
                        userDetail.postValue(detailUser)
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
                Log.d("MainViewModel", errorMessage)
            }

        })
    }

    fun getUserDetail(): LiveData<ArrayList<User>> {
        return userDetail
    }

    fun setFollowers(username: String?) {
        val followersUser = ArrayList<User>()
        val asyncClient = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        asyncClient.addHeader("Authorization", "d57ba3014f40b4e75a07a12d1d4b1037e5f04de2")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                    try {
                        val result = String(responseBody)
                        Log.d("ViewModel", result.toString())
                        val response = JSONArray(result)
                        for (i in 0 until response.length()) {
                            val item = response.getJSONObject(i)
                            val user = User()
                                user.name = item.getString("name")
                                user.username = item.getString("login")
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
        asyncClient.addHeader("Authorization", "d57ba3014f40b4e75a07a12d1d4b1037e5f04de2")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    Log.d("ViewModel", result.toString())
                    val response = JSONArray(result)
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val user = User()
                        user.name = item.getString("name")
                        user.username = item.getString("login")
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