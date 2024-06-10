package com.example.apptranslate

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    private const val BASE_URL = "https://ws.detectlanguage.com/" //Variable constante que contiene la base para la consulta a la api

    val retrofitService: ApiService by lazy { //funcion que conecta al aspiservice con la api se crea solo cuando la llaman si no no
        getRetrofit().create(ApiService::class.java) //conecta con la api

    }

    private fun getRetrofit(): Retrofit { //funcion que devuelve la instancia para poder hacer peticiones a la api
        val logging = HttpLoggingInterceptor() //devuelve la respuesta en log
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) //indico que muestre toda la informacion por el log
        val httpClient = OkHttpClient.Builder() //variable que tiene el interceptor creado con nombre logging
        httpClient.addInterceptor(logging) //asigno el interceptor

        return Retrofit.Builder()//retorna el builder
            .baseUrl(BASE_URL) //url para las peticiones creada anteriormente
            .addConverterFactory(GsonConverterFactory.create())//para convertir en gson
            .client(httpClient.build()) //asigno el interceptor al retrofit
            .build() //crea instancia

    }
}

