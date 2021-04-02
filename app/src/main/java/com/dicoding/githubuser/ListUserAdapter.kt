package com.dicoding.githubuser

import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.databinding.ItemRowUserBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private var listUser = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root){

        val btnFavorite: ImageView = itemView.findViewById(R.id.btn_love)
        val btnShare: ImageView = itemView.findViewById(R.id.btn_share)
        fun bind(userItems: User) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(55,55))
                    .into(imgAvatar)
                binding.txtName.text = userItems.name
                binding.txtUserName.text = userItems.username
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ListViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.btnFavorite.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Added to favorite", Toast.LENGTH_SHORT).show()
        }
        holder.btnShare.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Share", Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnClickListener{ onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}
