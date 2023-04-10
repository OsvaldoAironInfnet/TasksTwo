package com.poc.firstprojectinfnet.components

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.databinding.FragmentLoginBinding
import com.poc.firstprojectinfnet.databinding.FragmentPasswordComponentInputBinding

class PasswordComponentInputFragment(
    private val hintPassword: String? = null
) : Fragment() {

    private var binding: FragmentPasswordComponentInputBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordComponentInputBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hintPassword?.let {
            binding?.edtPassword?.hint = it
        }
    }

    fun getPasswordText() = binding?.edtPassword?.editText?.text.toString()

}