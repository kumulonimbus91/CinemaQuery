package com.nenad.cinemaquery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nenad.cinemaquery.data.model.Result

@Database(entities = [Result::class], version = 2)
abstract class MoviesDatabase: RoomDatabase() {

abstract fun getDao(): MoviesDao


}