package com.task.github.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.github.R
import com.task.github.data.ApiResult
import com.task.github.data.RepoUser
import com.task.github.data.retrofit.RetrofitBuilder
import com.task.github.data.room.DatabaseBuilder
import com.task.github.data.room.DatabaseHelper
import com.task.github.support.PaginationListener
import com.task.github.support.RepoItemClickListener
import com.task.github.support.ViewModelFactory
import com.task.github.ui.userDetail.HomeDetailFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(), RepoItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: HomeUserAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        initViewModel()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeUserAdapter(this)
        view.rvRepoUser.adapter = adapter
        fetchUser()
        view.rvRepoUser.addOnScrollListener(paginationListener)

    }

    private fun fetchUser() {
        homeViewModel.fetchRepoUser()
        homeViewModel.getUsers().observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Success -> {
                    progressBarHome.isVisible = false
                    adapter.addRepo(it.data)

                }
                is ApiResult.Error -> {

                }

            }
        }

    }

    private var paginationListener: PaginationListener = object : PaginationListener() {

        override val isLoading: Boolean
            get() = homeViewModel.isLoading

        override fun loadMoreItems() {
            homeViewModel.loadNewPage()
            progressBarHome.isVisible = true

        }


        override fun getLayoutManager(): LinearLayoutManager {
            return rvRepoUser.layoutManager as LinearLayoutManager
        }
    }

    override fun onItemClick(pos: Int, item: RepoUser) {
        val argument = Bundle()
        argument.putSerializable(HomeDetailFragment.USER_DETAILS, item)
        findNavController().navigate(R.id.action_title_to_details, argument)
    }

    override fun onAddComment(comment: String, pos: Int, data: RepoUser) {
        homeViewModel.addComment(comment, data.id)
        showToast(getString(R.string.comment_success))

    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
                this,
                ViewModelFactory(
                        RetrofitBuilder.apiService,
                        DatabaseHelper(DatabaseBuilder.getInstance(requireContext()))
                )
        ).get(HomeViewModel::class.java)
    }

    private fun showToast(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }
}