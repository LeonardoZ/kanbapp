package com.github.leonardoz.kanbapp.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.databinding.FragmentManageColumnsBinding
import com.github.leonardoz.kanbapp.util.format
import com.github.leonardoz.kanbapp.view.adapter.ManageColumnsAdapter
import com.github.leonardoz.kanbapp.view.adapter.dragndrop.SimpleItemTouchHelperCallback
import com.github.leonardoz.kanbapp.view.dialog.ChangeColumnDialog
import com.github.leonardoz.kanbapp.view.fragment.BoardsListFragment.Companion.CHANGE_DIALOG_KEY
import com.github.leonardoz.kanbapp.view.viewmodel.ManageColumnsViewModel
import kotlinx.android.synthetic.main.fragment_boards_list.*
import kotlinx.android.synthetic.main.fragment_manage_columns.*
import javax.inject.Inject


class ManageColumnsFragment : Fragment(), ManageColumnsAdapter.CardCallbacks {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ManageColumnsViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var columnsAdapter: ManageColumnsAdapter
    private var boardId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectManageColumnsDialog(this)

        arguments?.let {
            boardId = ManageColumnsFragmentArgs.fromBundle(it).boardId.toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_manage_columns, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireNotNull(boardId)
        initViewModel()
        initRecycler()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ManageColumnsViewModel::class.java)

        viewModel.afterOk.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
        })

        viewModel.canceled.observe(viewLifecycleOwner, Observer {
            dialog.dismiss()
        })

        viewModel.columns.observe(viewLifecycleOwner, Observer { columns ->
            columnsAdapter.updateField(columns.toMutableList())
        })

        viewModel.boardId.postValue(boardId)
    }

    private fun initRecycler() {
        columnsAdapter = ManageColumnsAdapter(this)
        columnsAdapter.setHasStableIds(true)
        val linearLayoutManager = LinearLayoutManager(context)

        manageColumnsRecyclerView.apply {
            adapter = columnsAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }

        val callback = SimpleItemTouchHelperCallback(columnsAdapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(manageColumnsRecyclerView)
    }

    override fun onEditClicked(column: Column) {
        val changeColumn = ChangeColumnDialog()

        val args = Bundle()
        args.putLong(ChangeColumnDialog.ID_PARAM, column.id)
        args.putLong(ChangeColumnDialog.BOARD_ID_PARAM, column.boardId)
        changeColumn.arguments = args

        val ft = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag(CHANGE_DIALOG_KEY)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        changeColumn.show(ft, CHANGE_DIALOG_KEY)
        true
    }


    override fun onDestroy() {
        manageColumnsRecyclerView?.adapter = null
        super.onDestroy()
    }

    override fun onRemoveClicked(column: Column) {
        AlertDialog.Builder(context)
            .setTitle(format(getString(R.string.remove_column_ask), column.title))
            .setPositiveButton(R.string.remove_column) { _: DialogInterface, i: Int ->
                viewModel.onRemove(column)
            }
            .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, _ ->
                dialogInterface.dismiss()
            }.show()
    }


    companion object {
        const val CHANGE_DIALOG_KEY = "CHANGE_DIALOG_KEY"
    }

    override fun onReorder(columnA: Column, columnB: Column) {
        viewModel.onReorder(columnA, columnB)
    }

}