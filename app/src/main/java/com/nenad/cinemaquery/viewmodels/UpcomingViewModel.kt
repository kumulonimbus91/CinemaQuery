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
class UpcomingViewModel @Inject constructor(val repository: Repository, application: Application): AndroidViewModel(application) {
    val resultUpcoming = MutableLiveData<Resource<Movies>>()
    var upcomingResponse: Movies? = null
    var upcomingPageNum = 1

    fun getUpcomingMovies() = viewModelScope.launch {
        resultUpcoming.postValue(com.nenad.cinemaquery.data.remote.Resource.Loading())
        val response = repository.remote.getUpcomingMovies(upcomingPageNum)
        resultUpcoming.postValue(handleUpcomingResponse(response))

    }


    private fun handleUpcomingResponse(response: Response<Movies>) : Resource<Movies> {
        if (response.isSuccessful) {
            upcomingPageNum++ //load next page
            response.body().let { resultResponse ->
                if(upcomingResponse == null) {
                    upcomingResponse = resultResponse
                } else {
                    val oldMovies = upcomingResponse!!.results
                    val newMovies = resultResponse?.results
                    if (newMovies != null) {
                        oldMovies.addAll(newMovies)
                    }

                }



                return Resource.Success(upcomingResponse ?: resultResponse!!) //if upcoming is null return resultResponse
            }
        }
        return Resource.Error(response.message())


    }




}