package com.maulnad.academy.di

import android.content.Context
import com.maulnad.academy.data.AcademyRepository
import com.maulnad.academy.data.source.local.LocalDataSource
import com.maulnad.academy.data.source.local.room.AcademyDatabase
import com.maulnad.academy.data.source.remote.RemoteDataSource
import com.maulnad.academy.utils.AppExecutors
import com.maulnad.academy.utils.JsonHelper

object Injection {
    fun provideRepository(context: Context): AcademyRepository {

        val database = AcademyDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = LocalDataSource.getInstance(database.academyDao())
        val appExecutors = AppExecutors()

        return AcademyRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}