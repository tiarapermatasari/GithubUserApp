package com.dicoding.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FragmentAdapter(): RecyclerView.Adapter<FragmentAdapter.ListViewHolder>() {

    private val listUser = ArrayList<User>()
    private lateinit var onItemClickCallback1: OnItemClickCallback

    fun setData(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val btnFavorite: ImageView = itemView.findViewById(R.id.btn_love)
        val btnShare: ImageView = itemView.findViewById(R.id.btn_share)
        var txtName: TextView = itemView.findViewById(R.id.txt_name)
        var txtUsername: TextView = itemView.findViewById(R.id.txt_user_name)
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_avatar)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userItems = listUser[position]
        Glide.with(holder.itemView.context)
            .load(userItems.avatar)
            .apply(RequestOptions().override(55,55))
            .into(holder.imgAvatar)
        holder.txtName.text = userItems.name
        holder.txtUsername.text = userItems.username

        holder.btnFavorite.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Added to favorite", Toast.LENGTH_SHORT).show()
        }
        holder.btnShare.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Share", Toast.LENGTH_SHORT).show()
        }
    }
    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback1 = onItemClickCallback
    }

}