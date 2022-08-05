package com.nenad.cinemaquery.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nenad.cinemaquery.data.model.Movies
import com.nenad.cinemaquery.util.Constants
import com.nenad.cinemaquery.util.Constants.API_KEY
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.net.URLEncoder

interface ApiServiceInt {

    @GET("/3/movie/popular/")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String = API_KEY, @Query("page")
    pageNumber: Int): Response<Movies>

    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String = API_KEY,@Query("page")
    pageNumber: Int): Response<Movies>

    @GET("/3/search/movie")
    suspend fun searchMovie(@Query("api_key") apiKey: String = API_KEY, @Query("query") query: String, @Query("page")
    pageNumber: Int): Response<Movies>

    @GET("/3/discover/movie")
    suspend fun searchMovieByDate(@Query("api_key") apiKey: String = API_KEY, @Query("release_date.gte") releaseDate: String): Response<Movies>


}





