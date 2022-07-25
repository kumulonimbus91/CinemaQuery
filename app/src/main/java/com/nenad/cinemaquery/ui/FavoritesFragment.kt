package com.nenad.cinemaquery.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.adapter.MoviesAdapter
import com.nenad.cinemaquery.databinding.FragmentFavoritesBinding
import com.nenad.cinemaquery.viewmodel.ViewModel
class FavoritesFragment : Fragment() {
    lateinit var mBinding: FragmentFavoritesBinding
    lateinit var moviesAdapter: MoviesAdapter
    val viewModel: ViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        setUpRV()
        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.GONE

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movies.observe(viewLifecycleOwner, Observer {
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

    }


}