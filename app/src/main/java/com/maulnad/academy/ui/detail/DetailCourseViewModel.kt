package com.maulnad.academy.ui.detail

import androidx.lifecycle.ViewModel
import com.maulnad.academy.data.CourseEntity
import com.maulnad.academy.data.ModuleEntity
import com.maulnad.academy.data.source.AcademyRepository
import com.maulnad.academy.utils.DataDummy

class DetailCourseViewModel(private val academyRepository: AcademyRepository) : ViewModel() {
    private lateinit var courseId: String

    fun setSelectedCourse(courseId: String) {
        this.courseId = courseId
    }

    fun getCourse(): CourseEntity = academyRepository.getCourseWithModules(courseId)

    fun getModules(): List<ModuleEntity> = academyRepository.getAllModulesByCourse(courseId)
}