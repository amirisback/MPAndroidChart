package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.xxmassdeveloper.mpchartexample.base.BaseActivityBinding
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView
import com.xxmassdeveloper.mpchartexample.databinding.ActivityLinechart1CustomBinding

/**
 * Example of a heavily customized [LineChart] with limit lines, custom line shapes, etc.
 *
 * @since 1.7.4
 * @version 3.1.0
 */
class LineChartActivity1Custom : BaseActivityBinding<ActivityLinechart1CustomBinding>() {

    override fun setupViewBinding(): ActivityLinechart1CustomBinding {
        return ActivityLinechart1CustomBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        title = "LineChartActivity1Custom"

        binding.chart.apply {

            setBackgroundColor(Color.WHITE)
            setTouchEnabled(true)
            setDrawGridBackground(false)
            description.isEnabled = false
            axisRight.isEnabled = false

            // set listeners
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry, h: Highlight?) {
                    Log.i("Entry selected", e.toString())
                    Log.i("LOW HIGH", "low: " + getLowestVisibleX() + ", high: " + getHighestVisibleX())
                    Log.i("MIN MAX", "xMin: $xChartMin, xMax: $xChartMax, yMin: $yChartMin, yMax: $yChartMax")
                }

                override fun onNothingSelected() {
                    Log.i("Nothing selected", "Nothing selected.")
                }
            })



            // create marker to display box when values are selected
            val mv = MyMarkerView(this@LineChartActivity1Custom, R.layout.custom_marker_view)

            // Set the marker to the chart
            mv.chartView = this
            marker = mv

            setDragEnabled(true)
            setScaleEnabled(true)
            setPinchZoom(true)

            val xAxis = xAxis
            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.valueFormatter = IndexAxisValueFormatter(
                listOf(
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"
                )
            )

            val yAxis = axisLeft
            // disable dual axis (only use LEFT axis)
            axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
            yAxis.setAxisMaximum(1400f)
            yAxis.setAxisMinimum(0f)

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawLimitLinesBehindData(true)

            // add data
            setData(10, 180f)

            // draw points over time
            animateX(1500)

        }
    }

    private fun setData(count: Int, range: Float) {
        val values = ArrayList<Entry?>()

        for (i in 0..<count) {
            val `val` = (Math.random() * range).toFloat() - 30
            values.add(Entry(i.toFloat(), `val`, resources.getDrawable(R.drawable.star)))
        }

        val set1: LineDataSet

        if (binding.chart.data != null && binding.chart.data.getDataSetCount() > 0) {
            set1 = binding.chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            binding.chart.data.notifyDataChanged()
            binding.chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")

            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.setColor(Color.BLACK)
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.setLineWidth(3f)
            set1.circleRadius = 4f

            // draw points as solid circles
            set1.setDrawCircleHole(false)
            set1.setDrawValues(false)
            set1.setDrawFilled(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> binding.chart.axisLeft.axisMinimum }

            val dataSets = ArrayList<ILineDataSet?>()
            dataSets.add(set1) // add the data sets
            dataSets.add(set1) // add the data sets
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            binding.chart.setData(data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.line, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    "https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/LineChartActivity1.java".toUri()
                startActivity(i)
            }

            R.id.actionToggleValues -> {
                val sets = binding.chart.data.dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.setDrawValues(!set.isDrawValuesEnabled)
                }

                binding.chart.invalidate()
            }

            R.id.actionToggleIcons -> {
                val sets = binding.chart.data.dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.setDrawIcons(!set.isDrawIconsEnabled)
                }

                binding.chart.invalidate()
            }

            R.id.actionToggleHighlight -> {
                if (binding.chart.data != null) {
                    binding.chart.data.isHighlightEnabled = !binding.chart.data.isHighlightEnabled()
                    binding.chart.invalidate()
                }
            }

            R.id.actionToggleFilled -> {
                val sets = binding.chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    if (set.isDrawFilledEnabled) set.setDrawFilled(false)
                    else set.setDrawFilled(true)
                }
                binding.chart.invalidate()
            }

            R.id.actionToggleCircles -> {
                val sets = binding.chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    if (set.isDrawCirclesEnabled) set.setDrawCircles(false)
                    else set.setDrawCircles(true)
                }
                binding.chart.invalidate()
            }

            R.id.actionToggleCubic -> {
                val sets = binding.chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.mode = if (set.mode == LineDataSet.Mode.CUBIC_BEZIER)
                        LineDataSet.Mode.LINEAR
                    else
                        LineDataSet.Mode.CUBIC_BEZIER
                }
                binding.chart.invalidate()
            }

            R.id.actionToggleStepped -> {
                val sets = binding.chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.mode = if (set.mode == LineDataSet.Mode.STEPPED)
                        LineDataSet.Mode.LINEAR
                    else
                        LineDataSet.Mode.STEPPED
                }
                binding.chart.invalidate()
            }

            R.id.actionToggleHorizontalCubic -> {
                val sets = binding.chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.mode = if (set.mode == LineDataSet.Mode.HORIZONTAL_BEZIER)
                        LineDataSet.Mode.LINEAR
                    else
                        LineDataSet.Mode.HORIZONTAL_BEZIER
                }
                binding.chart.invalidate()
            }

            R.id.actionTogglePinch -> {
                if (binding.chart.isPinchZoomEnabled) binding.chart.setPinchZoom(false)
                else binding.chart.setPinchZoom(true)

                binding.chart.invalidate()
            }

            R.id.actionToggleAutoScaleMinMax -> {
                binding.chart.isAutoScaleMinMaxEnabled = !binding.chart.isAutoScaleMinMaxEnabled
                binding.chart.notifyDataSetChanged()
            }

            R.id.animateX -> {
                binding.chart.animateX(2000)
            }

            R.id.animateY -> {
                binding.chart.animateY(2000, Easing.EaseInCubic)
            }

            R.id.animateXY -> {
                binding.chart.animateXY(2000, 2000)
            }

            R.id.actionSave -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveToGallery()
                } else {
                    requestStoragePermission(binding.chart)
                }
            }
        }
        return true
    }

    override fun saveToGallery() {
        saveToGallery(binding.chart, "LineChartActivity1")
    }

}
