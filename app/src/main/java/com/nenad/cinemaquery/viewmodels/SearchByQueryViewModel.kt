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
class SearchByQueryViewModel @Inject constructor(val repository: Repository, application: Application): AndroidViewModel(application) {

    val resultSearch = MutableLiveData<Resource<Movies>>()
    var searchResponse: Movies? = null
    var searchPageNum = 1
    val dateSearch = MutableLiveData<Resource<Movies>>()


    fun searchMovie(query: String) = viewModelScope.launch {
        resultSearch.postValue(Resource.Loading())
        val response = repository.remote.searchMovie(query, searchPageNum)
        resultSearch.postValue(handleSearchResponse(response))
    }


    private fun handleSearchResponse(response: Response<Movies>) : Resource<Movies> {
        if (response.isSuccessful) {
            searchPageNum++
            response.body().let { resultResponse ->
                if(searchResponse == null) {
                    searchResponse = resultResponse
                } else {
                    val oldMovies = searchResponse!!.results
                    val newMovies = resultResponse?.results
                    if (newMovies != null) {
                        oldMovies.addAll(newMovies)
                    }

                }




                return Resource.Success(searchResponse ?: resultResponse!!)
            }
        }
        return Resource.Error(response.message())


    }





}