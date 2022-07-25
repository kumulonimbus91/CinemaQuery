package com.nenad.cinemaquery.di

import android.content.Context
import androidx.room.Room
import com.nenad.cinemaquery.data.local.MoviesDatabase
import com.nenad.cinemaquery.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MoviesDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDatabaseDao(moviesDatabase: MoviesDatabase) = moviesDatabase.getDao()
}