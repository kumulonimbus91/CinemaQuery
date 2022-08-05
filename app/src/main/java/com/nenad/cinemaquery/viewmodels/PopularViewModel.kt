package com.nenad.cinemaquery.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.nenad.cinemaquery.data.model.Movies
import com.nenad.cinemaquery.data.remote.Resource
import com.nenad.cinemaquery.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(val repository: Repository, application: Application): AndroidViewModel(application) {
    val resultPopular = MutableLiveData<Resource<Movies>>()
    var popularResponse: Movies? = null
    var popularPageNum = 1

    init {
        getPopularMovies()
    }

    fun getPopularMovies() = viewModelScope.launch {
        resultPopular.postValue(Resource.Loading())
        val response = repository.remote.getPopularMovies(popularPageNum)
        resultPopular.postValue(handlePopularResponse(response))
    }

    private fun handlePopularResponse(response: Response<Movies>) :
            Resource<Movies> {
        if (response.isSuccessful) {
            popularPageNum++// 
            response.body().let { resultResponse ->
                if (popularResponse == null) {
                    popularResponse = resultResponse
                } else {
                    val oldMovies = popularResponse!!.results
                    val newMovies = resultResponse?.results
                    if (newMovies != null) {
                        oldMovies.addAll(newMovies)
                    }
                }
                return Resource.Success(popularResponse ?: resultResponse!!) //if its first response return resultresponse instead
            }
        }
        return Resource.Error(response.message())


    }











}