package com.dicoding.githubuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentFollowersBinding


class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var adapter: FragmentAdapter
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        adapter = FragmentAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getString(ARG_USERNAME)

        showLoading(true)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        if (user != null) {
            detailViewModel.setFollowers(user)
        }

        detailViewModel.getFollowers().observe(viewLifecycleOwner) {
            adapter.setData(it)
            showLoading(false)

        }
        adapter.setOnItemClickCallback(object : FragmentAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    companion object {
        private const val ARG_USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String) =
            FollowersFragment().apply {
                FollowersFragment().apply {
                    val fragment = FollowingFragment()
                    val bundle = Bundle()
                    bundle.putString(ARG_USERNAME, username)
                    fragment.arguments = bundle
                }
                }
            }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollowersFragment.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowersFragment.visibility = View.GONE
        }
    }
}




