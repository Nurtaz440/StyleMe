package com.styleme.app.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.styleme.app.BuildConfig
import com.styleme.app.databinding.FragmentSettingsBinding
import com.styleme.app.utils.MockRepository
import com.styleme.app.utils.toast
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val PREFS = "styleme_settings"
        private const val KEY_USERS_URL   = "users_api_url"
        private const val KEY_PICTURES_URL = "pictures_api_url"
        private const val KEY_DEMO_MODE   = "demo_mode"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        // Load saved values (fall back to BuildConfig defaults)


        binding.etUsersUrl.setText(
            prefs.getString(KEY_USERS_URL, BuildConfig.BASE_URL_USERS)
        )
        binding.etPicturesUrl.setText(
            prefs.getString(KEY_PICTURES_URL, BuildConfig.BASE_URL_PICTURES)
        )
        binding.switchDemoMode.isChecked = prefs.getBoolean(KEY_DEMO_MODE, false).also {
            MockRepository.enabled = it
        }

        binding.switchDemoMode.setOnCheckedChangeListener { _, isChecked ->
            MockRepository.enabled = isChecked
            prefs.edit().putBoolean(KEY_DEMO_MODE, isChecked).apply()
            updateUrlFieldsEnabled(!isChecked)
            if (isChecked)
                toast("Demo mode ON — using fake data, no backend needed")
            else
                toast("Demo mode OFF — connecting to real backend")
        }

        binding.btnSave.setOnClickListener {
            val usersUrl   = binding.etUsersUrl.text.toString().trim()
            val picturesUrl = binding.etPicturesUrl.text.toString().trim()

            if (usersUrl.isEmpty() || picturesUrl.isEmpty()) {
                toast("Please fill in both URLs"); return@setOnClickListener
            }

            prefs.edit()
                .putString(KEY_USERS_URL, usersUrl)
                .putString(KEY_PICTURES_URL, picturesUrl)
                .apply()

            toast("Settings saved. Restart the app to apply new URLs.")
        }

        binding.btnReset.setOnClickListener {
            binding.etUsersUrl.setText(BuildConfig.BASE_URL_USERS)
            binding.etPicturesUrl.setText(BuildConfig.BASE_URL_PICTURES)
            prefs.edit()
                .putString(KEY_USERS_URL, BuildConfig.BASE_URL_USERS)
                .putString(KEY_PICTURES_URL, BuildConfig.BASE_URL_PICTURES)
                .apply()
            toast("URLs reset to defaults")
        }

        updateUrlFieldsEnabled(!MockRepository.enabled)
    }

    private fun updateUrlFieldsEnabled(enabled: Boolean) {
        binding.etUsersUrl.isEnabled    = enabled
        binding.etPicturesUrl.isEnabled = enabled
        binding.btnSave.isEnabled       = enabled
        binding.btnReset.isEnabled      = enabled
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
