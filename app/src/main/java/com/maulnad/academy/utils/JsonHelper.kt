package com.maulnad.academy.utils

import android.content.Context
import com.maulnad.academy.data.source.remote.response.ContentResponse
import com.maulnad.academy.data.source.remote.response.CourseResponse
import com.maulnad.academy.data.source.remote.response.ModuleResponse
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class JsonHelper(private val context: Context) {
    private fun parsingFileToString(fileName: String): String? {
        return try {
            val `is` = context.assets.open(fileName)
            val buffer = ByteArray(`is`.available())
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    fun loadCourse(): List<CourseResponse> {
        val list = ArrayList<CourseResponse>()
        try {
            val responseObject = JSONObject(parsingFileToString("CourseResponses.json").toString())
            val listArray = responseObject.getJSONArray("courses")
            for (data in 0 until listArray.length()) {
                val course = listArray.getJSONObject(data)

                val id = course.getString("id")
                val title = course.getString("title")
                val description = course.getString("description")
                val date = course.getString("date")
                val imagePath = course.getString("imagePath")

                val courseResponse = CourseResponse(id, title, description, date, imagePath)
                list.add(courseResponse)
            }
        } catch (error: JSONException) {
            error.printStackTrace()
        }
        return list
    }

    fun loadModule(courseId: String): List<ModuleResponse> {
        val fileName = String.format("Module_%s.json", courseId)
        val list = ArrayList<ModuleResponse>()
        try {
            val result = parsingFileToString(fileName)
            if (result != null) {
                val responseObject = JSONObject(result)
                val listArray = responseObject.getJSONArray("modules")
                for (data in 0 until listArray.length()) {
                    val course = listArray.getJSONObject(data)

                    val moduleId = course.getString("moduleId")
                    val title = course.getString("title")
                    val position = course.getInt("position")

                    val moduleResponse = ModuleResponse(moduleId, courseId, title, position)
                    list.add(moduleResponse)
                }
            }
        } catch (error: JSONException) {
            error.printStackTrace()
        }
        return list
    }

    fun loadContent(moduleId: String): ContentResponse {
        val fileName = String.format("Content_%s.json", moduleId)
        var contentResponse: ContentResponse? = null
        try {
            val result = parsingFileToString(fileName)
            if (result != null) {
                val responseObject = JSONObject(result)

                val content = responseObject.getString("content")
                contentResponse = ContentResponse(moduleId, content)
            }
        } catch (error: JSONException) {
            error.printStackTrace()
        }
        return contentResponse as ContentResponse
    }
}
