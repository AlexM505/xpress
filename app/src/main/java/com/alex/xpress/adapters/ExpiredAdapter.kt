package com.alex.xpress.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alex.xpress.R
import com.alex.xpress.models.Product

class ExpiredAdapter (val listProducts: ArrayList<Product>, val context: Context) : RecyclerView.Adapter<ExpiredAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.expired_design, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.expiredName.text = listProducts[position].nameProduct
        holder.expiredCant.text = "Cantidad: "+ listProducts[position].cantProduct.toString()
        holder.expiredState.text = listProducts[position].state
        holder.expiredDate.text = listProducts[position].expirationDate
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expiredName: TextView = itemView.findViewById(R.id.txtExpiredName)
        val expiredCant:TextView = itemView.findViewById(R.id.txtExpiredCant)
        val expiredState:TextView = itemView.findViewById(R.id.txtExpiredState)
        val expiredDate:TextView = itemView.findViewById(R.id.txtExpiredDate)
    }

}