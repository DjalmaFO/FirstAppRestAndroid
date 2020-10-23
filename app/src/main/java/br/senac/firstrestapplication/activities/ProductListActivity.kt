package br.senac.firstrestapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import br.senac.firstrestapplication.R
import br.senac.firstrestapplication.model.Product
import br.senac.firstrestapplication.service.ProductAPI
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.product_card_item.view.*
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.concurrent.TimeUnit
import kotlin.math.log

class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
    }

    override fun onResume() {
        super.onResume()
        getProducts()
    }
    fun getProducts(){
        val cliente = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://oficinacordova.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .client(cliente)
            .build()

        val apiProduct = retrofit.create(ProductAPI::class.java)

        val call = apiProduct.list()

        // Implementação do call back
        val callBack = object: Callback<List<Product>>{
            // Implementação da classe
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductListActivity, "Erro ao realizar a requisição", Toast.LENGTH_LONG).show()
                Log.e("ProductListActivity", "getProducts", t)

                shimmerProductsList.visibility = View.GONE
                shimmerProductsList.stopShimmer()
                scrollViewProductList.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful){
                    val products = response.body()
                    reloadList(products)
                }else{
                    Toast.makeText(this@ProductListActivity, "Erro ao realizar a requisição", Toast.LENGTH_LONG).show()
                    Log.e(response.code().toString(), response.errorBody().toString())
                }

                shimmerProductsList.visibility = View.GONE
                shimmerProductsList.stopShimmer()
                scrollViewProductList.visibility = View.VISIBLE
            }

        }

        call.enqueue(callBack)

        shimmerProductsList.visibility = View.VISIBLE
        shimmerProductsList.startShimmer()
        scrollViewProductList.visibility = View.GONE

    }

    fun reloadList(products: List<Product>?){
        val formater = NumberFormat.getCurrencyInstance()

        products?.let {
            for (product in it){
                val card = layoutInflater.inflate(R.layout.product_card_item, contProductsList, false)
                card.tvNomeProductList.text = product.nomeProduto
                card.tvPrecoProductList.text = formater.format(product.precoProduto)

                val shimmer = Shimmer.AlphaHighlightBuilder()
                    .setBaseAlpha(0.9f)
                    .setHighlightAlpha(0.7f)
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build()


                val shimmerDrawable = ShimmerDrawable()
                shimmerDrawable.setShimmer(shimmer)

                val url = "https://oficinacordova.azurewebsites.net/android/rest/produto/image/${product.idProduto}"

                Picasso.get()
                    .load(url)
                    .placeholder(shimmerDrawable)
                    .error(R.drawable.no_image)
                    .into(card.ivProductCardList)

                contProductsList.addView(card)
            }
        }

    }
}