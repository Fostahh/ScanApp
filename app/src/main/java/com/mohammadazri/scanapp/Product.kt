package com.mohammadazri.scanapp

class Product{
    private var code: String? = null
    private var name: String? = null

    fun Product(code: String?, name: String?) {
        this.code = code
        this.name = name
    }

    fun getProductCode(): String? {
        return code
    }

    fun setProductCode(code: String?) {
        this.code = code
    }

    fun getProductName(): String? {
        return name
    }

    fun setProductName(name: String?) {
        this.name = name
    }

}