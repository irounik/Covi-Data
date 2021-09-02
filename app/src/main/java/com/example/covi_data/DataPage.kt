package com.example.covi_data

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_data_page.*

class DataPage : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var initialState = 1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_page)

        val id = intent.getIntExtra("id",0)
        val stateName = intent.getStringExtra("stateName")
        initialState = id
        setDataFromApi(stateName)

        val spinner : Spinner = findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.states,
            R.layout.spinner_theme
        ).also{
            arrayAdapter -> arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner.adapter = arrayAdapter
        }
        spinner.setSelection(id)
        spinner.onItemSelectedListener = this

        findViewById<Button>(R.id.getGraphBtn).setOnClickListener {
            getGraph()
        }
    }

    private fun setDataFromApi(stateName: String?) {
        val url = "https://data.covid19india.org/data.json"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val stateDataArray = response.getJSONArray("statewise")
                    var index = 0
                    if(stateName != "All India"){
                        for(i in 1 until stateDataArray.length()){
                            val ithState = stateDataArray.getJSONObject(i)
                            if(stateName == ithState.getString("state")) {
                                index = i
                                break
                            }
                        }
                    }
                    val stateData = stateDataArray.getJSONObject(index)
                    activeData.text = stateData.getString("active")
                    totalData.text = stateData.getString("confirmed")
                    recoveredData.text = stateData.getString("recovered")
                    deadData.text = stateData.getString("deaths")
                    val lU ="Last Update: " + stateData.getString("lastupdatedtime")
                    upToDate.text = lU
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.d("error", error.message.toString())
                }
        )
        queue.add(jsonObjectRequest)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position != initialState){
            val intent = Intent(this, DataPage::class.java)
            intent.putExtra("id",spinner.selectedItemPosition)
            intent.putExtra("stateName",spinner.selectedItem.toString())
            startActivity(intent)
            finish()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    private fun getGraph() {
        val intent = Intent(this,DataDetails::class.java)
        startActivity(intent)
    }
}