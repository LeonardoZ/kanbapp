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
import com.github.leonardoz.kanbapp.databinding.DialogCreateColumnBinding
import com.github.leonardoz.kanbapp.view.viewmodel.CreateColumnViewModel
import javax.inject.Inject

class CreateColumnDialog : DialogFragment() {

    companion object {
        const val BOARD_ID_PARAM = "BOARD_ID_PARAM"
    }

    private lateinit var binding: DialogCreateColumnBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var dialog: AlertDialog

    private var boardId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectCreateColumnDialog(this)
        boardId = arguments?.getLong(BOARD_ID_PARAM)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        requireNotNull(boardId)
        binding = DialogCreateColumnBinding.inflate(LayoutInflater.from(activity))

        binding.viewModel = ViewModelProviders.of(this.activity!!, viewModelFactory)
            .get(CreateColumnViewModel::class.java)

        binding.viewModel?.let {
            it.boardId = boardId!!
            it.afterColumnCreated.observe(this, Observer {
                dialog.dismiss()
            })
            it.cancelColumnCreation.observe(this, Observer {
                dialog.dismiss()
            })
            it.setToInitialState()
        }
        binding.setLifecycleOwner(this)

        dialog = AlertDialog.Builder(activity as Context)
            .setTitle(getString(R.string.create_column))
            .setView(binding.root)
            .create()
        return dialog
    }

}