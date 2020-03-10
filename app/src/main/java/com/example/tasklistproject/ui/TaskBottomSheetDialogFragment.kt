package com.example.tasklistproject.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.tasklistproject.R
import com.example.tasklistproject.TaskViewModel
import com.example.tasklistproject.room.Task
import kotlinx.android.synthetic.main.bottom_sheet_add_edit.view.*
import kotlinx.android.synthetic.main.bottom_sheet_add_edit.view.checkbox_doneState
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

enum class OpenReason{
    REASON_EDIT, REASON_ADD
}
const val OPEN_REASON="reason"

class TaskBottomSheetDialogFragment : BaseFullScreenBottomSheetDialogFragment() {

    private val myViewModel: TaskViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if ( (arguments?.getSerializable(resources.getString(R.string.OPEN_REASON)) as OpenReason) == OpenReason.REASON_EDIT){
            view.button_CreateUpdate_Task.text = resources.getString(R.string.update_button)

            if(arguments?.getBundle(resources.getString(R.string.TASK_BUNDLE)) != null) {
                view.checkbox_doneState.isChecked = arguments!!.getBundle(resources.getString(R.string.TASK_BUNDLE))!!
                    .getBoolean(resources.getString(R.string.DONE_STATUS))
                view.bottom_sheet_editText_TaskName.setText(
                    arguments!!.getBundle(resources.getString(R.string.TASK_BUNDLE))!!
                        .getString(resources.getString(R.string.TASK_NAME)))
                view.bottom_sheet_editText_AdditionalInfo.setText(
                    arguments!!.getBundle(resources.getString(R.string.TASK_BUNDLE))!!
                        .getString(resources.getString(R.string.TASK_ADDITIONAL_INFO)))
            }
        }

        view.button_CreateUpdate_Task?.setOnClickListener {
            val id = arguments!!.getBundle(resources.getString(R.string.TASK_BUNDLE))?.
            getInt(resources.getString(R.string.TASK_ID))
            val taskName = view.bottom_sheet_editText_TaskName.text.toString()
            val additionalInfo = view.bottom_sheet_editText_AdditionalInfo.text.toString()
            val doneState = view.checkbox_doneState.isChecked
            if(taskName.isBlank()){
                view.bottom_sheet_editText_TaskName.error = getString(R.string.error_taskname)
                return@setOnClickListener
            }

            when((arguments?.getSerializable(resources.getString(R.string.OPEN_REASON)) as OpenReason)){
                OpenReason.REASON_EDIT ->{
                    myViewModel.updateTask( Task(info = additionalInfo, isDone = doneState,name = taskName,id = id))
                    dismiss()
                }
                OpenReason.REASON_ADD -> {
                    myViewModel.addNewTask( Task(isDone = doneState,name = taskName,info = additionalInfo))
                    dismiss()
                }
            }


            view.bottom_sheet_editText_TaskName.setOnKeyListener { _, _, _ ->
                if (view.bottom_sheet_editText_TaskName.text!!.isNotEmpty()) {
                    view.bottom_sheet_editText_TaskName.error = null //Clear the error
                }
                false
            }

        }
    }

    companion object{
        @JvmStatic
        fun newInstance(openReason:OpenReason): TaskBottomSheetDialogFragment{
            return TaskBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(OPEN_REASON, openReason)
                }
            }
        }
    }
}
