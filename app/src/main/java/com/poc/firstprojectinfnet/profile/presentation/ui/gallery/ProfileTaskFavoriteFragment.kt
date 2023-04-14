package com.poc.firstprojectinfnet.profile.presentation.ui.gallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.poc.commom.base.views.BaseActivity
import com.poc.firstprojectinfnet.databinding.FragmentGalleryBinding
import com.poc.firstprojectinfnet.home.data.Task
import com.poc.firstprojectinfnet.home.navigation.RedirectHomeFlowEnum
import com.poc.firstprojectinfnet.home.presentation.HomeAction
import com.poc.firstprojectinfnet.home.presentation.HomeViewModel
import com.poc.firstprojectinfnet.home.presentation.list.ItemTaskAdapter
import com.poc.firstprojectinfnet.home.presentation.list.OnRecyclerViewDataSetChanged
import com.poc.firstprojectinfnet.profile.data.model.TasksProfile
import org.koin.android.ext.android.inject

class ProfileTaskFavoriteFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val homeViewModel: HomeViewModel by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observeAction() = with(homeViewModel) {
        action.observe(viewLifecycleOwner) { actionValue ->
            when (actionValue) {
                HomeAction.RedirectToDetail -> {
                    val task = HomeAction.RedirectToDetail.detailTask
                    (requireActivity() as BaseActivity).apply {
                        navigationScreen?.navigate(
                            RedirectHomeFlowEnum.HOME_DETAIL_FRAGMENT.navigationScreen
                                .apply {
                                    bundle = Bundle().apply { putSerializable("taskArg", task) }
                                })
                    }
                    action.value = null
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateView()
        homeViewModel.recoveryFavoriteTasks()
        observeAction()
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
                    stateTask.tasksProfile?.let {
                        itemTaskAdapter.clear()
                        it.favoriteDatasetTask?.forEach { tsk ->
                            if (tsk.title != "") {
                                itemTaskAdapter.addItem(tsk)
                            }
                        }
                        stateTask.tasksProfile.favoriteDatasetTask = null
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}