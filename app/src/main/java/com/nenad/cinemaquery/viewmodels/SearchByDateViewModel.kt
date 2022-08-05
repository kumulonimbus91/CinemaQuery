package com.nenad.cinemaquery.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nenad.cinemaquery.data.model.Movies
import com.nenad.cinemaquery.data.remote.Resource
import com.nenad.cinemaquery.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchByDateViewModel @Inject constructor(val repository: Repository, application: Application): AndroidViewModel(application) {
    val dateSearch = MutableLiveData<Resource<Movies>>()
    var searchByDateResponse: Movies? = null
    var searchByDatePageNum = 1



    fun searchMovieByDate(releaseDate: String) = viewModelScope.launch {
        dateSearch.postValue(Resource.Loading())
        val response = repository.remote.searchMovieByDate(releaseDate)
        dateSearch.postValue(handleSearchResponseByDate(response))
    }

    private fun handleSearchResponseByDate(response: Response<Movies>) : Resource<Movies> {
        if (response.isSuccessful) {
            searchByDatePageNum++
            response.body().let { resultResponse ->
                if(searchByDateResponse == null) {
                    searchByDateResponse = resultResponse
                } else {
                    val oldMovies = searchByDateResponse!!.results
                    val newMovies = resultResponse?.results
                    if (newMovies != null) {
                        oldMovies.addAll(newMovies)
                    }

                }




                return Resource.Success(searchByDateResponse ?: resultResponse!!)
            }
        }
        return Resource.Error(response.message())


    }




}