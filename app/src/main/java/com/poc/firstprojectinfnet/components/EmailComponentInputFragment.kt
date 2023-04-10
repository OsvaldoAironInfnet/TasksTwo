package com.poc.firstprojectinfnet.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poc.commom.base.isValidEmail
import com.poc.firstprojectinfnet.databinding.FragmentEmailComponentInputBinding

class EmailComponentInputFragment : Fragment() {

    private var binding: FragmentEmailComponentInputBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailComponentInputBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    fun getEmailValid(): String? {
        if (binding?.edtUsername?.editText?.text.isValidEmail()) {
            return binding?.edtUsername?.editText?.text.toString()
        }
        return null
    }
}