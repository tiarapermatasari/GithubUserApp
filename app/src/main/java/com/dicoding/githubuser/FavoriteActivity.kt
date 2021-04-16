package com.dicoding.githubuser

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.helper.MappingFavoriteHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.*
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.data.UserFavorite
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.viewmodel.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var viewModel: FavoriteViewModel

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FavoriteAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = adapter
        }

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnDeleteListener { favorite, position ->
            viewModel.deleteFavorite(favorite.id.toString())
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        binding.rvFavorite.adapter = adapter
        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(userFavorite: UserFavorite) {
                Toast.makeText(baseContext, "anda memilih ${userFavorite.username}", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.showRecyclerFavorite()
            val deferredFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingFavoriteHelper.mapCursorToArrayList(cursor)
            }
            viewModel.closeTheDatabase()
            val favoriteDef = deferredFav.await()
            if (favoriteDef.size > 0) {
                adapter.listFavorite = favoriteDef
            } else {
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

}

