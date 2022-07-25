package com.nenad.cinemaquery.data.repository

import com.nenad.cinemaquery.data.local.LocalDatasource
import com.nenad.cinemaquery.data.remote.RemoteDatasource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(val local: LocalDatasource, val remote: RemoteDatasource){





}