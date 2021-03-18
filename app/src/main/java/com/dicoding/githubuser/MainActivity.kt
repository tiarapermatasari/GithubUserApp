package com.dicoding.githubuser

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var dataAvatar: TypedArray
    private lateinit var dataName: Array<String>
    private lateinit var dataCompany: Array<String>
    private lateinit var dataUsername: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataRepository: Array<String>


    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.lv_list)
        adapter = UserAdapter(this)
        listView.adapter = adapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val moveIntent = Intent(this@MainActivity, UserDetail ::class.java )
            moveIntent.putExtra(UserDetail.EXTRA_USER, users)
            startActivity(moveIntent)
        }
    }
    private fun prepare() {
        dataName = resources.getStringArray(R.array.name)
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
        dataCompany = resources.getStringArray(R.array.company)
        dataUsername = resources.getStringArray(R.array.username)
        dataLocation = resources.getStringArray(R.array.location)
        dataFollowers = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)
        dataRepository = resources.getStringArray(R.array.repository)
    }

    private fun addItem() {
        for (position in dataName.indices) {
            val user = User(
                    dataAvatar.getResourceId(position, -1),
                    dataName[position],
                    dataCompany[position],
                    dataUsername[position],
                    dataLocation[position],
                    dataFollowers[position],
                    dataFollowing[position],
                    dataRepository[position]
            )
            users.add(user)
        }
        adapter.user = users
    }
}