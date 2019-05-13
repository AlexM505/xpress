package com.alex.xpress.models

//data class Product (var idProduct: Int, var nameProduct: String, var cantProduct: Int, var icon: Int, var today: String, var expirationDate: String)

class Product{

    var idProduct: Int = 0
    var nameProduct: String = ""
    var cantProduct: Int = 0
    var icon: Int  = 0
    var today: String = ""
    var expirationDate: String = ""
    var state: String = ""
    var indNotification = 0

    constructor()

    constructor(idProduct: Int, nameProduct: String, cantProduct: Int, icon: Int, today: String, expirationDate: String, state: String, indNotification: Int) : this(){
        this.idProduct = idProduct
        this.nameProduct = nameProduct
        this.cantProduct = cantProduct
        this.icon = icon
        this.today = today
        this.expirationDate = expirationDate
        this.state = state
        this.indNotification = indNotification
    }

    constructor(nameProduct: String, cantProduct: Int, icon: Int, today: String, expirationDate: String, state: String, indNotification: Int) : this(){
        this.nameProduct = nameProduct
        this.cantProduct = cantProduct
        this.icon = icon
        this.today = today
        this.expirationDate = expirationDate
        this.state = state
        this.indNotification = indNotification
    }

}