package com.nenad.cinemaquery.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.adapter.MoviesAdapter
import com.nenad.cinemaquery.data.remote.Resource
import com.nenad.cinemaquery.databinding.FragmentIntheatresBinding
import com.nenad.cinemaquery.util.Constants
import com.nenad.cinemaquery.viewmodels.UpcomingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class UpcomingFragment : BaseFragment() {

    lateinit var mBinding: FragmentIntheatresBinding
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var upcomingViewModel: UpcomingViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intheatres, container, false)

        upcomingViewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
        upcomingViewModel.getUpcomingMovies()
        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.VISIBLE
        upcomingMovies()
    }

    fun upcomingMovies() {
        lifecycleScope.launch {
            upcomingViewModel.resultUpcoming.observe(viewLifecycleOwner, Observer { response ->
                when(response) {
                    is Resource.Success<*> -> {
                        hideProgressBar(mBinding.progressbar)
                        response.data.let {
                            moviesAdapter.differ.submitList(it?.results?.toList())
                            val totalPages = it!!.totalResults / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = upcomingViewModel.upcomingPageNum == totalPages
                            if (isLastPage) {
                                mBinding.rvUpcoming.setPadding(0,0,0,0)
                            }
                        }


                    }
                    is Resource.Error<*> -> {
                        response.message.let {
                            Log.e("TAG", "ERROR")

                        }
                    }
                    is Resource.Loading<*> -> {
                        showProgressBar(mBinding.progressbar)
                    }
                }

            })
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                upcomingViewModel.getUpcomingMovies()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    fun setUpRV() {
        moviesAdapter = MoviesAdapter(this)
        mBinding.rvUpcoming.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@UpcomingFragment.scrollListener)

        }
        moviesAdapter.setOnClickListener {
            val action = UpcomingFragmentDirections.actionIntheatresFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }


}