package com.nenad.cinemaquery.viewmodel

import android.app.Application
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.*
import com.nenad.cinemaquery.data.model.Movies
import com.nenad.cinemaquery.data.model.Result
import com.nenad.cinemaquery.data.remote.Resource
import com.nenad.cinemaquery.data.repository.Repository
import com.nenad.cinemaquery.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor( val repository: Repository, application: Application): AndroidViewModel(application) {


    var query: MutableLiveData<String> = MutableLiveData()
    val resultPopular = MutableLiveData<Resource<Movies>>()
    val resultUpcoming = MutableLiveData<Resource<Movies>>()
    val resultSearch = MutableLiveData<Resource<Movies>>()
    val dateSearch = MutableLiveData<Resource<Movies>>()

    var popularResponse: Movies? = null
    var upcomingResponse: Movies? = null
    var searchResponse: Movies? = null
    var searchByDateResponse: Movies? = null

    var upcomingPageNum = 1
    var popularPageNum = 1
    var searchPageNum = 1
    var searchByDatePageNum = 1





    //remote
    init {
        getPopularMovies()
    }

    fun getPopularMovies() = viewModelScope.launch {
        resultPopular.postValue(com.nenad.cinemaquery.data.remote.Resource.Loading())
        val response = repository.remote.getPopularMovies(popularPageNum)
        resultPopular.postValue(handlePopularResponse(response))
    }

    fun getUpcomingMovies() = viewModelScope.launch {
        resultUpcoming.postValue(com.nenad.cinemaquery.data.remote.Resource.Loading())
        val response = repository.remote.getUpcomingMovies(upcomingPageNum)
        resultUpcoming.postValue(handleUpcomingResponse(response))

    }

    fun searchMovie(query: String) = viewModelScope.launch {
        resultSearch.postValue(Resource.Loading())
        val response = repository.remote.searchMovie(query)
        resultSearch.postValue(handleSearchResponse(response))
    }
    fun searchMovieByDate(releaseDate: String) = viewModelScope.launch {
        dateSearch.postValue(Resource.Loading())
        val response = repository.remote.searchMovieByDate(releaseDate)
        dateSearch.postValue(handleSearchResponseByDate(response))
    }

    private fun handlePopularResponse(response: Response<Movies>) :
            Resource<Movies> {
        if (response.isSuccessful) {
            popularPageNum++
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


    private fun handleUpcomingResponse(response: retrofit2.Response <Movies>) : com.nenad.cinemaquery.data.remote.Resource<Movies> {
        if (response.isSuccessful) {
            upcomingPageNum++
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



                return Resource.Success(upcomingResponse ?: resultResponse!!)
            }
        }
        return Resource.Error(response.message())


    }
    private fun handleSearchResponse(response: Response <Movies>) : Resource<Movies> {
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
    private fun handleSearchResponseByDate(response: Response <Movies>) : Resource<Movies> {
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

   //local
    fun insertMovie(movie: Result) {
       viewModelScope.launch {
           repository.local.dao.insert(movie)
       }

    }

    val movies: LiveData<List<Result>> = repository.local.dao.getAllMovies().asLiveData()









}