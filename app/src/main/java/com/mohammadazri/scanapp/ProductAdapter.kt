package com.mohammadazri.scanapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class ProductAdapter(options: FirebaseRecyclerOptions<Product>) : FirebaseRecyclerAdapter<Product, ProductAdapter.ViewHolder>(
    options
) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val code: TextView = itemView.findViewById(R.id.code)
        val expireDate:TextView = itemView.findViewById(R.id.expireDate)
        val name:TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout:View = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Product) {
        holder.code.text = model.code
        holder.expireDate.text = model.expireDate
        holder.name.text = model.name
    }
}