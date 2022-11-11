package com.othadd.hngmobiletask3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.othadd.hngmobiletask3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val sharedViewModel: ExploreViewModel by activityViewModels { ExploreViewModelFactory() }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            homeFragment = this@HomeFragment
        }

        return binding.root
    }
}