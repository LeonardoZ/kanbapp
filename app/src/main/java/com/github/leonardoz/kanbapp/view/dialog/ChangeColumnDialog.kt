package com.github.leonardoz.kanbapp.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.databinding.DialogChangeColumnBinding
import com.github.leonardoz.kanbapp.view.viewmodel.ChangeColumnViewModel
import javax.inject.Inject

class ChangeColumnDialog : DialogFragment() {

    companion object {
        const val ID_PARAM = "ID_PARAM"
        const val BOARD_ID_PARAM = "BOARD_ID_PARAM"
    }

    private lateinit var binding: DialogChangeColumnBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var dialog: AlertDialog

    private var columnId: Long? = 0L
    private var boardId: Long? = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectChangeColumnDialog(this)
        columnId = arguments?.getLong(ID_PARAM)
        boardId = arguments?.getLong(BOARD_ID_PARAM)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        requireNotNull(columnId)
        requireNotNull(boardId)

        binding = DialogChangeColumnBinding.inflate(LayoutInflater.from(activity))

        binding.viewModel = ViewModelProviders.of(this.activity!!, viewModelFactory)
            .get(ChangeColumnViewModel::class.java)

        binding.viewModel?.let {

            it.afterColumnChanged.observe(this, Observer {
                dialog.dismiss()
            })

            it.cancelColumnChanged.observe(this, Observer {
                dialog.dismiss()
            })
            it.column.observe(this, Observer { column ->
                dialog.setTitle(getString(R.string.change_column) + " " + column?.title)
            })
            it.boardAndColumnIds.postValue(Pair(boardId!!, columnId!!))
            it.setToInitialState()
        }
        binding.setLifecycleOwner(this)

        dialog = AlertDialog.Builder(activity as Context)
            .setTitle(getString(R.string.change_column))
            .setView(binding.root)
            .create()
        return dialog
    }

}