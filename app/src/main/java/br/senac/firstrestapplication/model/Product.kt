package br.senac.firstrestapplication.model

data class Product (
    var idCategoria: Int,
    var idProduto: Int,
    var nomeProduto: String,
    var qtdMinEstoque: Int,
    var precoProduto: Float,
    var descProduto: String,
    var ativoProduto: Boolean
)