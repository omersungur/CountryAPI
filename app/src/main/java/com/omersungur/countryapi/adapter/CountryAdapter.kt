package com.omersungur.countryapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.omersungur.countryapi.R
import com.omersungur.countryapi.databinding.CountryItemBinding
import com.omersungur.countryapi.model.CountryModel
import com.omersungur.countryapi.util.PlaceHolderProgressBar
import com.omersungur.countryapi.util.downloadUrl
import com.omersungur.countryapi.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.country_item.view.*

class CountryAdapter(var countryList : ArrayList<CountryModel>) : RecyclerView.Adapter<CountryAdapter.CountryHolder>(),CountryClickListener {
    class CountryHolder(var binding : CountryItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CountryItemBinding>(inflater, R.layout.country_item,parent,false)
        return CountryHolder(binding)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {

        holder.binding.country = countryList[position]
        holder.binding.clickListener = this

        /*holder.itemView.countryNameFeed.text = countryList[position].countryName
        holder.itemView.countryRegionFeed.text = countryList[position].countryRegion

        countryList[position].countryFlag?.let {
            holder.itemView.countryImageRow.downloadUrl(it, PlaceHolderProgressBar(holder.itemView.context))
        }

        holder.itemView.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }*/
    }

    fun updateCountryList(newCountryList : List<CountryModel>) { // swipe refresh ve dataları gözlemlediğimiz yer için yazdık.
        countryList.clear() // yeni liste geldiğinde ilk başta içini bir boşalttık
        countryList.addAll(newCountryList)
        notifyDataSetChanged() // adapter içeriğinin değiştiğini sisteme haber verdik.
    }

    override fun countryClicked(v: View) {

        val uuid = v.uuidTextView.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }
}