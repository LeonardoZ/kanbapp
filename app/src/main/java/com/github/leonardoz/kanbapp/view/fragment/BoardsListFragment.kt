package com.github.leonardoz.kanbapp.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.databinding.FragmentBoardsListBinding
import com.github.leonardoz.kanbapp.util.format
import com.github.leonardoz.kanbapp.view.adapter.BoardsAdapter
import com.github.leonardoz.kanbapp.view.dialog.ChangeNameDialog
import com.github.leonardoz.kanbapp.view.viewmodel.BoardsViewModel
import kotlinx.android.synthetic.main.fragment_boards_list.*
import javax.inject.Inject


class BoardsListFragment : Fragment() {

    private lateinit var binding: FragmentBoardsListBinding
    private lateinit var viewModel: BoardsViewModel
    private lateinit var boardsAdapter: BoardsAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val navigationObserver = Observer<Any> {
        activity?.let { activity ->
            Navigation.findNavController(activity, R.id.nav_host_fragment)
                .navigate(R.id.action_boardsListFragment_to_createBoardFragment)
        }
    }

    private val boardRemovedObserver = Observer<Board> { board ->
        activity?.let { activity ->
            format(activity.getString(R.string.x_removed), board.name)
            Toast.makeText(activity, "", Toast.LENGTH_SHORT)
        }
    }

    private val boardsObserver = Observer<List<Board>> {

        boardsAdapter.dataset = it

        if (it.isEmpty()) {
            boardsRecyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            boardsRecyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectBoardsListFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoardsListBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initRecycler()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BoardsViewModel::class.java)

        binding.let {
            it.boardsViewModel = viewModel

            viewModel.boards.observe(viewLifecycleOwner, boardsObserver)
            viewModel.navigateToCreateBoardFragment.observe(viewLifecycleOwner, navigationObserver)
            viewModel.informBoardWasRemoved.observe(viewLifecycleOwner, boardRemovedObserver)
        }
    }

    private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        boardsAdapter = BoardsAdapter(
            ::menuCallback,
            ::openBoardCallback
        )
        boardsRecyclerView.apply {
            adapter = boardsAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        boardsRecyclerView?.adapter = null
        super.onDestroy()
    }

    private fun menuCallback(item: MenuItem?, boardPosition: Int) {
        when (item?.itemId) {
            R.id.menu_change_name -> menuChangeNameClicked(boardPosition)
            R.id.menu_remove -> removeBoard(boardPosition)
            else -> false
        }
    }

    private fun removeBoard(boardPosition: Int) {
        val board = boardsAdapter.dataset[boardPosition]

        AlertDialog.Builder(context)
            .setTitle(format(getString(R.string.remove_board_ask), board.name))
            .setPositiveButton(R.string.remove) { _: DialogInterface, i: Int ->
                viewModel.removeBoard(board)
            }
            .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, _ ->
                dialogInterface.dismiss()
            }.show()

    }

    private fun menuChangeNameClicked(boardPosition: Int) {
        val changeNameDialog = ChangeNameDialog()

        val args = Bundle()
        val (id, name) = boardsAdapter.dataset[boardPosition]
        args.putLong(ChangeNameDialog.ID_PARAM, id)
        args.putString(ChangeNameDialog.NAME_PARAM, name)
        changeNameDialog.arguments = args

        val ft = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag(CHANGE_DIALOG_KEY)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        changeNameDialog.show(ft, CHANGE_DIALOG_KEY)
        true
    }

    private fun openBoardCallback(board: Board) {
        activity?.let {
            val action = BoardsListFragmentDirections
                .actionBoardsListFragmentToBoardFragment(board.id.toString())
            Navigation.findNavController(view!!).navigate(action)
        }
    }

    companion object {
        const val CHANGE_DIALOG_KEY = "CHANGE_DIALOG_KEY"
    }

}
