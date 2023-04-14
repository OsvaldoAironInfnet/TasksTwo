package com.poc.firstprojectinfnet.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.databinding.FragmentHomeTaskDetailBinding
import com.poc.firstprojectinfnet.home.data.Task
import org.koin.android.ext.android.inject

class HomeTaskDetailFragment : Fragment() {

    private var binding: FragmentHomeTaskDetailBinding? = null
    private val homeViewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeTaskDetailBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataTask = arguments?.get("taskArg") as Task
        setupView(dataTask)
    }

    private fun setupView(task: Task) {
        binding?.txtTitle?.text = task.title
        binding?.txtDateExpire?.text = "Data de expiração: ${task.date} - ${task.hour}"
        binding?.txtDescription?.text = task.description
        if (task.favorite) {
            binding?.imageFav?.setImageResource(R.drawable.baseline_star_24)
        } else {
            binding?.imageFav?.setImageResource(R.drawable.baseline_star_border_24)
        }
    }
}