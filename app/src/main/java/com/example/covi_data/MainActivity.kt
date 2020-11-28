package com.example.covi_data

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner : Spinner = findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.states,
            R.layout.spinner_theme
        ).also{
            arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner.adapter = arrayAdapter
        }
    }
    fun getData(view: View) {
        val intent = Intent(this, DataPage::class.java)
        intent.putExtra("id",spinner.selectedItemPosition)
        intent.putExtra("stateName",spinner.selectedItem.toString())

        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right)
        startActivity(intent)
    }

}