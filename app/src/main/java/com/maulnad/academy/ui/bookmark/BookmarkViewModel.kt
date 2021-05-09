package com.maulnad.academy.ui.bookmark

import androidx.lifecycle.ViewModel
import com.maulnad.academy.data.CourseEntity
import com.maulnad.academy.utils.DataDummy

class BookmarkViewModel: ViewModel() {
    fun getBookmarks(): List<CourseEntity> = DataDummy.generateDummyCourse()
}