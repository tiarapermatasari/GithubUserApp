package com.dicoding.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {

    internal var user = arrayListOf<User>()
    override fun getCount(): Int = user.size
    override fun getItem(i: Int): Any = user[i]
    override fun getItemId(i: Int): Long = i.toLong()
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {

        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false)
        }
        val viewHolder = ViewHolder(itemView as View)
        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    private inner class ViewHolder internal constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.txt_name)
        private val imgAvatar: ImageView = view.findViewById(R.id.img_avatar)
        private val txtCompany: TextView = view.findViewById(R.id.txt_company)
        private val txtLocation: TextView = view.findViewById(R.id.txt_location)
        private val txtUsername: TextView = view.findViewById(R.id.txt_user_name)
        private val txtFollowing: TextView = view.findViewById(R.id.txt_following)
        private val txtFollowers: TextView = view.findViewById(R.id.txt_followers)
        private val txtRepository: TextView = view.findViewById(R.id.txt_repository)

        internal fun bind(user: User) {
            txtName.text = user.name
            txtCompany.text = user.company
            txtLocation.text = user.location
            txtUsername.text = user.username
            txtFollowing.text = user.following
            txtFollowers.text = user.followers
            txtRepository.text = user.repository
            imgAvatar.setImageResource(user.avatar)
        }
    }
}
