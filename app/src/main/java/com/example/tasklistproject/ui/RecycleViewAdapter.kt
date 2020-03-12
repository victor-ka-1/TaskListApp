package com.example.tasklistproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklistproject.R
import com.example.tasklistproject.room.Task
import kotlinx.android.synthetic.main.mynew_recycler_listview_vitya.view.*
import java.util.*
import kotlin.collections.ArrayList

enum class ShowType{
    ALL_TASKS, DONE_TASKS, UNDONE_TASKS, IMPORTANT_TASKS
}

class RecycleViewAdapter(private val listener: ItemListListeners) : RecyclerView.Adapter<MyViewHolder>(), Filterable{

    private val tasksList: ArrayList<Task> = ArrayList()
    private val fullList = ArrayList(tasksList)

    private fun setData(newList:List<Task>){
        tasksList.clear()
        tasksList.addAll(newList)

        fullList.clear()
        fullList.addAll(tasksList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mynew_recycler_listview_vitya, parent,false)
        return MyViewHolder(itemView, listener)
    }
    override fun getItemCount(): Int {
       return tasksList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(tasksList[position])
    }

    fun updateRecyclerViewList( newList: List<Task>){
        val taskDiffUtilCallback = TaskDiffUtil(tasksList, newList)
        val taskDiffResult = DiffUtil.calculateDiff(taskDiffUtilCallback)
        setData(newList)
        taskDiffResult.dispatchUpdatesTo(this)
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList : ArrayList<Task> = ArrayList()
                if (constraint == null || constraint.isEmpty()){
                    filteredList.addAll(fullList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for(listItem in fullList){
                        if(listItem.name.contains(filterPattern)){
                            filteredList.add(listItem)
                        }
                    }
                }

                return FilterResults().apply { values =filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                updateListWithSearch(results!!.values as List<Task>)
            }
            fun updateListWithSearch(newList: List<Task>){
                val taskDiffUtilCallback = TaskDiffUtil(tasksList, newList)
                val taskDiffResult = DiffUtil.calculateDiff(taskDiffUtilCallback)
                tasksList.clear()
                tasksList.addAll(newList)
                taskDiffResult.dispatchUpdatesTo(this@RecycleViewAdapter)
            }
        }
    }
}


class MyViewHolder(private val view1: View, private val listeners: ItemListListeners) : RecyclerView.ViewHolder(view1){

    fun bind(task: Task){
        view1.taskId.text = task.id.toString()
        view1.isDone_checkBoxik.isChecked= task.isDone
        view1.tvTask.text = task.name
        if(task.isImportant) {
            view1.setBackgroundColor( ContextCompat.getColor(view1.context,R.color.highlightedTask ))
            view1.deleteButton.setBackgroundColor(ContextCompat.getColor(view1.context,R.color.highlightedTask ))
        }else{
            view1.setBackgroundColor( ContextCompat.getColor(view1.context,R.color.colorWhite ))
            view1.deleteButton.setBackgroundColor(ContextCompat.getColor(view1.context,R.color.colorWhite ))
        }

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
                && oldList[oldItemPosition].info == newList[newItemPosition].info
                && oldList[oldItemPosition].isImportant == newList[newItemPosition].isImportant)
    }
}


interface ItemListListeners {
    fun onItemClick(task: Task)
    fun onDeleteButtonClick(task: Task)
    fun onCheckBoxClick(task: Task)
}


