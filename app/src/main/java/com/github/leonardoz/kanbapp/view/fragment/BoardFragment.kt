package com.github.leonardoz.kanbapp.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Column
import com.github.leonardoz.kanbapp.view.adapter.ColumnsAdapter
import com.github.leonardoz.kanbapp.view.dialog.CreateColumnDialog
import com.github.leonardoz.kanbapp.view.viewmodel.BoardViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_board.*
import javax.inject.Inject

class BoardFragment : Fragment() {

    private lateinit var viewModel: BoardViewModel
    private lateinit var adapter: ColumnsAdapter
    private var boardId: Long? = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectBoardFragment(this)

        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            boardId = BoardFragmentArgs.fromBundle(it).boardId.toLong()
        }
        tabLayout.setupWithViewPager(viewpager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewpager.currentItem = it.position
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_board, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initAdapter()
    }

    private fun initAdapter() {
        adapter = ColumnsAdapter(childFragmentManager)
        viewpager.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BoardViewModel::class.java)

        viewModel.columns.observe(viewLifecycleOwner, Observer {
            viewModel.onColumnsLoaded()
            val frags = it.map { column ->
                createColumnFragment(column)
            }
            adapter.dataset = frags
        })

        viewModel.board.observe(viewLifecycleOwner, Observer {
            it?.let { board ->
                (activity as? AppCompatActivity)
                    ?.supportActionBar
                    ?.title = board.name

                Navigation.findNavController(view!!)
                    .currentDestination?.label = board.name
            }
        })

        viewModel.boardId.postValue(boardId)

        viewModel.onAddColumn.observe(viewLifecycleOwner, Observer {
            openCreateColumnDialog(it)
        })

        viewModel.onManageColumns.observe(viewLifecycleOwner, Observer {
            goToManageColumnsFragment(it)
        })
    }

    private fun openCreateColumnDialog(boardId: Long) {
        val createColumnDialog = CreateColumnDialog()

        val bundle = Bundle()
        bundle.putLong(CreateColumnDialog.BOARD_ID_PARAM, boardId)

        createColumnDialog.arguments = bundle

        val ft = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag(CREATE_COLUMN_DIALOG_KEY)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        createColumnDialog.show(ft, CREATE_COLUMN_DIALOG_KEY)
    }

    private fun goToManageColumnsFragment(receivedBoardId: Long?) {
        requireNotNull(receivedBoardId)
        val action = BoardFragmentDirections
            .actionBoardFragmentToManageColumnsFragment(receivedBoardId.toString())

        Navigation.findNavController(view!!).navigate(action)
    }

    companion object {
        const val CREATE_COLUMN_DIALOG_KEY = "CREATE_COLUMN_DIALOG_KEY"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new_column -> viewModel.addColumn()
            R.id.action_manage -> viewModel.reorderWorkColumns()
        }
        return true
    }

    private fun createColumnFragment(column: Column): Pair<String, Fragment> {
        return Pair(column.title, ColumnFragment.newInstance(boardId!!, column.id))
    }
}

