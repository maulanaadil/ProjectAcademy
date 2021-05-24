package com.maulnad.academy.ui.reader

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maulnad.academy.R
import com.maulnad.academy.data.source.local.entity.ModuleEntity
import com.maulnad.academy.ui.reader.content.ModuleContentFragment
import com.maulnad.academy.ui.reader.list.ModuleListFragment
import com.maulnad.academy.viewmodel.ViewModelFactory
import com.maulnad.academy.vo.Resource
import com.maulnad.academy.vo.Status

class CourseReaderActivity : AppCompatActivity(), CourseReaderCallback {
    companion object {
        const val EXTRA_COURSE_ID = "extra_course_id"
    }

    private var isLarge = false
    private lateinit var viewModel: CourseReaderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_reader)

        if (findViewById<View>(R.id.frame_list) != null) {
            isLarge = true
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[CourseReaderViewModel::class.java]
        viewModel.modules.observe(this, initObserver)

        val bundle = intent.extras
        if (bundle != null) {
            val courseId = bundle.getString(EXTRA_COURSE_ID)
            if (courseId != null) {
                viewModel.setSelectedCourse(courseId)
                populateFragment()
            }
        }
    }

    private fun populateFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (!isLarge) {
            var fragment = supportFragmentManager.findFragmentByTag(ModuleContentFragment.TAG)
            if (fragment == null) {
                fragment = ModuleListFragment.newInstance()
                fragmentTransaction.add(R.id.frame_container, fragment, ModuleListFragment.TAG)
                fragmentTransaction.addToBackStack(null)
            }
            fragmentTransaction.commit()
        } else {
            var fragmentList = supportFragmentManager.findFragmentByTag(ModuleListFragment.TAG)

            if (fragmentList == null) {
                fragmentList = ModuleListFragment.newInstance()
                fragmentTransaction.add(R.id.frame_list, fragmentList, ModuleListFragment.TAG)
            }

            var fragmentContent =
                supportFragmentManager.findFragmentByTag(ModuleContentFragment.TAG)

            if (fragmentContent == null) {
                fragmentContent = ModuleContentFragment.newInstance()
                fragmentTransaction.add(
                    R.id.frame_content,
                    fragmentContent,
                    ModuleContentFragment.TAG
                )
            }
            fragmentTransaction.commit()
        }
    }

    private fun removeObserver() {
        viewModel.modules.removeObserver(initObserver)
    }

    private fun getLastReadFromModules(moduleEntities: List<ModuleEntity>): String? {
        var lastReadModule: String? = null

        for (moduleEntity in moduleEntities) {
            if (moduleEntity.read) {
                lastReadModule = moduleEntity.moduleId
                continue
            }
            break
        }
        return lastReadModule
    }

    private val initObserver: Observer<Resource<List<ModuleEntity>>> = Observer { modules ->
        if (modules != null) {
            when (modules.status) {
                Status.LOADING -> {
                    // Todo
                }
                Status.SUCCESS -> {
                    val dataModules: List<ModuleEntity>? = modules.data
                    if (dataModules != null && dataModules.isNotEmpty()) {
                        val firstModule = dataModules[0]
                        val isFirstModuleRead = firstModule.read

                        if (!isFirstModuleRead) {
                            val firstModuleId = firstModule.moduleId
                            viewModel.setSelectedModule(firstModuleId)
                        } else {
                            val lastReadModuleId = getLastReadFromModules(dataModules)
                            if (lastReadModuleId != null) {
                                viewModel.setSelectedModule(lastReadModuleId)
                            }
                        }

                    }
                    removeObserver()
                }

                Status.ERROR -> {
                    Toast.makeText(this, "Gagal menampilkan data.", Toast.LENGTH_SHORT).show()
                    removeObserver()
                }
            }
        }
    }

    override fun moveTo(position: Int, moduleId: String) {
        if (!isLarge) {
            val fragment = ModuleContentFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_container, fragment, ModuleContentFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}