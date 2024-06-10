package com.example.apptranslate

import okhttp3.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService { //interfaz que se utiliza para hacer las peticiones a la API
    @GET("/0.2/languages") //base de la url
    suspend fun getLanguages(): retrofit2.Response<List<Language>>

    @Headers("Authorization: Bearer 26985a330f79deb242cb0332c9ef8322") //es la identificacion que envia para poder hacer la solicitud
    @FormUrlEncoded //se utiliza para enviar la solicitud encodeada
    @POST("/0.2/detect")//base de la url
    suspend fun getTextLanguage(@Field("q")text: String) : retrofit2.Response<DetectionResponse> //funcion que envia la peticion a la api y devuelve la lista con la respuesta del API
}
