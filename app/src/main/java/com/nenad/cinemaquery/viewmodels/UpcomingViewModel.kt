package com.nenad.cinemaquery.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nenad.cinemaquery.MyApplication
import com.nenad.cinemaquery.data.model.Movies
import com.nenad.cinemaquery.data.remote.Resource
import com.nenad.cinemaquery.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UpcomingViewModel @Inject constructor(val repository: Repository, application: Application): AndroidViewModel(application) {
    val resultUpcoming = MutableLiveData<Resource<Movies>>()
    var upcomingResponse: Movies? = null
    var upcomingPageNum = 1

    fun getUpcomingMovies() = viewModelScope.launch {
        safeUpcomingMoviesCall()

    }

    private suspend fun safeUpcomingMoviesCall() {
        resultUpcoming.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getUpcomingMovies(upcomingPageNum)
                resultUpcoming.postValue(handleUpcomingResponse(response))
            } else {
                resultUpcoming.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> resultUpcoming.postValue(Resource.Error("Network Failure"))
                else -> resultUpcoming.postValue(Resource.Error("Conversion Error"))
            }
        }

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

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }




}