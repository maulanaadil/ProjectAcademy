package com.maulnad.academy.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.maulnad.academy.data.source.local.LocalDataSource
import com.maulnad.academy.data.source.local.entity.CourseEntity
import com.maulnad.academy.data.source.local.entity.CourseWithModule
import com.maulnad.academy.data.source.local.entity.ModuleEntity
import com.maulnad.academy.data.source.remote.ApiResponse
import com.maulnad.academy.data.source.remote.RemoteDataSource
import com.maulnad.academy.data.source.remote.response.ContentResponse
import com.maulnad.academy.data.source.remote.response.CourseResponse
import com.maulnad.academy.data.source.remote.response.ModuleResponse
import com.maulnad.academy.utils.AppExecutors
import com.maulnad.academy.vo.Resource

class AcademyRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : AcademyDataSource {

    companion object {
        @Volatile
        private var instance: AcademyRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): AcademyRepository =
            instance ?: synchronized(this) {
                instance ?: AcademyRepository(
                    remoteData,
                    localData,
                    appExecutors
                ).apply { instance = this }
            }
    }


    override fun getAllCourses(): LiveData<Resource<PagedList<CourseEntity>>> {
        return object :
            NetworkBoundResource<PagedList<CourseEntity>, List<CourseResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<CourseEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllCourses(), config).build()
            }

            override fun shouldFetch(data: PagedList<CourseEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<CourseResponse>>> =
                remoteDataSource.getAllCourses()

            override fun saveCallResult(courseResponse: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in courseResponse) {
                    val course = CourseEntity(
                        response.id,
                        response.title,
                        response.description,
                        response.date,
                        false,
                        response.imagePath
                    )
                    courseList.add(course)
                }
                localDataSource.insertCourses(courseList)
            }

        }.asLiveData()
    }

    override fun getCourseWithModules(courseId: String): LiveData<Resource<CourseWithModule>> {
        return object : NetworkBoundResource<CourseWithModule, List<ModuleResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<CourseWithModule> =
                localDataSource.getCourseWithModule(courseId)

            override fun shouldFetch(data: CourseWithModule?): Boolean =
                data?.mModule == null || data.mModule.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> =
                remoteDataSource.getModule(courseId)

            override fun saveCallResult(moduleResponse: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (response in moduleResponse) {
                    val course = ModuleEntity(
                        response.moduleId,
                        response.courseId,
                        response.title,
                        response.position,
                        false
                    )
                    moduleList.add(course)
                }
                localDataSource.insertModules(moduleList)
            }

        }.asLiveData()
    }

    override fun getAllModulesByCourse(courseId: String): LiveData<Resource<List<ModuleEntity>>> {
        return object :
            NetworkBoundResource<List<ModuleEntity>, List<ModuleResponse>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<ModuleEntity>> =
                localDataSource.getAllModulesByCourse(courseId)

            override fun shouldFetch(modules: List<ModuleEntity>?): Boolean =
                modules == null || modules.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<ModuleResponse>>> =
                remoteDataSource.getModule(courseId)

            override fun saveCallResult(moduleResponse: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for (response in moduleResponse) {
                    val module = ModuleEntity(
                        response.moduleId,
                        response.courseId,
                        response.title,
                        response.position,
                        false
                    )
                    moduleList.add(module)
                }
                localDataSource.insertModules(moduleList)
            }

        }.asLiveData()
    }

    override fun getContent(moduleId: String): LiveData<Resource<ModuleEntity>> {
        return object : NetworkBoundResource<ModuleEntity, ContentResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<ModuleEntity> =
                localDataSource.getModuleWithContent(moduleId)

            override fun shouldFetch(moduleEntity: ModuleEntity?): Boolean =
                moduleEntity?.contentEntity == null

            override fun createCall(): LiveData<ApiResponse<ContentResponse>> =
                remoteDataSource.getContent(moduleId)

            override fun saveCallResult(contentResponse: ContentResponse) =
                localDataSource.updateContent(contentResponse.content.toString(), moduleId)

        }.asLiveData()
    }

    override fun getBookmarkedCourses(): LiveData<PagedList<CourseEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()
        return LivePagedListBuilder(localDataSource.getBookmarkedCourse(), config).build()
    }


    override fun setCourseBookmark(course: CourseEntity, state: Boolean) =
        appExecutors.diskIO().execute { localDataSource.setCourseBookmark(course, state) }

    override fun setReadModule(module: ModuleEntity) =
        appExecutors.diskIO().execute { localDataSource.setReadModule(module) }

}