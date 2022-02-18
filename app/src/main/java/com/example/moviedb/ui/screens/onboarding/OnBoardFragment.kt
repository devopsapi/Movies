package com.example.moviedb.ui.screens.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.moviedb.databinding.FragmentOnBoardBinding
import com.example.moviedb.utils.Credentials.PREFERENCES_KEY

class OnBoardFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkIfOnBoardFinished()) {
            findNavController().navigate(OnBoardFragmentDirections.actionOnBoardToHome())
        }

        binding.getStartedBtn?.setOnClickListener {
            this.findNavController()
                .navigate(OnBoardFragmentDirections.actionOnBoardToHome())
            onBoardingFinished()
        }
    }

    private fun checkIfOnBoardFinished(): Boolean {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return false
        return sharedPref.getBoolean(PREFERENCES_KEY, false)
    }

    private fun onBoardingFinished() {
        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            putBoolean(PREFERENCES_KEY, true)
            apply()
        }
    }
}