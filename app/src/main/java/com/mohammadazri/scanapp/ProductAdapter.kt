package com.mohammadazri.scanapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class ProductAdapter(options: FirebaseRecyclerOptions<Product>
) : FirebaseRecyclerAdapter<Product, ProductAdapter.ViewHolder>(options) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productCode:TextView = itemView.findViewById(R.id.codeProduct)
        val productName:TextView = itemView.findViewById(R.id.nupProduct)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout:View = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Product) {
        holder.productCode.setText(model.getProductCode())
        holder.productName.setText(model.getProductName())
    }
}