package com.othadd.hngmobiletask3

import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.othadd.hngmobiletask3.databinding.FragmentDetailBinding
import com.othadd.hngmobiletask3.models.Country
import com.othadd.hngmobiletask3.models.NA
import java.util.*

class DetailFragment : Fragment() {
    private val sharedViewModel: ExploreViewModel by activityViewModels { ExploreViewModelFactory() }
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            detailFragment = this@DetailFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selectedCountry.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            setTextViews(it)
        }
    }

    /***
     * coatOfarmurl
     * capital
     * independence
     * subregion
     * currency
     * dialling code
     */
    private fun setTextViews(it: Country) {
        val numberFormat = NumberFormat.getInstance()

        binding.apply {

            // capital
            if (it.capital == NA) {
                capitalTextView.visibility = View.GONE
                capitalValueTextView.visibility = View.GONE
            } else {
                capitalValueTextView.text = it.capital
            }

            // population
            populationValueTextView.text = numberFormat.format(it.population)

            // area
            areaValueTextView.text = "${numberFormat.format(it.area)} square kilometers"

            //continent
            continentValueTextView.text = it.continent

            //landlocked
            landLockedValueTextView.text = it.landLocked.capitalize(Locale.getDefault())

            // independence
            if (it.independence == NA) {
                independenceTextView.visibility = View.GONE
                independenceValueTextView.visibility = View.GONE
            } else {
                independenceValueTextView.text = it.independence.capitalize(Locale.getDefault())
            }

            // un membership
            uNMemberValueTextView.text = it.memberOfUN.capitalize(Locale.getDefault())

            // region
            regionValueTextView.text = it.region

            //subregion
            if (it.subRegion == NA) {
                subregionTextView.visibility = View.GONE
                subregionValueTextView.visibility = View.GONE
            } else {
                subregionValueTextView.text = it.subRegion
            }

            //languages
            if (it.languages.isEmpty()) {
                languagesTextView.visibility = View.GONE
                languagesValueTextView.visibility = View.GONE
            } else {
                var languages = ""
                for ((index, language) in it.languages.iterator().withIndex()) {
                    languages += if (index == 0) {
                        language
                    } else {
                        ", $language"
                    }
                }
                languagesValueTextView.text = languages
            }

            // currency
            if (it.currency == NA){
                currencyTextView.visibility = View.GONE
                currencyValueTextView.visibility = View.GONE
            }
            else{
                currencyValueTextView.text = it.currency
            }

            //timezone
            timezoneValueTextView.text = it.timezone

            //dialling code
            if (it.dialingCode == NA){
                diallingCodeTextView.visibility = View.GONE
                diallingCodeValueTextView.visibility = View.GONE
            }
            else{
                diallingCodeValueTextView.text = it.dialingCode
            }

            //driving side
            drivingSideValueTextView.text = it.drivingSide.capitalize(Locale.getDefault())
        }
    }
}