package com.poc.firstprojectinfnet.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.poc.firstprojectinfnet.databinding.FragmentHomeListBinding
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.presentation.list.ItemTaskAdapter
import com.poc.firstprojectinfnet.home.presentation.list.OnRecyclerViewDataSetChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeListFragment : Fragment() {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    private var _binding: FragmentHomeListBinding? = null

    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeListBinding.inflate(inflater, container, false)

        populateView()

        homeViewModel.listAllTasks()


        return _binding?.root

    }

    private fun populateView() {
        _binding?.recyclerView?.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            val itemTaskAdapter =
                ItemTaskAdapter(_binding?.recyclerView!!, object : OnRecyclerViewDataSetChanged {
                    override fun onDataSetChanged(position: Int) {}
                    override fun onItemRemoved(task: Task?) {
                        homeViewModel.deleteItem(task)
                    }
                })

            recyclerView.adapter = itemTaskAdapter
            uiScope.launch {
                homeViewModel.viewState.observe(viewLifecycleOwner) { stateTask ->
                    if (stateTask.listFavorite) {
                        itemTaskAdapter.clear()
                    }
                    stateTask.dataSetRecoveryLocal?.let {
                        itemTaskAdapter.clear()
                        it.forEach { tsk ->
                            if (tsk.title != "") {
                                itemTaskAdapter.addItem(tsk)
                            }
                        }
                        stateTask.dataSetRecoveryLocal = null
                    }
                    stateTask.dataSetChangedExecution?.let { tsk ->
                        if (tsk.title != "") {
                            itemTaskAdapter.addItem(tsk)
                        }
                    }
                }
            }
        }

    }

}