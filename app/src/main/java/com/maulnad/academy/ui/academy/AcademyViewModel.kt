package com.maulnad.academy.ui.academy

import androidx.lifecycle.ViewModel
import com.maulnad.academy.data.CourseEntity
import com.maulnad.academy.data.source.AcademyRepository
import com.maulnad.academy.utils.DataDummy

class AcademyViewModel(private val academyRepository: AcademyRepository): ViewModel() {
    fun getCourses(): List<CourseEntity> = academyRepository.getAllCourses()
}