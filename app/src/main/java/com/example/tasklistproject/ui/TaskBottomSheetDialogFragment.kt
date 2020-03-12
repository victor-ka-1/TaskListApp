package com.example.tasklistproject.ui


import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import com.example.tasklistproject.R
import com.example.tasklistproject.TaskViewModel
import com.example.tasklistproject.room.Task
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.bottom_sheet_add_edit.*
import kotlinx.android.synthetic.main.bottom_sheet_add_edit.view.*
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

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        activity!!.addFloatingButton.show()
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
                view.checkbox_imoptantState.isChecked = arguments!!.getBundle(resources.getString(R.string.TASK_BUNDLE))!!
                    .getBoolean(resources.getString(R.string.TASK_IMPORTANT))
            }
        }

        view.button_CreateUpdate_Task?.setOnClickListener {
            val id = arguments!!.getBundle(resources.getString(R.string.TASK_BUNDLE))?.
            getInt(resources.getString(R.string.TASK_ID))
            val taskName = view.bottom_sheet_editText_TaskName.text.toString()
            val additionalInfo = view.bottom_sheet_editText_AdditionalInfo.text.toString()
            val doneState = view.checkbox_doneState.isChecked
            val importantState = view.checkbox_imoptantState.isChecked
            if(taskName.isBlank()){
                view.bottom_sheet_editText_TaskName.error = getString(R.string.error_taskname)
                return@setOnClickListener
            }

            when((arguments?.getSerializable(resources.getString(R.string.OPEN_REASON)) as OpenReason)){
                OpenReason.REASON_EDIT ->{
                    myViewModel.updateTask(
                        Task(info = additionalInfo, isDone = doneState,name = taskName,isImportant = importantState,id = id)
                    )
                    dismiss()
                    activity!!.addFloatingButton.show()
                }
                OpenReason.REASON_ADD -> {
                    myViewModel.addNewTask( Task(isDone = doneState,name = taskName,info = additionalInfo,isImportant = importantState))
                    dismiss()
                    activity!!.addFloatingButton.show()
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

    //extension for opening keyboard when fragment resumes
    private fun EditText.showKeyboard() {
        if (requestFocus()) {
            (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(this, SHOW_IMPLICIT)
            setSelection(text.length)
        }
    }
    override fun onResume() {
        super.onResume()
        bottom_sheet_editText_TaskName.postDelayed({ bottom_sheet_editText_TaskName.showKeyboard()}, 30)
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
