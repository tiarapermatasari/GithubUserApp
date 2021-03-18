package com.dicoding.githubuser

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserDetail : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)


        val dataUser= intent.getParcelableExtra<User>(EXTRA_USER) as User

        val txtName: TextView = findViewById(R.id.txt_name)
        val imgAvatar: ImageView = findViewById(R.id.img_avatar)
        val txtCompany: TextView = findViewById(R.id.txt_company)
        val txtLocation: TextView = findViewById(R.id.txt_location)
        val txtUsername: TextView = findViewById(R.id.txt_user_name)
        val txtFollowers: TextView = findViewById(R.id.txt_followers)
        val txtFollowing: TextView = findViewById(R.id.txt_following)
        val txtRepository: TextView = findViewById(R.id.txt_repository)

        txtName.text = dataUser.name
        txtCompany.text = dataUser.company
        txtLocation.text = dataUser.location
        txtUsername.text = dataUser.username
        txtFollowers.text = dataUser.followers
        txtFollowing.text = dataUser.following
        txtRepository.text = dataUser.repository
        imgAvatar.setImageResource(dataUser.avatar)
    }

}