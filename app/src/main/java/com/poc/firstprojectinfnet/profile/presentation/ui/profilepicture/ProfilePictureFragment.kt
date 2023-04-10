package com.poc.firstprojectinfnet.profile.presentation.ui.profilepicture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.poc.firstprojectinfnet.R
import com.poc.firstprojectinfnet.components.ButtonPrimaryComponentFragment
import com.poc.firstprojectinfnet.databinding.FragmentProfilePictureBinding
import com.poc.firstprojectinfnet.home.presentation.HomeActivity
import com.poc.firstprojectinfnet.home.presentation.HomeOnBackPressed
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureAction
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureState
import com.poc.firstprojectinfnet.profile.presentation.ui.ProfilePictureViewModel
import org.koin.android.ext.android.inject

class ProfilePictureFragment : Fragment(), HomeOnBackPressed {

    private var _binding: FragmentProfilePictureBinding? = null
    private val profilePictureViewModel: ProfilePictureViewModel by inject()

    private val btnUpdateProfile by lazy {
        ButtonPrimaryComponentFragment(
            onClickButton = {
                onBtnUpdateProfileAction()
            },
            textButton = requireContext().getString(R.string.btn_update_profile)
        )
    }


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideStatusHomeActivity()

        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.btn_update_profile,
            btnUpdateProfile,
            "BTN_UPDATE_PROFILE_FRAGMENT"
        )?.commit()

        observeAction()
        observeState()

        hideStatusProfileSettings()

        profilePictureViewModel.recoveryProfileImage()
        profilePictureViewModel.recoveryProfileSettings()

    }


    override fun onResume() {
        super.onResume()
        profilePictureViewModel.recoveryProfileImage()
        profilePictureViewModel.recoveryProfileSettings()
    }

    private fun hideStatusProfileSettings() {
        _binding?.txtEmail?.visibility = View.GONE
        _binding?.txtName?.visibility = View.GONE
        _binding?.iconEmail?.visibility = View.GONE
    }

    private fun hideStatusHomeActivity() {
        (activity as HomeActivity).let {
            it.hideToolbar()
            it.hideFloactingButton()
            it.hideNavigationView()
        }
    }

    private fun onBtnUpdateProfileAction() {
        profilePictureViewModel.startAProfileActivity()
    }

    private fun observeState() {
        profilePictureViewModel.state.observe(viewLifecycleOwner) { state ->
            setupProfileSettings(state)
        }
    }


    private fun observeAction() {
        profilePictureViewModel.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                ProfilePictureAction.StartProfileActivity -> {
                    startAProfileActivity()
                }

                ProfilePictureAction.ShowProfileImage -> {
                    val image = ProfilePictureAction.ShowProfileImage.image
                    showProfileImage(image)
                }

                else -> Unit
            }
        }
    }

    private fun setupProfileSettings(profilePictureState: ProfilePictureState) {
        profilePictureState.apply {
            this.profilePictureState.email?.let {
                _binding?.txtEmail?.text = it
                _binding?.txtEmail?.visibility = View.VISIBLE
                _binding?.iconEmail?.visibility = View.VISIBLE
            }

            this.profilePictureState.name.let {
                _binding?.txtName?.text = it
                _binding?.txtName?.visibility = View.VISIBLE
            }
        }
    }

    private fun showProfileImage(image: Uri?) {
        _binding?.imageView?.let {
            Glide
                .with(this@ProfilePictureFragment)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(it)
        }
    }

    private fun startAProfileActivity() {
        val intent = Intent(requireContext(), ProfilePictureActivity::class.java)
        startActivity(intent)
    }

    fun hideRootView() {
        _binding?.root?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed(): Boolean {
        _binding?.root?.visibility = View.GONE
        return true
    }

}