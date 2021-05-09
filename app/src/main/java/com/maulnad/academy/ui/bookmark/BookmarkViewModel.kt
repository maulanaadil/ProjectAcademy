package com.maulnad.academy.ui.bookmark

import androidx.lifecycle.ViewModel
import com.maulnad.academy.data.CourseEntity
import com.maulnad.academy.data.source.AcademyRepository
import com.maulnad.academy.utils.DataDummy

class BookmarkViewModel(private val academyRepository: AcademyRepository): ViewModel() {
    fun getBookmarks(): List<CourseEntity> = academyRepository.getBookmarkedCourses()
}