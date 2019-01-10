package com.github.leonardoz.kanbapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.dummy.DummyContent
import com.github.leonardoz.kanbapp.dummy.DummyContent.DummyItem
import com.github.leonardoz.kanbapp.view.adapter.CardRecyclerViewAdapter
import com.github.leonardoz.kanbapp.view.viewmodel.ColumnViewModel
import kotlinx.android.synthetic.main.fragment_cards_list.*
import javax.inject.Inject

class ColumnFragment : Fragment() {

    companion object {
        const val BOARD_ID = "BOARD_ID"
        const val COLUMN_ID = "COLUMN_ID"

        fun newInstance(boardId: Long, columnId: Long): ColumnFragment {
            val bundle = Bundle()
            bundle.putLong(BOARD_ID, boardId)
            bundle.putLong(COLUMN_ID, columnId)

            val columnFragment = ColumnFragment()
            columnFragment.arguments = bundle
            return columnFragment
        }
    }


    private lateinit var viewModel: ColumnViewModel
    private lateinit var adapter: CardRecyclerViewAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var listener: OnListFragmentInteractionListener? = object : OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: DummyItem?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectColumnFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cards_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initViewModel()
    }

    private fun initAdapter() {
        cardsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CardRecyclerViewAdapter(DummyContent.ITEMS, listener)
        cardsRecyclerView.adapter = adapter
    }

    private fun initViewModel() {
        arguments?.let {
            val boardId = it.getLong(BOARD_ID)
            val columnId = it.getLong(COLUMN_ID)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ColumnViewModel::class.java)
            viewModel.column?.observe(viewLifecycleOwner, Observer {
                // todo
            })
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

}
