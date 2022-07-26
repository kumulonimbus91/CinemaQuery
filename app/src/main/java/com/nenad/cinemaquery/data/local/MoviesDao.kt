package com.nenad.cinemaquery.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nenad.cinemaquery.data.model.Result
import kotlinx.coroutines.flow.Flow


@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Result): Long

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<MutableList<Result>>

    @Delete
    suspend fun deleteMovie(movie: Result)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()



}