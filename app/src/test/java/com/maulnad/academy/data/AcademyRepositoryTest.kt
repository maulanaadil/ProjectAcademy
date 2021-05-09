package com.maulnad.academy.data

import com.maulnad.academy.data.source.remote.RemoteDataSource
import com.maulnad.academy.utils.DataDummy
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class AcademyRepositoryTest {

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private val academyRepository = FakeAcademyRepository(remote)

    private val courseResponse = DataDummy.generateRemoteDummyCourses()
    private val courseId = courseResponse[0].id
    private val moduleResponse = DataDummy.generateRemoteDummyModules(courseId)
    private val moduleId = moduleResponse[0].moduleId
    private val content = DataDummy.generateRemoteDummyContent(moduleId)

    @Test
    fun getAllCourses() {
        `when`(remote.getAllCourses()).thenReturn(courseResponse)
        val courseEntities = academyRepository.getAllCourses()
        verify(remote).getAllCourses()
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.size.toLong())
    }

    @Test
    fun getBookmarkedCourses() {
        `when`(remote.getAllCourses()).thenReturn(courseResponse)
        val courseEntities = academyRepository.getBookmarkedCourses()
        verify(remote).getAllCourses()
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.size.toLong())
    }

    @Test
    fun getCourseWithModules() {
        `when`(remote.getAllCourses()).thenReturn(courseResponse)
        val resultCourse = academyRepository.getCourseWithModules(courseId)
        verify(remote).getAllCourses()
        assertNotNull(resultCourse)
        assertEquals(courseResponse[0].title, resultCourse.title)
    }

    @Test
    fun getAllModulesByCourse() {
        `when`(remote.getModule(courseId)).thenReturn(moduleResponse)
        val moduleEntities = academyRepository.getAllModulesByCourse(courseId)
        verify(remote).getModule(courseId)
        assertNotNull(moduleEntities)
        assertEquals(moduleResponse.size.toLong(), moduleEntities.size.toLong())
    }

    @Test
    fun getContent() {
        `when`(remote.getModule(courseId)).thenReturn(moduleResponse)
        `when`(remote.getContent(moduleId)).thenReturn(content)
        val resultModule = academyRepository.getContent(courseId, moduleId)
        verify(remote).getContent(moduleId)
        assertNotNull(resultModule)
        assertEquals(content.content, resultModule.contentEntity?.content)
    }
}