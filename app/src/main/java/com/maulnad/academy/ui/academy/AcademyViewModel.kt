package com.maulnad.academy.ui.academy

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.maulnad.academy.data.source.local.entity.CourseEntity
import com.maulnad.academy.data.AcademyRepository
import com.maulnad.academy.vo.Resource

class AcademyViewModel(private val academyRepository: AcademyRepository): ViewModel() {
    fun getCourses(): LiveData<Resource<PagedList<CourseEntity>>> = academyRepository.getAllCourses()
}