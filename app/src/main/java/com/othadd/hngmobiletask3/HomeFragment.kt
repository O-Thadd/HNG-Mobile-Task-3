package com.othadd.hngmobiletask3

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.othadd.hngmobiletask3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val sharedViewModel: ExploreViewModel by activityViewModels { ExploreViewModelFactory() }
    private lateinit var binding: FragmentHomeBinding

    private lateinit var languagesDialog: ConstraintLayout
    private lateinit var dialogOverlay: View
    private lateinit var backPressedCallback: OnBackPressedCallback
    private lateinit var englishRadioButton: ImageView
    private lateinit var germanRadioButton: ImageView
    private lateinit var frenchRadioButton: ImageView
    private lateinit var italianRadioButton: ImageView
    private lateinit var spanishRadioButton: ImageView
    private lateinit var languagesIndicator: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        backPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            doNothing()
        }
        backPressedCallback.isEnabled = false
    }

    fun doNothing() {
        // do nothing
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        languagesDialog = binding.languageDialogConstraintLayout
        dialogOverlay = binding.dialogOverlayView

        binding.apply {
            languagesDialog = languageDialogConstraintLayout
            dialogOverlay = dialogOverlayView
            englishRadioButton = englishRadioButtonImageView
            germanRadioButton = germanRadioButtonImageView
            frenchRadioButton = frenchRadioButtonImageView
            italianRadioButton = italianRadioButtonImageView
            spanishRadioButton = spanishRadioButtonImageView
            languagesIndicator = languageIndicatorTextView
        }

        val adapter = CountryRecyclerAdapter{
            sharedViewModel.setSelectedCountry(it)
            navigateToDetailsFragment()
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            homeFragment = this@HomeFragment
            countryRecyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEditText.addTextChangedListener {
            sharedViewModel.searchCountries(it.toString())
        }

        sharedViewModel.languageTag.observe(viewLifecycleOwner){
            updateLanguagesDialogState(it)
        }
    }

    private fun updateLanguagesDialogState(it: String) {
        hideDialog(languagesDialog)
        clearAllRadioButtonsSelection()
        when(it){
            Languages.ENGLISH.tag -> {
                englishRadioButton.setImageResource(R.drawable.ic_radio_button_checked)
                languagesIndicator.text = "ENG"
            }

            Languages.GERMAN.tag -> {
                germanRadioButton.setImageResource(R.drawable.ic_radio_button_checked)
                languagesIndicator.text = "DEU"
            }

            Languages.FRENCH.tag -> {
                frenchRadioButton.setImageResource(R.drawable.ic_radio_button_checked)
                languagesIndicator.text = "FRA"
            }

            Languages.ITALIAN.tag -> {
                italianRadioButton.setImageResource(R.drawable.ic_radio_button_checked)
                languagesIndicator.text = "ITA"
            }
            Languages.SPANISH.tag -> {
                spanishRadioButton.setImageResource(R.drawable.ic_radio_button_checked)
                languagesIndicator.text = "ESP"
            }
        }
    }

    private fun clearAllRadioButtonsSelection() {
        englishRadioButton.setImageResource(R.drawable.ic_radio_button_unchecked)
        germanRadioButton.setImageResource(R.drawable.ic_radio_button_unchecked)
        frenchRadioButton.setImageResource(R.drawable.ic_radio_button_unchecked)
        italianRadioButton.setImageResource(R.drawable.ic_radio_button_unchecked)
        spanishRadioButton.setImageResource(R.drawable.ic_radio_button_unchecked)
    }

    fun navigateToDetailsFragment(){
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
    }

    fun showDialog(dialog: ConstraintLayout){

        backPressedCallback.isEnabled = true
        dialogOverlay.visibility = View.VISIBLE

        val movePropertyValueHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, -dialog.height.toFloat())
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()
    }

    private fun hideDialog(dialog: ConstraintLayout) {
        dialogOverlay.visibility = View.GONE
        backPressedCallback.isEnabled = false

        val movePropertyValueHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f)
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()
    }

    fun showLanguagesDialog(){
        showDialog(languagesDialog)
    }

    fun hideLanguagesDialog(){
        hideDialog(languagesDialog)
    }

}