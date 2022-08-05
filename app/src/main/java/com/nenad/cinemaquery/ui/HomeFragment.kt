package com.nenad.cinemaquery.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.adapter.MoviesAdapter
import com.nenad.cinemaquery.databinding.FragmentHomeBinding
import com.nenad.cinemaquery.util.Constants
import com.nenad.cinemaquery.util.Constants.QUERY_PAGE_SIZE
import com.nenad.cinemaquery.viewmodel.ViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    val viewModel: ViewModel by activityViewModels()
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()
        popularMovies()
        clickListeners()

        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.VISIBLE


    }
    fun popularMovies() {
        lifecycleScope.launch {
            viewModel.resultPopular.observe(viewLifecycleOwner, Observer { response ->
                when(response) {
                    is com.nenad.cinemaquery.data.remote.Resource.Success<*> -> {
                        hideProgressBar()
                        response.data.let {
                            moviesAdapter.differ.submitList(it?.results?.toList())
                            val totalPages = it!!.totalResults / QUERY_PAGE_SIZE + 2 //the last page will always be empty
                            isLastPage = viewModel.popularPageNum == totalPages
                            if (isLastPage) {
                                mBinding.rvPopular.setPadding(0,0,0,0)
                            }
                        }


                    }
                    is com.nenad.cinemaquery.data.remote.Resource.Error -> {
                        response.message.let {
                            Log.e("TAG", "ERROR")

                        }
                    }
                    is com.nenad.cinemaquery.data.remote.Resource.Loading -> {

                        showProgressBar()
                    }
                }

            })
        }
    }

    fun setUpRV() {
        moviesAdapter = MoviesAdapter(this)
        mBinding.rvPopular.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListener)

        }

    }
    fun clickListeners() {
        moviesAdapter.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            findNavController().navigate(action)
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
                viewModel.getPopularMovies()
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
    fun showProgressBar() {
        mBinding.progressbar.visibility = View.VISIBLE
        isLoading = true

    }

    fun hideProgressBar() {
        mBinding.progressbar.visibility = View.INVISIBLE
        isLoading = false

    }



}