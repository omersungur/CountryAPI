package com.omersungur.countryapi.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.omersungur.countryapi.R
import com.omersungur.countryapi.adapter.CountryAdapter
import com.omersungur.countryapi.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private lateinit var feedViewModel : FeedViewModel
    private var countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel = ViewModelProviders.of(this)[FeedViewModel::class.java]
        feedViewModel.refreshData()

        countryList.layoutManager = LinearLayoutManager(context)
        countryList.adapter = countryAdapter

        observeLiveData()

        swipeRefreshLayout.setOnRefreshListener {
            countryList.visibility = View.GONE
            countryErrorText.visibility = View.GONE
            countryLoadingBar.visibility = View.VISIBLE

            feedViewModel.refreshFromAPI()

            swipeRefreshLayout.isRefreshing = false

        }

    }

    private fun observeLiveData() {
        feedViewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(it)
                countryErrorText.visibility = View.INVISIBLE
                countryLoadingBar.visibility = View.INVISIBLE
            }
        })

        feedViewModel.countryError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if(it) {
                    countryList.visibility = View.INVISIBLE
                    countryErrorText.visibility = View.VISIBLE
                    countryLoadingBar.visibility = View.INVISIBLE
                }
            }
        })

        feedViewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if(it) {
                    countryList.visibility = View.INVISIBLE
                    countryErrorText.visibility = View.INVISIBLE
                    countryLoadingBar.visibility = View.VISIBLE
                }

            }
        })
    }
}