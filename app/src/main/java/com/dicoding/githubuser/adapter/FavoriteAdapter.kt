package com.dicoding.githubuser.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.data.UserFavorite
import com.dicoding.githubuser.databinding.ItemUserFavoriteBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    private var onItemClickCallback:OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    var listFavorite = ArrayList<UserFavorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    private lateinit var deleteListener: ((UserFavorite, Int) -> Unit)

    fun setOnDeleteListener(listener: (UserFavorite, Int) -> Unit) {
        deleteListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])

        Log.e("Adapter", "$listFavorite")
    }

    override fun getItemCount(): Int {
        return this.listFavorite.size
    }

    inner class ListViewHolder(private val binding : ItemUserFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: UserFavorite) {
            Glide.with(itemView.context)
                .load(favorite.avatar)
                .apply(RequestOptions().override(200, 200))
                .into(binding.imgAvatar)
            binding.txtName

            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(favorite)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(userFavourite: UserFavorite)
    }
}

