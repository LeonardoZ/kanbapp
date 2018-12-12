package com.github.leonardoz.kanbapp.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.github.leonardoz.kanbapp.KanbappApplication
import com.github.leonardoz.kanbapp.R
import com.github.leonardoz.kanbapp.databinding.FragmentCreateBoardBinding
import com.github.leonardoz.kanbapp.view.viewmodel.CreateBoardViewModel
import kotlinx.android.synthetic.main.fragment_create_board.*
import javax.inject.Inject

class CreateBoardFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val navigationObserver = Observer<Any> {
        activity?.let { activity ->
            Navigation.findNavController(activity, R.id.nav_host_fragment)
                .navigate(R.id.action_createBoardFragment_to_boardsListFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.applicationContext as KanbappApplication)
            .appComponent
            .injectCreateBoardFragment(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etName.requestFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCreateBoardBinding.inflate(inflater, container, false)
            .apply {
                viewModel = ViewModelProviders.of(this@CreateBoardFragment, viewModelFactory)
                    .get(CreateBoardViewModel::class.java)
                viewModel!!.navigateToBoardsFragment.observe(viewLifecycleOwner, navigationObserver)
                setLifecycleOwner(this@CreateBoardFragment)
            }.root
    }

}
