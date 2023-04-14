package com.poc.firstprojectinfnet.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.poc.commom.base.safeLet
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.components.ButtonPrimaryComponentFragment
import com.poc.firstprojectinfnet.databinding.FragmentHomeAddTaskBinding
import org.koin.android.ext.android.inject

class HomeAddTaskFragment : Fragment() {

    private var binding: FragmentHomeAddTaskBinding? = null
    private val homeViewModel: HomeViewModel by inject()

    private val buttonSaveTask by lazy {
        ButtonPrimaryComponentFragment(onClickButton = {
            validateFieldsAndCreateTask()
        }, textButton = requireContext().getString(R.string.btn_save_task))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeAddTaskBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideStatusHomeActivity()
        observeAction()
        setupOptionsSpinner()
        setupComponents()
    }

    private fun observeAction() = with(homeViewModel) {
        action.observe(viewLifecycleOwner) { action ->
            when (action) {
                HomeAction.RedirectToHome -> {
                    showMessage(HomeAction.RedirectToHome.message)
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    private fun validateFieldsAndCreateTask() {
        binding?.edtTitle?.text.toString()
        binding?.edtDescription?.text.toString()
        binding?.edtDate?.text.toString()
        binding?.edtHour?.text.toString()
        safeLet(
            binding?.edtTitle?.text.toString(),
            binding?.edtDescription?.text.toString(),
            binding?.edtDate?.text.toString(),
            binding?.edtHour?.text.toString(),
            result = { title, description, date, hour ->
                val dateFieldValidated = validateFieldDate(date)
                if (!dateFieldValidated) {
                    showMessage("Preencha o campo data corretamente!")
                } else {
                    homeViewModel.createTask(title, description, binding?.checkBoxFav!!.isChecked, date, hour, binding?.spinner?.selectedItem.toString())
                }
            },
            failure = {
                showMessage("Preencha os campos corretamente!")
            })
    }

    private fun validateFieldDate(date: String): Boolean {
        if (date.split("/").size >= 2) {
            return true
        }
        return false
    }

    private fun showMessage(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupOptionsSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            org.koin.android.R.layout.support_simple_spinner_dropdown_item,
            arrayOf("Baixa", "Média", "Alta")
        )
        binding?.spinner?.adapter = adapter
    }

    private fun hideStatusHomeActivity() {
        (activity as HomeActivity).let {
            it.hideToolbar()
            it.hideFloactingButton()
            it.hideNavigationView()
        }
    }

    private fun setupComponents() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.btn_save, buttonSaveTask, "BTN_SAVE_TASK").commit()
    }
}