package br.senac.firstrestapplication.service

import br.senac.firstrestapplication.model.Product
import retrofit2.Call
import retrofit2.http.GET

interface ProductAPI {

    @GET("/android/rest/produto")
    fun list(): Call <List<Product>>
}