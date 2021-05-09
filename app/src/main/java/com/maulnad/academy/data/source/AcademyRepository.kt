package com.maulnad.academy.data.source

import com.maulnad.academy.data.CourseEntity
import com.maulnad.academy.data.ModuleEntity
import com.maulnad.academy.data.source.remote.RemoteDataSource

class AcademyRepository private constructor(private val remoteDataSource: RemoteDataSource): AcademyDataSource {

    companion object {
        @Volatile
        private var instance: AcademyRepository? = null

        fun getInstance(remoteData: RemoteDataSource): AcademyRepository = instance ?: synchronized(this) {
            instance ?: AcademyRepository(remoteData).apply { instance = this }
        }
    }


    override fun getAllCourses(): List<CourseEntity> {
        val courseResponses = remoteDataSource.getAllCourses()
        val courseList = ArrayList<CourseEntity>()

        for (response in courseResponses) {
            val course = CourseEntity(
                response.id,
                response.title,
                response.description,
                response.date,
                false,
                response.imagePath
            )
            courseList.add(course)
        }
        return courseList
    }

    override fun getBookmarkedCourses(): List<CourseEntity> {
        TODO("Not yet implemented")
    }

    override fun getCourseWithModules(courseId: String): CourseEntity {
        TODO("Not yet implemented")
    }

    override fun getAllModulesByCourse(courseId: String): List<ModuleEntity> {
        TODO("Not yet implemented")
    }

    override fun getContent(courseId: String, moduleId: String): ModuleEntity {
        TODO("Not yet implemented")
    }
}