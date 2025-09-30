package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.xxmassdeveloper.mpchartexample.base.BaseActivityBinding
import com.xxmassdeveloper.mpchartexample.databinding.ActivityPiechart2Binding

class PieChartActivity2 : BaseActivityBinding<ActivityPiechart2Binding>() {

    override fun setupViewBinding(): ActivityPiechart2Binding {
        return ActivityPiechart2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        title = "PieChartActivity"

        binding.apply {

            chart.setUsePercentValues(true)
            chart.description.isEnabled = false
            chart.isDrawHoleEnabled = false
            chart.setDrawCenterText(false)

            chart.setExtraOffsets(5f, 10f, 5f, 5f)
            chart.setDragDecelerationFrictionCoef(0.95f)

            chart.setCenterTextTypeface(tfLight)

            chart.setRotationAngle(0f)
            // enable rotation of the chart by touch
            chart.isRotationEnabled = true
            chart.isHighlightPerTapEnabled = true

            // add a selection listener
            chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(
                    e: Entry?,
                    h: Highlight,
                ) {
                    if (e == null) return
                    Log.i(
                        "VAL SELECTED",
                        ("Value: " + e.y + ", index: " + h.x + ", DataSet index: " + h.dataSetIndex)
                    )
                }

                override fun onNothingSelected() {
                    Log.i("PieChart", "nothing selected")
                }
            })

            chart.animateY(1400, Easing.EaseInOutQuad)

            // chart.spin(2000, 0, 360);
            chart.legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                xEntrySpace = 7f
                yEntrySpace = 0f
                yOffset = 0f
            }

            // entry label styling
            chart.setEntryLabelColor(Color.WHITE)
            chart.setEntryLabelTypeface(tfRegular)
            chart.setEntryLabelTextSize(12f)
        }

        setData(4, 5.toFloat())
    }

    private fun setData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry?>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0..<count) {
            entries.add(
                PieEntry(
                    ((Math.random() * range) + range / 5).toFloat(),
                    parties[i % parties.size],
                )
            )
        }

        val dataSet = PieDataSet(entries, "Election Results")

        dataSet.setDrawIcons(false)

        dataSet.setSliceSpace(0f)
        dataSet.setIconsOffset(MPPointF(0f, 40f))
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int?>()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(tfLight)
        binding.chart.setData(data)

        // undo all highlights
        binding.chart.highlightValues(null)

        binding.chart.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pie, menu)
        return true
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    "https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/PieChartActivity.java".toUri()
                startActivity(i)
            }

            R.id.actionToggleValues -> {
                for (set in binding.chart.data.getDataSets()) set.setDrawValues(!set.isDrawValuesEnabled())
                binding.chart.invalidate()
            }

            R.id.actionToggleIcons -> {
                for (set in binding.chart.data.getDataSets()) set.setDrawIcons(!set.isDrawIconsEnabled())
                binding.chart.invalidate()
            }

            R.id.actionToggleHole -> {
                binding.chart.isDrawHoleEnabled = !binding.chart.isDrawHoleEnabled
                binding.chart.invalidate()
            }

            R.id.actionToggleMinAngles -> {
                if (binding.chart.minAngleForSlices == 0f) binding.chart.setMinAngleForSlices(36f)
                else binding.chart.setMinAngleForSlices(0f)
                binding.chart.notifyDataSetChanged()
                binding.chart.invalidate()
            }

            R.id.actionToggleCurvedSlices -> {
                val toSet =
                    !binding.chart.isDrawRoundedSlicesEnabled || !binding.chart.isDrawHoleEnabled
                binding.chart.setDrawRoundedSlices(toSet)
                if (toSet && !binding.chart.isDrawHoleEnabled) {
                    binding.chart.isDrawHoleEnabled = true
                }
                if (toSet && binding.chart.isDrawSlicesUnderHoleEnabled) {
                    binding.chart.setDrawSlicesUnderHole(false)
                }
                binding.chart.invalidate()
            }

            R.id.actionDrawCenter -> {
                binding.chart.setDrawCenterText(!binding.chart.isDrawCenterTextEnabled)
                binding.chart.invalidate()
            }

            R.id.actionToggleXValues -> {
                binding.chart.setDrawEntryLabels(!binding.chart.isDrawEntryLabelsEnabled)
                binding.chart.invalidate()
            }

            R.id.actionTogglePercent -> {
                binding.chart.setUsePercentValues(!binding.chart.isUsePercentValuesEnabled)
                binding.chart.invalidate()
            }

            R.id.animateX -> {
                binding.chart.animateX(1400)
            }

            R.id.animateY -> {
                binding.chart.animateY(1400)
            }

            R.id.animateXY -> {
                binding.chart.animateXY(1400, 1400)
            }

            R.id.actionToggleSpin -> {
                binding.chart.spin(
                    1000,
                    binding.chart.rotationAngle,
                    binding.chart.rotationAngle + 360,
                    Easing.EaseInOutCubic
                )
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
        saveToGallery(binding.chart, "PieChartActivity")
    }

}
