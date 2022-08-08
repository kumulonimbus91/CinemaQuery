package com.nenad.cinemaquery.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
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
class PopularViewModel @Inject constructor(val repository: Repository, application: Application): AndroidViewModel(application) {

    val resultPopular = MutableLiveData<Resource<Movies>>()
    var popularResponse: Movies? = null
    var popularPageNum = 1

    init {
        getPopularMovies()
    }

    fun getPopularMovies() = viewModelScope.launch {
        safePopularMoviesCall()
    }

    private suspend fun safePopularMoviesCall() {
        resultPopular.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getPopularMovies(popularPageNum)
                resultPopular.postValue(handlePopularResponse(response))
            } else {
                resultPopular.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> resultPopular.postValue(Resource.Error("Network Failure"))
                else -> resultPopular.postValue(Resource.Error("Conversion Error"))
            }
        }

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