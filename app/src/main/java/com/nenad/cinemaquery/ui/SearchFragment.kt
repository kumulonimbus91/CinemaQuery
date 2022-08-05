package com.nenad.cinemaquery.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nenad.cinemaquery.R
import com.nenad.cinemaquery.adapter.MoviesAdapter
import com.nenad.cinemaquery.databinding.FragmentSearchBinding
import com.nenad.cinemaquery.util.Constants.SEARCH_MOVIES_TIME_DELAY
import com.nenad.cinemaquery.viewmodel.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class SearchFragment : BaseFragment() {
    lateinit var mBinding: FragmentSearchBinding
    lateinit var moviesAdapter: MoviesAdapter
    val viewModel: ViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        setUpRV()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var job: Job? = null
        mBinding.searchEdittext.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_MOVIES_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchMovie(editable.toString())
                    }
                }
            }
        }
        mBinding.btnSearch.setOnClickListener {

            viewModel.resultSearch.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    when (response) {
                        is com.nenad.cinemaquery.data.remote.Resource.Success<*> -> {
                            hideProgressBar(mBinding.progressbar)
                            response.data.let {
                                moviesAdapter.differ.submitList(it?.results)

                            }
                        }
                        is com.nenad.cinemaquery.data.remote.Resource.Error -> {
                            response.message.let {
                                Log.e("TAG", "ERROR")
                            }
                        }
                        is com.nenad.cinemaquery.data.remote.Resource.Loading -> {
                            showProgressBar(mBinding.progressbar)
                        }
                    }
                })
        }







        requireActivity().findViewById<ViewGroup>(R.id.cl_view).visibility = View.GONE

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofmonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayofmonth)

            viewModel.searchMovieByDate(formatDate(myCalendar))
            viewModel.dateSearch.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    when (response) {
                        is com.nenad.cinemaquery.data.remote.Resource.Success<*> -> {
                            hideProgressBar(mBinding.progressbar)
                            response.data.let {
                                moviesAdapter.differ.submitList(it?.results)
                            }
                        }
                        is com.nenad.cinemaquery.data.remote.Resource.Error -> {
                            response.message.let {
                                Log.e("TAG", "ERROR")
                            }
                        }
                        is com.nenad.cinemaquery.data.remote.Resource.Loading -> {
                            showProgressBar(mBinding.progressbar)
                        }
                    }
                })
        }


        mBinding.fabBtn.setOnClickListener {
            DatePickerDialog(
                requireActivity(), datePicker, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()


        }


    }
    fun setUpRV() {
        moviesAdapter = MoviesAdapter(this)
        mBinding.rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(activity)
            //addOnScrollListener(this@SearchFragment.scrollListener)

        }
        moviesAdapter.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun formatDate(myCalendar: Calendar): String {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(myCalendar.time)
    }
}