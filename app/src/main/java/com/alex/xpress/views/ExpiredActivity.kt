package com.alex.xpress.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.xpress.R
import com.alex.xpress.Utils.NotificationUtils
import com.alex.xpress.adapters.ExpiredAdapter
import com.alex.xpress.database.DbHelper
import com.alex.xpress.models.Product
import kotlinx.android.synthetic.main.toolbar.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal

class ExpiredActivity : AppCompatActivity() {

    lateinit var dbHelper : DbHelper

    var dataListProductExpired: ArrayList<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expired)
        setSupportActionBar(toolbar)
        title = "Productos Vencidos"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        dbHelper = DbHelper(this)
        dataListProductExpired = dbHelper.readAllProductsExpired()

        val recyclerView = findViewById<RecyclerView>(R.id.expired_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val expiredAdapter = ExpiredAdapter(dataListProductExpired!!, this)
        recyclerView.adapter = expiredAdapter
    }

    override fun onResume() {
        super.onResume()
        NotificationUtils.hideTimerNotification(this)

        if(dataListProductExpired!!.size == 0){
                MaterialTapTargetPrompt.Builder(this)
                    .setTarget(toolbar)
                    .setPrimaryText("Ups")
                    .setSecondaryText("No hay ningun producto vencido :D")
                    .setBackButtonDismissEnabled(true)
                    .setPromptBackground(RectanglePromptBackground())
                    .setPromptFocal(RectanglePromptFocal())
                    .show()
        }
    }
}
