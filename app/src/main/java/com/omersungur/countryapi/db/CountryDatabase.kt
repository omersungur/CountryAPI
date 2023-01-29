package com.omersungur.countryapi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.omersungur.countryapi.model.CountryModel

@Database(entities = arrayOf(CountryModel::class), version = 1)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao () : CountryDAO

    companion object {

        //Singleton yapısı
        //Veri tabanımıza aynı an da ulaşılmasını istemiyoruz yoksa sistem çökecektir, bu yüzden singleton yapısını kullanıyoruz.
        //companion object > Diğer her yerde bu bloğa erişilmesini sağladı.

        @Volatile private var instance : CountryDatabase? = null
        //Volatile > Değişkeni diğer threadlere görünür hale getirir
        private val lock = Any()

        operator fun invoke (context : Context) : CountryDatabase = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }
        //instance var mı bakıyoruz yoksa makeDatabase fonksiyonu ile oluşturup bu instance'ın içine veriyoruz.

        private fun makeDatabase(context: Context) : CountryDatabase = Room.databaseBuilder(context.applicationContext,
        CountryDatabase::class.java,"countryDatabase").build()
    }
}