package com.dicoding.eventproject.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.eventproject.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        settingsViewModel = SettingsViewModelFactory(pref).create(SettingsViewModel::class.java)

        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkMode ->
            binding.switchTheme.isChecked = isDarkMode
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            Log.d("SettingsFragment", "Switch checked: $isChecked")
            settingsViewModel.saveThemeSetting(isChecked) // Save the new theme setting
        }

        return binding.root
    }
}
