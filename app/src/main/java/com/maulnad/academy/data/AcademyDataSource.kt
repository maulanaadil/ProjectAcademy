package com.maulnad.academy.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.maulnad.academy.data.source.local.entity.CourseEntity
import com.maulnad.academy.data.source.local.entity.CourseWithModule
import com.maulnad.academy.data.source.local.entity.ModuleEntity
import com.maulnad.academy.vo.Resource

interface AcademyDataSource {
    fun getAllCourses(): LiveData<Resource<PagedList<CourseEntity>>>

    fun getBookmarkedCourses(): LiveData<PagedList<CourseEntity>>

    fun getCourseWithModules(courseId: String): LiveData<Resource<CourseWithModule>>

    fun getAllModulesByCourse(courseId: String): LiveData<Resource<List<ModuleEntity>>>

    fun getContent(moduleId: String): LiveData<Resource<ModuleEntity>>

    fun setCourseBookmark(course: CourseEntity, state: Boolean)

    fun setReadModule(module: ModuleEntity)
}