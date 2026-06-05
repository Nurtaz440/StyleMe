package com.styleme.app.ui.haircolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.styleme.app.databinding.FragmentHairColourBinding
import com.styleme.app.models.HairColour
import com.styleme.app.utils.Resource
import com.styleme.app.utils.gone
import com.styleme.app.utils.toast
import com.styleme.app.utils.visible
import com.styleme.app.viewmodels.HairColourViewModel
import com.styleme.app.viewmodels.HomeViewModel

class HairColourFragment : Fragment() {

    private var _binding: FragmentHairColourBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HairColourViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: HairColourAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHairColourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HairColourAdapter { colour -> applyColour(colour) }
        binding.rvColours.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvColours.adapter = adapter

        viewModel.loadColours()

        viewModel.colours.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> { binding.progressBar.gone(); adapter.submitList(state.data) }
                is Resource.Error   -> { binding.progressBar.gone(); toast(state.message) }
            }
        }

        viewModel.changeResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.tvStatus.text = "Applying colour… this may take a moment"
                    binding.tvStatus.visible()
                }
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.tvStatus.gone()
                    state.data?.let { binding.ivResult.setImageBitmap(it); binding.ivResult.visible() }
                    viewModel.updatedPictureId.value?.let {
                        homeViewModel.currentPictureId.value = it
                        homeViewModel.cachedBitmap = state.data
                    }
                    toast("Hair colour applied!")
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.tvStatus.gone()
                    toast(state.message)
                }
            }
        }
    }

    private fun applyColour(colour: HairColour) {
        val pid = homeViewModel.currentPictureId.value ?: run {
            toast("No photo loaded. Go back and upload a photo first."); return
        }
        viewModel.changeHairColour(pid, colour)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
