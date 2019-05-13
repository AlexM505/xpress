package com.alex.xpress.adapters

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alex.xpress.R
import com.alex.xpress.Utils.NotificationUtils
import com.alex.xpress.Utils.Utils
import com.alex.xpress.database.DbHelper
import com.alex.xpress.models.Product
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class ProductoAdapter(val listProducts: ArrayList<Product>, val context: Context) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_productos, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return listProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var dbHelper = DbHelper(context)
        var hoyMill:Long = 0
        var vctoMill:Long = 0
        val calendar = Calendar.getInstance()
        val simpledateformat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        simpledateformat.format(calendar.time)
        hoyMill = calendar.timeInMillis

        var stringDate = simpledateformat.parse(listProducts[position].expirationDate)
        calendar.time = stringDate
        vctoMill = calendar.timeInMillis

        val dateValidate:Long = vctoMill - hoyMill

        val txtCantidad:String = "Cant: "+ listProducts[position].cantProduct.toString()

        holder.indicador.setBackgroundResource(R.color.colorPrimaryDark)
        holder.txtNameProduct.text = Utils.validateProductName(listProducts[position].nameProduct)
        holder.txtCantProduct.text = txtCantidad

        Picasso.with(context).load(listProducts[position].icon).into(holder.iconProduct)

        if (holder.newCountDownTimer != null) {
            holder.newCountDownTimer!!.cancel()
        }

        if(dateValidate < 0){

            if(listProducts[position].indNotification != 1){
                listProducts[position].state = Utils.validateStateProduct(listProducts[position].expirationDate)
                listProducts[position].indNotification = 1
                dbHelper.updateProductState(listProducts[position])
                NotificationUtils.showTimerExpired(context)
            }

            if(listProducts[position].state.equals("CADUCADO")){
                holder.txtDaysRemaining.text = "Fecha"
                holder.txtTimeRemainingProduct.text = "Vencida."
            }
        }else{
            holder.newCountDownTimer = object : CountDownTimer(dateValidate, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    //val timeRemaining = Utils.timeString(millisUntilFinished)
                    val days = Utils.timeDays(millisUntilFinished)
                    val timeRemaining = Utils.timeHoursMinSec(millisUntilFinished)

                    holder.txtDaysRemaining.text = days
                    holder.txtTimeRemainingProduct.text = timeRemaining
                }

                override fun onFinish() {
                    holder.txtDaysRemaining.text = "Fecha"
                    holder.txtTimeRemainingProduct.text = "Vencida."
                }
            }.start()
        }

        holder.cardViewItinerario.setOnClickListener{ view ->
            if(listProducts[holder.layoutPosition].state == "CADUCADO"){
                listProducts.removeAt(holder.layoutPosition)
                notifyItemRemoved(holder.layoutPosition)
            }else{
                Toast.makeText(context,"Producto vigente",Toast.LENGTH_SHORT).show()
            }

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indicador:View = itemView.findViewById(R.id.v_indicador_i)
        val txtNameProduct:TextView = itemView.findViewById(R.id.txtNameProduct)
        val iconProduct:ImageView = itemView.findViewById(R.id.iconProduct)
        val cardViewItinerario:CardView = itemView.findViewById(R.id.cardview_producto)
        val txtCantProduct:TextView = itemView.findViewById(R.id.txtCantProduct)
        val txtTimeRemainingProduct:TextView = itemView.findViewById(R.id.txtTimeRemainingProduct)
        val txtDaysRemaining:TextView = itemView.findViewById(R.id.txtDaysRemaining)

        var newCountDownTimer: CountDownTimer? = null
    }

}