package com.nenad.cinemaquery.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.adapter.MoviesAdapter
import com.nenad.cinemaquery.databinding.FragmentDetailsBinding
import com.nenad.cinemaquery.util.Constants.imgPath
import com.nenad.cinemaquery.viewmodel.ViewModel


class DetailsFragment : Fragment() {
    lateinit var mBinding: FragmentDetailsBinding
    val viewModel: ViewModel by activityViewModels()
    val args: DetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)


        clickListener()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.GONE
        setUpUI()
    }



    fun setUpUI() {
        Glide.with(this).load(imgPath + args.movie.poster_path).into(mBinding.img)
        mBinding.tvTitle.text = args.movie.title
        mBinding.tvOverview.text = args.movie.overview
        mBinding.rate.text = args.movie.vote_average.toString()
        mBinding.date.text = args.movie.release_date
    }

    fun clickListener() {
        mBinding.favBtn.setOnClickListener {
         viewModel.insertMovie(args.movie)
            Toast.makeText(requireActivity(), "${args.movie.title} is added to Watch List", Toast.LENGTH_SHORT).show()
        }
    }



}