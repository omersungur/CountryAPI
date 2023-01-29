package com.omersungur.countryapi.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.omersungur.countryapi.model.CountryModel

@Dao
interface CountryDAO {

    @Insert
    suspend fun insertAll(vararg countries : CountryModel) : List<Long>

    @Query("SELECT * FROM CountryModel")
    suspend fun getCountries() : List<CountryModel>

    @Query("SELECT * FROM CountryModel WHERE uuid = :countryId")
    suspend fun getCountry(countryId : Int) : CountryModel

    @Query("DELETE FROM CountryModel")
    suspend fun deleteAll()
}