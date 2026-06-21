package com.styleme.app.ui.hairstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.styleme.app.databinding.FragmentHairStyleBinding
import com.styleme.app.models.ModelPicture
import com.styleme.app.utils.Resource
import com.styleme.app.utils.gone
import com.styleme.app.utils.toast
import com.styleme.app.utils.visible
import com.styleme.app.viewmodels.HairStyleViewModel
import com.styleme.app.viewmodels.HomeViewModel

class HairStyleFragment : Fragment() {

    private var _binding: FragmentHairStyleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HairStyleViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: ModelPictureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHairStyleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ModelPictureAdapter(
            onImageNeeded = { id, cb -> viewModel.getModelPictureBitmap(id, cb) },
            onModelClick  = { model -> applyStyle(model) }
        )
        binding.rvModels.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvModels.adapter = adapter

        val prefs = requireContext()
            .getSharedPreferences("StyleMePrefs", android.content.Context.MODE_PRIVATE)
        val userPictureId = homeViewModel.originalPictureId.value?.takeIf { it > 0 }
            ?: prefs.getInt("original_picture_id", 0).takeIf { it > 0 }

        viewModel.loadModelPictures(userPictureId)

        homeViewModel.detectedGender.observe(viewLifecycleOwner) { gender ->
            if (gender.isNullOrBlank() || gender == "unisex") {
                binding.tvGender.gone()
            } else {
                val label = gender.replaceFirstChar { it.uppercase() }
                binding.tvGender.text = "Detected: $label — showing $label styles"
                binding.tvGender.visible()
            }
        }

        viewModel.modelPictures.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    adapter.submitList(state.data)
                    if (state.data.isEmpty()) binding.tvEmpty.visible() else binding.tvEmpty.gone()
                }
                is Resource.Error -> { binding.progressBar.gone(); toast(state.message) }
            }
        }

        viewModel.changeResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvStatus.text = "Applying hair style… please wait"
                    binding.tvStatus.visible()
                    binding.ivResult.gone()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.tvStatus.gone()
                    state.data?.let { bitmap ->
                        binding.ivResult.setImageBitmap(bitmap)
                        binding.ivResult.visible()
                        // Scroll to top to show result
                        binding.root.scrollTo(0, 0)
                    }
                    viewModel.updatedPictureId.value?.let {
                        homeViewModel.currentPictureId.value = it
                        homeViewModel.cachedBitmap = state.data
                    }
                    toast("Hair style applied! ✅")
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.tvStatus.gone()
                    toast(state.message)
                }
            }
        }
    }

    private fun applyStyle(model: ModelPicture) {
        val prefs = requireContext()
            .getSharedPreferences("StyleMePrefs", android.content.Context.MODE_PRIVATE)

        // Always use the ORIGINAL uploaded picture so styles don't stack on each other
        val pid = homeViewModel.originalPictureId.value?.takeIf { it > 0 }
            ?: prefs.getInt("original_picture_id", 0).takeIf { it > 0 }
            ?: homeViewModel.currentPictureId.value?.takeIf { it > 0 }
            ?: prefs.getInt("current_picture_id", 0).takeIf { it > 0 }
            ?: run {
                toast("Please upload a photo first before choosing a style."); return
            }

        viewModel.changeHairStyle(pid, model.id)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
