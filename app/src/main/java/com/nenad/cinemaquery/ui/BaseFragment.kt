package com.nenad.cinemaquery.ui

import android.view.View
import android.widget.AbsListView
import android.widget.Adapter
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nenad.cinemaquery.adapter.MoviesAdapter
import com.nenad.cinemaquery.util.Constants
import com.nenad.cinemaquery.viewmodel.ViewModel

open class BaseFragment: Fragment() {

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }




}