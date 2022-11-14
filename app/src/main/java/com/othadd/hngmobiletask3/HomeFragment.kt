package com.othadd.hngmobiletask3

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.LocaleListCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.othadd.hngmobiletask3.databinding.FragmentHomeBinding
import com.othadd.hngmobiletask3.util.*

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
    private lateinit var filterDialog: ConstraintLayout
    private lateinit var africaCheckBox: ImageView
    private lateinit var antarticaCheckBox: ImageView
    private lateinit var asiaCheckBox: ImageView
    private lateinit var australiaCheckBox: ImageView
    private lateinit var europeCheckBox: ImageView
    private lateinit var northAmericaCheckBox: ImageView
    private lateinit var southAmericaCheckBox: ImageView
    private lateinit var utcPlus1CheckBox: ImageView
    private lateinit var utcPlus2CheckBox: ImageView
    private lateinit var utcPlus3CheckBox: ImageView
    private lateinit var utcMinus1CheckBox: ImageView
    private lateinit var utcMinus2CheckBox: ImageView
    private lateinit var utcMinus3CheckBox: ImageView


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
            filterDialog = filterDialogConstraintLayout
            africaCheckBox = africaCheckBoxImageView
            antarticaCheckBox = antarcticaCheckBoxImageView
            asiaCheckBox = asiaCheckBoxImageView
            australiaCheckBox = australiaCheckBoxImageView
            europeCheckBox = europeCheckBoxImageView
            northAmericaCheckBox = northAmericaCheckBoxImageView
            southAmericaCheckBox = southAmericaCheckBoxImageView
            utcPlus1CheckBox = utcPlus1CheckBoxImageView
            utcPlus2CheckBox = utcPlus2CheckBoxImageView
            utcPlus3CheckBox = utcPlus3CheckBoxImageView
            utcMinus1CheckBox = utcMinus1CheckBoxImageView
            utcMinus2CheckBox = utcMinus2CheckBoxImageView
            utcMinus3CheckBox = utcMinus3CheckBoxImageView

        }

        val adapter = CountryRecyclerAdapter {
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

    override fun onResume() {
        super.onResume()

        binding.searchEditText.addTextChangedListener{
            sharedViewModel.searchCountries(it.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.languageTag.observe(viewLifecycleOwner) {
            updateLanguagesDialogState(it)
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(it)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        sharedViewModel.hideDialogs.observe(viewLifecycleOwner) {
            hideLanguagesDialog()
            hideFilterDialog()
        }

        sharedViewModel.filters.observe(viewLifecycleOwner){
            for (filter in it.first){
                updateFilterCheckBoxes(filter, true)
            }

            for (filter in it.second){
                updateFilterCheckBoxes(filter, false)
            }
        }

        sharedViewModel.darkMode.observe(viewLifecycleOwner){
            binding.uiModeButtonImageView.setImageResource(if (it) R.drawable.ic_night_mode else R.drawable.ic_light_mode)
            AppCompatDelegate.setDefaultNightMode(if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }

        sharedViewModel.apiCallSate.observe(viewLifecycleOwner){
            when(it){
                BUSY -> {
                    binding.countryRecyclerView.visibility = View.GONE
                    binding.networkErrorConstraintLayout.visibility = View.GONE
                    binding.loadingConstraintLayout.visibility = View.VISIBLE
                }

                PASSED -> {
                    binding.countryRecyclerView.visibility = View.VISIBLE
                    binding.networkErrorConstraintLayout.visibility = View.GONE
                    binding.loadingConstraintLayout.visibility = View.GONE
                }

                FAILED -> {
                    binding.countryRecyclerView.visibility = View.GONE
                    binding.networkErrorConstraintLayout.visibility = View.VISIBLE
                    binding.loadingConstraintLayout.visibility = View.GONE
                }
            }
        }

        sharedViewModel.continentGroupHidden.observe(viewLifecycleOwner){
            binding.continentDropDownButtonImageView.setImageResource(if(it) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
            if (it) hideContinentGroup() else showContinentGroup()
        }

        sharedViewModel.timezoneGroupHidden.observe(viewLifecycleOwner){
            binding.timezoneDropDownButtonImageView.setImageResource(if (it) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
            if (it) hideTimezoneGroup() else showTimezoneGroup()
        }
    }


    private fun updateLanguagesDialogState(it: String) {
        hideDialog(languagesDialog)
        clearAllRadioButtonsSelection()
        when (it) {
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

    fun filterCheckBoxClicked(code: String) {
        val filterAdded = sharedViewModel.updateSelectedFilters(code)
        updateFilterCheckBoxes(code, filterAdded)
    }

    private fun updateFilterCheckBoxes(code: String, filterAdded: Boolean) {
        when (code) {
            AFRICA -> africaCheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            ANTARCTICA -> antarticaCheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            ASIA -> asiaCheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            AUSTRALIA -> australiaCheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            EUROPE -> europeCheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            NORTH_AMERICA -> northAmericaCheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            SOUTH_AMERICA -> southAmericaCheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            UTC_PLUS_1 -> utcPlus1CheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            UTC_PLUS_2 -> utcPlus2CheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            UTC_PLUS_3 -> utcPlus3CheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            UTC_MINUS_1 -> utcMinus1CheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            UTC_MINUS_2 -> utcMinus2CheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
            UTC_MINUS_3 -> utcMinus3CheckBox.setImageResource(if (filterAdded) R.drawable.ic_check_box else R.drawable.ic_check_box_blank)
        }
    }

    private fun navigateToDetailsFragment() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
    }

    private fun showDialog(dialog: ConstraintLayout) {

        backPressedCallback.isEnabled = true
        dialogOverlay.visibility = View.VISIBLE

        val movePropertyValueHolder =
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dialog.height.toFloat(), 0f)
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

        val movePropertyValueHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, dialog.height.toFloat())
        val transparencyValueHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            dialog,
            movePropertyValueHolder,
            transparencyValueHolder
        )
        animator.start()
    }

    fun showLanguagesDialog() {
        showDialog(languagesDialog)
    }

    fun hideLanguagesDialog() {
        hideDialog(languagesDialog)
    }

    fun showFilterDialog() {
        showDialog(filterDialog)
        sharedViewModel.prepFilteration()
    }

    fun hideFilterDialog() {
        hideDialog(filterDialog)
    }

    fun suspendFilteraion() {
        hideFilterDialog()
        sharedViewModel.suspendFilteration()
    }

    private fun hideContinentGroup(){
        binding.continentGroupConstraintLayout.visibility = View.GONE
    }

    private fun showContinentGroup() {
        binding.continentGroupConstraintLayout.visibility = View.VISIBLE
    }

    fun hideTimezoneGroup(){
        binding.timezoneGroupConstraintLayout.visibility = View.GONE
    }

    fun showTimezoneGroup(){
        binding.timezoneGroupConstraintLayout.visibility = View.VISIBLE
    }

}