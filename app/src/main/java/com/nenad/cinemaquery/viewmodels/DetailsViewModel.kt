package com.nenad.cinemaquery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nenad.cinemaquery.data.model.Result
import com.nenad.cinemaquery.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel@Inject constructor(val repository: Repository, application: Application): AndroidViewModel(application) {
    fun insertMovie(movie: Result) {
        viewModelScope.launch {
            repository.local.dao.insert(movie)
        }

    }

    val movies: LiveData<List<Result>> = repository.local.dao.getAllMovies().asLiveData()





}