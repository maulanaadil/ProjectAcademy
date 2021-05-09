package com.maulnad.academy.data

import com.maulnad.academy.data.source.AcademyDataSource
import com.maulnad.academy.data.source.remote.RemoteDataSource

class FakeAcademyRepository(private val remoteDataSource: RemoteDataSource):
    AcademyDataSource {

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

    override fun getCourseWithModules(courseId: String): CourseEntity {
        val courseResponse = remoteDataSource.getAllCourses()
        lateinit var course: CourseEntity
        for (response in courseResponse) {
            if (response.id == courseId) {
                course = CourseEntity(
                    response.id,
                    response.title,
                    response.description,
                    response.date,
                    false,
                    response.imagePath
                )
            }
        }
        return course
    }

    override fun getAllModulesByCourse(courseId: String): List<ModuleEntity> {
        val moduleResponse = remoteDataSource.getModule(courseId)
        val moduleList = ArrayList<ModuleEntity>()
        for (response in moduleResponse) {
            val module = ModuleEntity(
                response.moduleId,
                response.courseId,
                response.title,
                response.position,
                false
            )
            moduleList.add(module)
        }
        return moduleList
    }

    override fun getContent(courseId: String, moduleId: String): ModuleEntity {
        val contentResponse = remoteDataSource.getModule(courseId)
        lateinit var module: ModuleEntity
        for (response in contentResponse) {
            if (response.moduleId == moduleId ) {
                module = ModuleEntity(
                    response.moduleId,
                    response.courseId,
                    response.title,
                    response.position,
                    false
                )
                module.contentEntity = ContentEntity(remoteDataSource.getContent(moduleId).content)
                break
            }
        }
        return module
    }
}