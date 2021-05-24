package com.maulnad.academy.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.maulnad.academy.data.source.local.LocalDataSource
import com.maulnad.academy.data.source.local.entity.CourseEntity
import com.maulnad.academy.data.source.local.entity.CourseWithModule
import com.maulnad.academy.data.source.local.entity.ModuleEntity
import com.maulnad.academy.data.source.remote.RemoteDataSource
import com.maulnad.academy.utils.AppExecutors
import com.maulnad.academy.utils.DataDummy
import com.maulnad.academy.utils.LiveDataTestUtil
import com.maulnad.academy.utils.PagedListUtil
import com.maulnad.academy.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*


class AcademyRepositoryTest {

    private val courseResponse = DataDummy.generateRemoteDummyCourses()

    private val courseId = courseResponse[0].id
    private val moduleResponse = DataDummy.generateRemoteDummyModules(courseId)
    private val moduleId = moduleResponse[0].moduleId
    private val content = DataDummy.generateRemoteDummyContent(moduleId)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val academyRepository = FakeAcademyRepository(remote, local, appExecutors)


    @Test
    fun getAllCourses() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, CourseEntity>
        `when`(local.getAllCourses()).thenReturn(dataSourceFactory)
        academyRepository.getAllCourses()

        val courseEntities = Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyCourse()))
        verify(local).getAllCourses()
        assertNotNull(courseEntities.data)
        assertEquals(courseResponse.size.toLong(), courseEntities.data?.size?.toLong())
    }

    @Test
    fun getAllModulesByCourse() {
        val dummyModules = MutableLiveData<List<ModuleEntity>>()
        dummyModules.value = DataDummy.generateDummyModules(courseId)
        `when`(local.getAllModulesByCourse(courseId)).thenReturn(dummyModules)

        val courseEntities =
            LiveDataTestUtil.getValue(academyRepository.getAllModulesByCourse(courseId))
        verify(local).getAllModulesByCourse(courseId)
        assertNotNull(courseEntities.data)
        assertEquals(moduleResponse.size.toLong(), courseEntities.data?.size?.toLong())
    }

    @Test
    fun getBookmarkedCourses() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, CourseEntity>
        `when`(local.getBookmarkedCourse()).thenReturn(dataSourceFactory)
        academyRepository.getBookmarkedCourses()

        val courseEntities = Resource.success(PagedListUtil.mockPagedList(DataDummy.generateDummyCourse()))
        verify(local).getBookmarkedCourse()
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.data?.size?.toLong())
    }

    @Test
    fun getCourseWithModules() {
        val dummyEntity = MutableLiveData<CourseWithModule>()
        dummyEntity.value = DataDummy.generateDummyCourseWithModules(DataDummy.generateDummyCourse()[0], false)
        `when`(local.getCourseWithModule(courseId)).thenReturn(dummyEntity)

        val courseEntities =
            LiveDataTestUtil.getValue(academyRepository.getCourseWithModules(courseId))
        verify(local).getCourseWithModule(courseId)
        assertNotNull(courseEntities.data)
        assertNotNull(courseEntities.data?.mCourse?.title)
        assertEquals(courseResponse[0].title, courseEntities.data?.mCourse?.title)
    }


    @Test
    fun getContent() {
        val dummyEntity = MutableLiveData<ModuleEntity>()
        dummyEntity.value = DataDummy.generateDummyModulesWithContent(moduleId)
        `when`(local.getModuleWithContent(courseId)).thenReturn(dummyEntity)

        val courseEntitiesContent =
            LiveDataTestUtil.getValue(academyRepository.getContent(courseId))
        verify(local).getModuleWithContent(courseId)
        assertNotNull(courseEntitiesContent)
        assertNotNull(courseEntitiesContent.data?.contentEntity)
        assertNotNull(courseEntitiesContent.data?.contentEntity?.content)
        assertEquals(content.content, courseEntitiesContent.data?.contentEntity?.content)
    }
}