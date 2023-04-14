package com.poc.firstprojectinfnet.home.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.databinding.FragmentHomeListBinding
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.navigation.RedirectHomeFlowEnum
import com.poc.firstprojectinfnet.home.presentation.list.ItemTaskAdapter
import com.poc.firstprojectinfnet.home.presentation.list.OnRecyclerViewDataSetChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeListFragment : Fragment() {

    private var _binding: FragmentHomeListBinding? = null
    private val homeViewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeListBinding.inflate(inflater, container, false)
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateView()
        _binding?.recyclerView?.visibility = View.GONE
        _binding?.progressBar?.visibility = View.VISIBLE
        observeAction()
    }

    private fun observeAction() = with(homeViewModel) {
        action.observe(viewLifecycleOwner) { actionValue ->
            when (actionValue) {
                HomeAction.RedirectToDetail -> {
                    val task = HomeAction.RedirectToDetail.detailTask
                    (requireActivity() as BaseActivity).apply {
                        navigationScreen?.navigate(RedirectHomeFlowEnum.HOME_DETAIL_FRAGMENT.navigationScreen
                            .apply {
                                bundle = Bundle().apply { putSerializable("taskArg", task) }
                            })
                    }
                    action.value = null
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.recoveryAllTasks()
    }

    private fun hideProgressBar() {
        _binding?.recyclerView?.visibility = View.VISIBLE
        _binding?.progressBar?.visibility = View.GONE
    }

    private fun populateView() {
        homeViewModel.recoveryProfilePicture(onResult = {
            hideProgressBar()
            setupRecyclerView(it)
        }, onFailure = {
            hideProgressBar()
            setupRecyclerView()
        })
    }

    private fun setupRecyclerView(uri: Uri? = null) {
        _binding?.recyclerView?.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.itemAnimator = DefaultItemAnimator()
            val itemTaskAdapter =
                ItemTaskAdapter(
                    requireContext(),
                    _binding?.recyclerView!!,
                    uri,
                    onLongClick = { task, view ->
                        homeViewModel.redirectToDetailPage(task, view)
                    },
                    object : OnRecyclerViewDataSetChanged {
                        override fun onDataSetChanged(position: Int) {}
                        override fun onItemRemoved(task: Task?) {
                            homeViewModel.deleteItem(task)
                        }
                    })

            recyclerView.adapter = itemTaskAdapter
            if (view != null) {
                homeViewModel.state.observe(viewLifecycleOwner) { stateTask ->
                    if (stateTask.taskViewState?.listFavorite == true) {
                        itemTaskAdapter.clear()
                    }
                    stateTask.taskViewState?.dataSetRecoveryLocal?.let {
                        itemTaskAdapter.clear()
                        it.forEach { tsk ->
                            if (tsk.title != "") {
                                itemTaskAdapter.addItem(tsk)
                            }
                        }
                        stateTask.taskViewState.dataSetRecoveryLocal = null
                    }
                    stateTask.taskViewState?.dataSetChangedExecution?.let { tsk ->
                        if (tsk.title != "") {
                            itemTaskAdapter.addItem(tsk)
                        }
                    }
                }
            }
        }
    }
}