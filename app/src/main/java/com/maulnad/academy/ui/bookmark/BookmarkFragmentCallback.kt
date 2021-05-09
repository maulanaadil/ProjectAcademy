package com.maulnad.academy.ui.bookmark

import com.maulnad.academy.data.CourseEntity

interface BookmarkFragmentCallback {
    fun shareOnClick(course: CourseEntity)
}
