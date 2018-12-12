package com.github.leonardoz.kanbapp.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.databinding.DialogChangeNameBinding
import com.github.leonardoz.kanbapp.view.viewmodel.ChangeNameViewModel
import kotlinx.android.synthetic.main.dialog_change_name.*
import javax.inject.Inject

class ChangeNameDialog : DialogFragment() {

    companion object {
        const val ID_PARAM = "ID_PARAM"
        const val NAME_PARAM = "NAME_PARAM"
    }

    private lateinit var binding: DialogChangeNameBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var dialog: AlertDialog

    private var boardId: Long? = null
    private var boardName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectChangeNameDialog(this)
        boardId = arguments?.getLong(ID_PARAM)
        boardName = arguments?.getString(NAME_PARAM)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        binding = DialogChangeNameBinding.inflate(LayoutInflater.from(activity))

        binding.viewModel = ViewModelProviders.of(this.activity!!, viewModelFactory)
            .get(ChangeNameViewModel::class.java)

        binding.viewModel?.boardIdToBeChanged = boardId

        binding.viewModel?.let {
            it.afterNameChange.observe(this, Observer {
                dialog.dismiss()
            })

            it.cancelNameChange.observe(this, Observer {
                dialog.dismiss()
            })
            it.setToInitialState()
        }
        binding.setLifecycleOwner(this)

        dialog = AlertDialog.Builder(activity as Context)
            .setTitle(getString(R.string.change) + " " + boardName)
            .setView(binding.root)
            .create()
        return dialog
    }

}