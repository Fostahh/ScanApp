package com.mohammadazri.scanapp



import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class ProductAdapter(var context:Context, options: FirebaseRecyclerOptions<Product>) : FirebaseRecyclerAdapter<Product, ProductAdapter.ViewHolder>(options) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kodeBrg: TextView = itemView.findViewById(R.id.kodeBrgTextView)
        val nup: TextView = itemView.findViewById(R.id.nupTextView)
        val card: CardView = itemView.findViewById(R.id.productCardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: View = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Product) {

        holder.nup.text = model.NUP
        holder.kodeBrg.text = model.kodeBarang

        holder.card.setOnClickListener {
            val alert = Dialog(context)
            alert.setContentView(R.layout.item_product_detail)

            val NUP = alert.findViewById<TextView>(R.id.nupProductDetail)
            val kodeBarang = alert.findViewById<TextView>(R.id.kodeBarangProductDetail)
            val tahunPeroleh = alert.findViewById<TextView>(R.id.tahunPerolehanProductDetail)
            val tempatRuangan = alert.findViewById<TextView>(R.id.tempatRuanganProductDetail)
            val kondisiBarang = alert.findViewById<TextView>(R.id.kondisiBarangProductDetail)
            val closeButton = alert.findViewById<Button>(R.id.closeButton)

//            val knds = alert.findViewById<EditText>(R.id.kndsProductDetail)

            kodeBarang.text = model.kodeBarang
            tahunPeroleh.text = model.tahunPeroleh
            NUP.text = model.NUP
            tempatRuangan.text = model.tempatRuangan
            kondisiBarang.text = model.kondisiBarang
//            knds.setText(model.kondisiBarang)

            alert.show()

            closeButton.setOnClickListener {
//                fun onClick(view:View) {
//                    val updateKondisi = HashMap<String, Any>()
//                    updateKondisi.put("kodeBarang", knds)
//                    firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Product").child(qrbarcode)
//
//                }
                alert.dismiss()
            }

        }


    }
}