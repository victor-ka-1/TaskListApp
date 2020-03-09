package com.example.tasklistproject.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklistproject.R
import com.example.tasklistproject.TaskViewModel
import com.example.tasklistproject.room.Task
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainScreenActivity : AppCompatActivity() {

    val myViewModel: TaskViewModel by viewModel()
    lateinit var myAdapter :RecycleViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        myAdapter =
            RecycleViewAdapter(object :
                ItemListListeners {
                override fun onItemClick(task: Task) {
                    TaskBottomSheetDialogFragment.newInstance(OpenReason.REASON_EDIT)
                        .apply {
                            arguments?.putBundle(this@MainScreenActivity.resources.getString(R.string.TASK_BUNDLE), Bundle().apply {
                                putBoolean(this@MainScreenActivity.resources.getString(R.string.DONE_STATUS), task.isDone)
                                putString(this@MainScreenActivity.resources.getString(R.string.TASK_NAME), task.name)
                                putString(this@MainScreenActivity.resources.getString(R.string.TASK_ADDITIONAL_INFO), task.info)
                                putInt(this@MainScreenActivity.resources.getString(R.string.TASK_ID), task.id!!)
                            })
                            show(supportFragmentManager,"bottom_sheet_fragmentDialog")
                        }
                }
                override fun onDeleteButtonClick(task: Task) {
                    myViewModel.deleteTask(task)
                }

                override fun onCheckBoxClick(task: Task) {
                    task.isDone = !task.isDone
                    myViewModel.updateTask(task)
                }
            })


        val myLayoutManager = LinearLayoutManager(this@MainScreenActivity)
        todoRecycleListView.apply {
            layoutManager = myLayoutManager
            adapter = myAdapter
        }

        myViewModel.finalLiveData.observe(this, Observer { updatedList ->
        if(updatedList != null) {
            myAdapter.updateRecyclerViewList(updatedList)
        }
        })


        addNewTaskButton.setOnClickListener {
            val fragment = TaskBottomSheetDialogFragment.newInstance(OpenReason.REASON_ADD)
            fragment.show(supportFragmentManager,"bottom_sheet_fragmentDialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater =menuInflater
        inflater.inflate(R.menu.tasks_options_menu,menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.deleteAllMenuItem -> {
                myViewModel.deleteAllTasks()
                true
            }
            R.id.choose_doneTasks_menuItem ->{
                myAdapter.apply {
                    myViewModel.showType.value = ShowType.DONE_TASKS
                }
                item.isChecked =true
                return true
            }
            R.id.choose_undoneTasks_menuItem ->{
                myAdapter.apply {
                    myViewModel.showType.value = ShowType.UNDONE_TASKS
                }
                item.isChecked =true
                return true
            }
            R.id.choose_allTasks_menuItem ->{
                myAdapter.apply {
                    myViewModel.showType.value = ShowType.ALL_TASKS
                }
                item.isChecked =true
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
