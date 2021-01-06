package com.example.covi_data

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONArray

class DataDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_details)
        setGraphData()
    }

    private fun setGraphData() {
        val url = "https://api.covid19india.org/data.json"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val testDataArray = response.getJSONArray("cases_time_series")
                    graphData(testDataArray,findViewById(R.id.totalGraph),Color.WHITE,"totalconfirmed")
                    graphData(testDataArray,findViewById(R.id.activeGraph),Color.BLUE,"dailyconfirmed")
                    graphData(testDataArray,findViewById(R.id.deathGraph),Color.RED,"dailydeceased")
                    graphData(testDataArray,findViewById(R.id.recGraph),Color.GREEN,"dailyrecovered")
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    Log.d("error", error.message.toString())
                }
        )
        queue.add(jsonObjectRequest)
    }

    private fun graphData(testDataArray:JSONArray, graph:GraphView, color: Int, field:String){
        val series = LineGraphSeries<DataPoint>()
        series.color = color
        var max = -1.0
        for (i in 0 until testDataArray.length()) {
            val o = testDataArray.getJSONObject(i)
            val y = o.getString(field).toDouble()
            if (y > max) max = y
            series.appendData(DataPoint(i.toDouble(), y), true, 500)
        }
        graph.addSeries(series)
        graph.gridLabelRenderer.gridColor = Color.WHITE
        graph.gridLabelRenderer.horizontalLabelsColor = Color.WHITE
        graph.gridLabelRenderer.verticalLabelsColor = Color.WHITE
        graph.viewport.maxYAxisSize = max
        graph.viewport.maxXAxisSize = testDataArray.length().toDouble()
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true

        val gLabel = graph.gridLabelRenderer
        gLabel.horizontalAxisTitle = "Days"
        gLabel.verticalAxisTitle = "Cases"
        gLabel.verticalAxisTitleColor = Color.WHITE
        gLabel.horizontalAxisTitleColor = Color.WHITE
        gLabel.verticalAxisTitleTextSize = 48f
        gLabel.horizontalAxisTitleTextSize = 48f
    }
}