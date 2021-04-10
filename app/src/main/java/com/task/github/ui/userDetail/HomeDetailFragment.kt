package com.task.github.ui.userDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.task.github.R
import com.task.github.data.RepoUser
import com.task.github.data.retrofit.RetrofitBuilder
import com.task.github.data.room.DatabaseBuilder
import com.task.github.data.room.DatabaseHelper
import com.task.github.support.ViewModelFactory
import com.task.github.support.imageLoader.ImageLoader
import kotlinx.android.synthetic.main.fragment_user_details.*

class HomeDetailFragment : Fragment() {

    companion object {
        const val USER_DETAILS = "user_details"
    }

    private lateinit var imageLoader: ImageLoader
    private lateinit var viewModel: HomeDetailViewModel
    private lateinit var adapter: CommentAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        arguments?.getSerializable(USER_DETAILS)?.let {
            if (it is RepoUser) {
                viewModel.user = it
            }
        }
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageLoader = ImageLoader(requireContext())
        setUserData()
        fetchCommentFromLocal()

    }

    private fun setUserData() {

        viewModel.user?.let {
            imageLoader.displayImage(it.owner.avatarUrl, ivUserImageDetails)
            tvUserNameDetail.text = getString(R.string.name, it.name)
            tvFullName.text = getString(R.string.full_name, it.name)
            tvOwnerDetails.text = getString(
                R.string.owner_details,
                it.owner.login,
                it.owner.url,
                it.owner.htmlUrl,
                it.owner.followersUrl,
                it.owner.type,
                it.createdAt,
                it.pushedAt
            )

        }
    }

    private fun fetchCommentFromLocal() {
        viewModel.fetchComment()
        viewModel.getCommentLiveData().observe(viewLifecycleOwner) {
            adapter = CommentAdapter(it)
            rvComment.adapter = adapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                RetrofitBuilder.apiService,
                DatabaseHelper(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(HomeDetailViewModel::class.java)
    }

}