package com.poc.firstprojectinfnet.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poc.firstprojectinfnet.databinding.FragmentButtonSaveComponentBinding


class ButtonPrimaryComponentFragment(
    private val onClickButton: () -> Unit,
    private val textButton: String
) : Fragment() {


    private var _binding: FragmentButtonSaveComponentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentButtonSaveComponentBinding.inflate(inflater)

        _binding?.btnLogin?.setOnClickListener {
            onClickButton.invoke()
        }

        _binding?.btnLogin?.text = textButton

        return _binding?.root
    }


    companion object {
        fun newInstance(onClickButton: () -> Unit, textButton: String) =
            ButtonPrimaryComponentFragment(onClickButton, textButton)
    }

}