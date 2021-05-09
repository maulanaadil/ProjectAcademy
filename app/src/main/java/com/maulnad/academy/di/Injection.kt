package com.maulnad.academy.di

import android.content.Context
import com.maulnad.academy.data.source.AcademyRepository
import com.maulnad.academy.data.source.remote.RemoteDataSource
import com.maulnad.academy.utils.JsonHelper

object Injection {
    fun provideRepository(context: Context): AcademyRepository {
        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))

        return AcademyRepository.getInstance(remoteDataSource)
    }
}