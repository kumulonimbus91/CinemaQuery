package com.nenad.cinemaquery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.adapter.MoviesAdapter
import com.nenad.cinemaquery.data.model.Result
import com.nenad.cinemaquery.databinding.FragmentFavoritesBinding
import com.nenad.cinemaquery.util.SwipeToDeleteCallback
import com.nenad.cinemaquery.viewmodels.DetailsViewModel
import com.nenad.cinemaquery.viewmodels.PopularViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    lateinit var mBinding: FragmentFavoritesBinding
    lateinit var moviesAdapter: MoviesAdapter
    lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        detailsViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]


        setUpRV()
        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.GONE





        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.movies.observe(viewLifecycleOwner, Observer {
            moviesAdapter.differ.submitList(it)

        })
        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.GONE
    }

    fun setUpRV() {
        moviesAdapter = MoviesAdapter(this)
        mBinding.rvSaved.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)

        }
        val swipeToDelete = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition


                moviesAdapter.getMovieAt(position)?.let {
                    detailsViewModel.deleteMovie(it) }







            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)

        itemTouchHelper.attachToRecyclerView(mBinding.rvSaved)

    }


}