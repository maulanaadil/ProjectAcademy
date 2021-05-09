package com.maulnad.academy.data.source

import com.maulnad.academy.data.CourseEntity
import com.maulnad.academy.data.ModuleEntity

interface AcademyDataSource {
    fun getAllCourses(): List<CourseEntity>

    fun getBookmarkedCourses(): List<CourseEntity>

    fun getCourseWithModules(courseId: String): CourseEntity

    fun getAllModulesByCourse(courseId: String): List<ModuleEntity>

    fun getContent(courseId: String, moduleId: String): ModuleEntity
}