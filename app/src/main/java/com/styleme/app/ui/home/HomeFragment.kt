package com.styleme.app.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.styleme.app.R
import com.styleme.app.databinding.FragmentHomeBinding
import com.styleme.app.utils.Resource
import com.styleme.app.utils.gone
import com.styleme.app.utils.toast
import com.styleme.app.utils.visible
import com.styleme.app.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            result.data?.data?.let { viewModel.uploadPicture(it) }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) openGallery()
        else toast("Storage permission is needed to select a photo")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadLatestHistory()
// ADD TEMPORARILY - remove after first run
        viewModel.seedFirestore()
        binding.btnUploadPhoto.setOnClickListener { checkPermissionAndOpenGallery() }

        binding.btnChangeColour.setOnClickListener {
            if (viewModel.currentPictureId.value == null) toast("Upload a photo first")
            else findNavController().navigate(R.id.action_home_to_hairColour)
        }

        binding.btnChangeStyle.setOnClickListener {
            if (viewModel.currentPictureId.value == null) toast("Upload a photo first")
            else findNavController().navigate(R.id.action_home_to_hairStyle)
        }

        binding.btnDiscard.setOnClickListener { viewModel.discardChanges() }

        // Upload state
        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvStatus.text = "Uploading & analysing your photo…"
                    binding.tvStatus.visible()
                    binding.btnDiscard.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val shape = state.data.faceShape
                    binding.tvStatus.text = if (shape != null)
                        "✨ Face shape detected: ${shape.label ?: shape.name}"
                    else "Photo uploaded! Choose a colour or style change."
                    binding.btnDiscard.gone()
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.tvStatus.text = state.message
                    toast(state.message)
                }
            }
        }

        // Bitmap display
        viewModel.currentBitmap.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    state.data?.let {
                        binding.ivPhoto.setImageBitmap(it)
                        binding.ivPhoto.visible()
                        binding.tvPlaceholder.gone()
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        // Discard result
        viewModel.discardResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnDiscard.gone()
                    binding.tvStatus.text = "Reverted to original photo"
                    toast("Changes discarded")
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        // Show discard button whenever current ≠ original
        viewModel.currentPictureId.observe(viewLifecycleOwner) { currentId ->
            val origId = viewModel.originalPictureId.value
            if (currentId != null && origId != null && currentId != origId)
                binding.btnDiscard.visible()
        }
    }

    private fun checkPermissionAndOpenGallery() {
        val perm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(requireContext(), perm) ==
            PackageManager.PERMISSION_GRANTED) openGallery()
        else permissionLauncher.launch(perm)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
