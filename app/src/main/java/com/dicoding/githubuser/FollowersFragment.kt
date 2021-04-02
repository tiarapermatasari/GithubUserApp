package com.dicoding.githubuser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentFollowersBinding


class FollowersFragment : Fragment() {

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): Fragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding : FragmentFollowersBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val argument = arguments?.getString(ARG_USERNAME)
        val view: View? = binding.root
        binding = FragmentFollowersBinding.inflate(layoutInflater)


        binding.rvFollowers.setHasFixedSize(true)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.setFollowers(argument)

        showLoading(true)
        showRecyclerView()
        return view
    }

    private fun showRecyclerView(){
        val adapter = FragmentAdapter()
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        adapter.notifyDataSetChanged()
        binding.rvFollowers.adapter = adapter

        detailViewModel.getFollowers().observe(viewLifecycleOwner) { userItems ->
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
            binding.progressBarFollowersFragment.visibility = View.VISIBLE
        }else {
            binding.progressBarFollowersFragment.visibility = View.GONE
        }
    }

}


