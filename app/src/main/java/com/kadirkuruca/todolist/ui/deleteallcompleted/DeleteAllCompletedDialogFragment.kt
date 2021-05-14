package com.kadirkuruca.todolist.ui.deleteallcompleted

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment : DialogFragment(){

    private val viewModel : DeleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Do you really want to delete all completed tasks?")
            .setNegativeButton("Cancel",null)
            .setPositiveButton("Yes"){_ , _ ->
                viewModel.onConfirmClick()
            }
            .create()
    }
}