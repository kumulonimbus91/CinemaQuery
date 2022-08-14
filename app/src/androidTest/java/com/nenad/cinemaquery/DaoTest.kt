package com.nenad.cinemaquery

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nenad.cinemaquery.data.local.MoviesDao
import com.nenad.cinemaquery.data.local.MoviesDatabase
import com.nenad.cinemaquery.data.model.Result
import com.nenad.cinemaquery.data.remote.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.nenad.cinemaquery.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DaoTest {
    private lateinit var database: MoviesDatabase
    private lateinit var dao: MoviesDao


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), MoviesDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.getDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertMovie() = runBlockingTest {
        val movie = Result(1, "Long time ago in a distant galaxy",
            3.7, "","2022-01-09", "Star wars", 7.8, 2000)
        dao.insert(movie)

        val allMovies = dao.getAllMovies().getOrAwaitValue()

        assertThat(allMovies).contains(movie)


    }

    @Test
    fun deleteMovie() = runBlockingTest {
        val movie =Result (1, "Long time ago in a distant galaxy",
            3.7, "","2022-01-09", "Star wars", 7.8, 2000)
        dao.insert(movie)
        dao.deleteMovie(movie)

        val allMovies = dao.getAllMovies().getOrAwaitValue()

        assertThat(allMovies).doesNotContain(movie)

    }






}