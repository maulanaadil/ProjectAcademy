package com.maulnad.academy.ui.bookmark

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class BookmarkViewModelTest {

    lateinit var viewModel: BookmarkViewModel
    @Before
    fun setUp() {
        viewModel = BookmarkViewModel()
    }

    @Test
    fun getBookmarks() {
        val courseEntities = viewModel.getBookmarks()
        assertNotNull(courseEntities)
        assertEquals(5, courseEntities.size)
    }
}