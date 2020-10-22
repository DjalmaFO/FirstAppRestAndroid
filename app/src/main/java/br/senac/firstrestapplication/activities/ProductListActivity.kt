package br.senac.firstrestapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.senac.firstrestapplication.R
import br.senac.firstrestapplication.model.Product
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.product_card_item.view.*
import java.text.NumberFormat

class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
    }

    fun reloadList(products: List<Product>){
        val formater = NumberFormat.getCurrencyInstance()

        for (product in products){
            val card = layoutInflater.inflate(R.layout.product_card_item, contProductsList, false)
            card.tvNomeProductList.text = product.nomeProduto
            card.tvPrecoProductList.text = formater.format(product.precoProduto)
            contProductsList.addView(card)
        }
    }
}