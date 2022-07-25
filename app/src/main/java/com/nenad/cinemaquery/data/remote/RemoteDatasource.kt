package com.nenad.cinemaquery.data.remote

import androidx.lifecycle.MutableLiveData
import com.nenad.cinemaquery.data.model.Movies
import com.nenad.cinemaquery.data.model.Result
import com.nenad.cinemaquery.util.Constants
import retrofit2.Response
import javax.inject.Inject

class RemoteDatasource @Inject constructor() {


    suspend fun getPopularMovies(pageNumber: Int): Response<Movies> {
        return RetrofitInstance.api.getPopularMovies()
    }
    suspend fun getUpcomingMovies(pageNumber: Int): Response<Movies> {
        return RetrofitInstance.api.getUpcomingMovies()
    }
    suspend fun searchMovie(query: String): Response<Movies> {
        return RetrofitInstance.api.searchMovie(Constants.API_KEY, query)
    }

    suspend fun searchMovieByDate(releaseDate: String): Response<Movies> {
        return RetrofitInstance.api.searchMovieByDate(Constants.API_KEY,releaseDate)
    }





}