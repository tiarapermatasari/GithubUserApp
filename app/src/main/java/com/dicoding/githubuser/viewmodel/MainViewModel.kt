package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class MainViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<User>>()

    fun setListUser(username: String?) {

        val listUser = ArrayList<User>()
        val asyncClient = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")
        asyncClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    Log.d("ViewModel", result)
                    val response = JSONObject(result)
                    val items = response.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val user = User()
                        user.name = item.getString("login")
                        user.avatar = item.getString("avatar_url")
                        listUser.add(user)
                    }
                    listUsers.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getListUser(): LiveData<ArrayList<User>> {
        return listUsers
    }
}