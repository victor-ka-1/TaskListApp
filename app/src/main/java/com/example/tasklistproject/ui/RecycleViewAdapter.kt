package com.example.tasklistproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklistproject.R
import com.example.tasklistproject.room.Task
import kotlinx.android.synthetic.main.mynew_recycler_listview_vitya.view.*

enum class ShowType{
    ALL_TASKS, DONE_TASKS, UNDONE_TASKS
}

class RecycleViewAdapter(private val listener: ItemListListeners) : RecyclerView.Adapter<MyViewHolder>(){

    private val taskList: ArrayList<Task> = ArrayList()

    private fun setData(newList:List<Task>){
        taskList.clear()
        taskList.addAll(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mynew_recycler_listview_vitya, parent,false)
        return MyViewHolder(itemView, listener)
    }
    override fun getItemCount(): Int {
       return taskList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    fun updateRecyclerViewList( newList: List<Task>){
        val taskDiffUtilCallback = TaskDiffUtil(taskList, newList)
        val taskDiffResult = DiffUtil.calculateDiff(taskDiffUtilCallback)
        setData(newList)
        taskDiffResult.dispatchUpdatesTo(this)
    }
}


class MyViewHolder(private val view1: View, private val listeners: ItemListListeners) : RecyclerView.ViewHolder(view1){

    fun bind(task: Task){
        view1.taskId.text = task.id.toString()
        view1.isDone_checkBoxik.isChecked= task.isDone
        view1.tvTask.text = task.name

        view1.deleteButton.setOnClickListener {
            listeners.onDeleteButtonClick(task)
        }

        view1.isDone_checkBoxik.setOnClickListener{
            listeners.onCheckBoxClick(task)
        }

        view1.setOnClickListener {
            listeners.onItemClick(task)
        }

    }
}

class TaskDiffUtil(private val oldList: List<Task>, private val newList: List<Task>) : DiffUtil.Callback(){

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return   (oldList[oldItemPosition].isDone == newList[newItemPosition].isDone
                && oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].info == newList[newItemPosition].info)
    }
}


interface ItemListListeners {
    fun onItemClick(task: Task)
    fun onDeleteButtonClick(task: Task)
    fun onCheckBoxClick(task: Task)
}


