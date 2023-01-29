package com.omersungur.countryapi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omersungur.countryapi.R
import com.omersungur.countryapi.databinding.FragmentCountryBinding
import com.omersungur.countryapi.util.PlaceHolderProgressBar
import com.omersungur.countryapi.util.downloadUrl
import com.omersungur.countryapi.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*

class CountryFragment : Fragment() {

    private var countryUuid = 0
    private lateinit var countryViewModel : CountryViewModel
    private lateinit var dataBinding : FragmentCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
        }

        countryViewModel = ViewModelProviders.of(this)[CountryViewModel::class.java]
        countryViewModel.getDataFromRoom(countryUuid)

        observeLiveData()

    }

    private fun observeLiveData () {
        countryViewModel.countryLiveData.observe(viewLifecycleOwner, Observer {country ->
            country?.let {

                dataBinding.selectedCountry = it
                /*countryNameText.text = it.countryName
                countryRegionText.text = it.countryRegion
                countryCapitalText.text = it.countryCapital
                countryCurrencyText.text = it.countryCurrency
                countryLanguageText.text = it.countryLanguage
                context?.let {
                    country.countryFlag?.let { it1 ->
                        ImageViewCountry.downloadUrl(it1, PlaceHolderProgressBar(it))
                    }
                }*/

            }
        })
    }
}