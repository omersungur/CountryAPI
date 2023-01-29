package com.omersungur.countryapi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.omersungur.countryapi.db.CountryDatabase
import com.omersungur.countryapi.model.CountryModel
import com.omersungur.countryapi.service.CountryAPIService
import com.omersungur.countryapi.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val countryAPIService = CountryAPIService()
    private val customPreferences = CustomSharedPreferences(getApplication())
    private val refreshTime = 10 * 60 * 1000 * 1000 * 1000L // 10 dakika

    val countries = MutableLiveData<List<CountryModel>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData() {

        val updateTime = customPreferences.getTime()
        updateTime?.let {
            if(updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
                getDataFromSQL() // Hesaplarımıza göre 10 dakikayı geçmediyse SQL içinden verileri çekiyoruz geçtiyse API'den alıyoruz.
            }
            else {
                getDataFromAPI()
            }
        }
    }

    fun refreshFromAPI() {
        getDataFromAPI()
    }

    private fun getDataFromSQL() {
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"SQL",Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI() {
        countryLoading.value = true

        compositeDisposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<CountryModel>>() {
                    override fun onSuccess(t: List<CountryModel>) {
                        storeInSQL(t) // verileri başarıyla çekebilirsek sql içine kaydediyoruz.
                        Toast.makeText(getApplication(),"API",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    fun showCountries(countryList: List<CountryModel>) {
        countries.value = countryList
        countryLoading.value = false
        countryError.value = false
    }

    fun storeInSQL(list: List<CountryModel>) {
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAll()
            val listLong =
                dao.insertAll(*list.toTypedArray()) // *list.toTypedArray() > listedeki elemanları tekil bir eleman gibi sayar.
            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt() // listLong içinde primary keyler bulunuyor.
                i++

            }
            showCountries(list)
        }
        customPreferences.saveTime(System.nanoTime()) // ne zaman verileri çekersek zaman kaydediliyor.
    }
}

