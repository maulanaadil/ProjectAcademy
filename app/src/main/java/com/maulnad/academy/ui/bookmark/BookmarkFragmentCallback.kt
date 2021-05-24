package com.maulnad.academy.ui.bookmark

import com.maulnad.academy.data.source.local.entity.CourseEntity

interface BookmarkFragmentCallback {
    fun shareOnClick(course: CourseEntity)
}
