package com.maulnad.academy.data.remote

import com.maulnad.academy.data.remote.response.ContentResponse
import com.maulnad.academy.data.remote.response.CourseResponse
import com.maulnad.academy.data.remote.response.ModuleResponse
import com.maulnad.academy.utils.JsonHelper

class RemoteDataSource private constructor(private val jsonHelper: JsonHelper){
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: JsonHelper): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource(helper).apply { instance = this }
        }
    }

    fun getAllCourses(): List<CourseResponse> = jsonHelper.loadCourse()

    fun getModule(courseId: String): List<ModuleResponse> = jsonHelper.loadModule(courseId)

    fun getContent(moduleId: String): ContentResponse = jsonHelper.loadContent(moduleId)
}