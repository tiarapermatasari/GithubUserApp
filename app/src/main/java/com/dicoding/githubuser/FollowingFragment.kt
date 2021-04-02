package com.dicoding.githubuser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {
    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): Fragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding : FragmentFollowingBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val argument = arguments?.getString(ARG_USERNAME)
        val view: View = binding.root
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        binding.rvFollowing.setHasFixedSize(true)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.setFollowing(argument)

        showLoading(true)
        showRecyclerView()
        return view
    }

    private fun showRecyclerView(){
        val adapter = FragmentAdapter()
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
        binding.rvFollowing.adapter = adapter

        detailViewModel.getFollowing().observe(viewLifecycleOwner) { userItems ->
            if (userItems != null) {
                showLoading(false)
                adapter.setData(userItems)
            }
        }
        adapter.setOnItemClickCallback(object: FragmentAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(condition: Boolean) {
        if(condition) {
            binding.progressBarFollowingFragment.visibility = View.VISIBLE
        }else {
            binding.progressBarFollowingFragment.visibility = View.GONE
        }
    }

}