package com.bariq.bariqgithub2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bariq.bariqgithub2.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val detailUserMainViewModel: DetailUserMainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
            1 -> {
                arguments?.getString(USERNAME)?.let { detailUserMainViewModel.getFollowers(it) }
                detailUserMainViewModel.followers.observe(requireActivity()) { user ->
                    setListUser(user)
                }
            }
            2 -> {
                arguments?.getString(USERNAME)?.let { detailUserMainViewModel.getFollowing(it) }
                detailUserMainViewModel.following.observe(requireActivity()) { user ->
                    setListUser(user)
                }
            }
        }
    }

    private fun setListUser(user: List<ItemsItem>) {
        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListUserAdapter(user as ArrayList<ItemsItem>)
        binding.rvFollow.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, data.login)
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
    }
}