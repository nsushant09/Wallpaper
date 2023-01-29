package com.neupanesushant.wallpaper

import com.neupanesushant.wallpaper.components.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL_PEXELS = "https://api.pexels.com/"
const val PEXELS_API_KEY = "563492ad6f9170000100000143117b6d868a4e458df8fc34a264a629"
fun appModules() = module{

    single{
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_PEXELS)
            .build()
            .create(Endpoints::class.java)

    }

    viewModel {
        MainViewModel(androidApplication(), get())
    }

}