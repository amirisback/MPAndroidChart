package com.xxmassdeveloper.mpchartexample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
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
import com.xxmassdeveloper.mpchartexample.databinding.ActivityPiechartBinding

class PieChartActivity : BaseActivityBinding<ActivityPiechartBinding>(), OnSeekBarChangeListener,
    OnChartValueSelectedListener {

    override fun setupViewBinding(): ActivityPiechartBinding {
        return ActivityPiechartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        title = "PieChartActivity"

        binding.apply {
            seekBar1.setOnSeekBarChangeListener(this@PieChartActivity)
            seekBar2.setOnSeekBarChangeListener(this@PieChartActivity)

            chart1.setUsePercentValues(true)
            chart1.description.isEnabled = false
            chart1.setExtraOffsets(5f, 10f, 5f, 5f)

            chart1.setDragDecelerationFrictionCoef(0.95f)

            chart1.setCenterTextTypeface(tfLight)
            chart1.centerText = generateCenterSpannableText()

            chart1.isDrawHoleEnabled = true
            chart1.setHoleColor(Color.WHITE)

            chart1.setTransparentCircleColor(Color.WHITE)
            chart1.setTransparentCircleAlpha(110)

            chart1.holeRadius = 58f
            chart1.transparentCircleRadius = 61f

            chart1.setDrawCenterText(true)

            chart1.setRotationAngle(0f)
            // enable rotation of the chart1 by touch
            chart1.isRotationEnabled = true
            chart1.isHighlightPerTapEnabled = true

            // chart1.setUnit(" €");
            // chart1.setDrawUnitsInChart(true);

            // add a selection listener
            chart1.setOnChartValueSelectedListener(this@PieChartActivity)

            seekBar1.progress = 4
            seekBar2.progress = 10

            chart1.animateY(1400, Easing.EaseInOutQuad)

            // chart1.spin(2000, 0, 360);
            val l = chart1.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            l.xEntrySpace = 7f
            l.yEntrySpace = 0f
            l.yOffset = 0f

            // entry label styling
            chart1.setEntryLabelColor(Color.WHITE)
            chart1.setEntryLabelTypeface(tfRegular)
            chart1.setEntryLabelTextSize(12f)
        }
    }

    private fun setData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry?>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart1.
        for (i in 0..<count) {
            entries.add(
                PieEntry(
                    ((Math.random() * range) + range / 5).toFloat(),
                    parties[i % parties.size],
                    resources.getDrawable(R.drawable.star)
                )
            )
        }

        val dataSet = PieDataSet(entries, "Election Results")

        dataSet.setDrawIcons(false)

        dataSet.setSliceSpace(3f)
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

        dataSet.setColors(colors)

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(tfLight)
        binding.chart1.setData(data)

        // undo all highlights
        binding.chart1.highlightValues(null)

        binding.chart1.invalidate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.pie, menu)
        return true
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.getItemId()
        if (itemId == R.id.viewGithub) {
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/PieChartActivity.java"))
            startActivity(i)
        } else if (itemId == R.id.actionToggleValues) {
            for (set in binding.chart1.getData()
                .getDataSets()) set.setDrawValues(!set.isDrawValuesEnabled())

            binding.chart1.invalidate()
        } else if (itemId == R.id.actionToggleIcons) {
            for (set in binding.chart1.getData()
                .getDataSets()) set.setDrawIcons(!set.isDrawIconsEnabled())

            binding.chart1.invalidate()
        } else if (itemId == R.id.actionToggleHole) {
            binding.chart1.setDrawHoleEnabled(!binding.chart1.isDrawHoleEnabled())
            binding.chart1.invalidate()
        } else if (itemId == R.id.actionToggleMinAngles) {
            if (binding.chart1.getMinAngleForSlices() == 0f) binding.chart1.setMinAngleForSlices(36f)
            else binding.chart1.setMinAngleForSlices(0f)
            binding.chart1.notifyDataSetChanged()
            binding.chart1.invalidate()
        } else if (itemId == R.id.actionToggleCurvedSlices) {
            val toSet =
                !binding.chart1.isDrawRoundedSlicesEnabled() || !binding.chart1.isDrawHoleEnabled()
            binding.chart1.setDrawRoundedSlices(toSet)
            if (toSet && !binding.chart1.isDrawHoleEnabled()) {
                binding.chart1.setDrawHoleEnabled(true)
            }
            if (toSet && binding.chart1.isDrawSlicesUnderHoleEnabled()) {
                binding.chart1.setDrawSlicesUnderHole(false)
            }
            binding.chart1.invalidate()
        } else if (itemId == R.id.actionDrawCenter) {
            binding.chart1.setDrawCenterText(!binding.chart1.isDrawCenterTextEnabled())
            binding.chart1.invalidate()
        } else if (itemId == R.id.actionToggleXValues) {
            binding.chart1.setDrawEntryLabels(!binding.chart1.isDrawEntryLabelsEnabled())
            binding.chart1.invalidate()
        } else if (itemId == R.id.actionTogglePercent) {
            binding.chart1.setUsePercentValues(!binding.chart1.isUsePercentValuesEnabled())
            binding.chart1.invalidate()
        } else if (itemId == R.id.animateX) {
            binding.chart1.animateX(1400)
        } else if (itemId == R.id.animateY) {
            binding.chart1.animateY(1400)
        } else if (itemId == R.id.animateXY) {
            binding.chart1.animateXY(1400, 1400)
        } else if (itemId == R.id.actionToggleSpin) {
            binding.chart1.spin(
                1000,
                binding.chart1.getRotationAngle(),
                binding.chart1.getRotationAngle() + 360,
                Easing.EaseInOutCubic
            )
        } else if (itemId == R.id.actionSave) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                saveToGallery()
            } else {
                requestStoragePermission(binding.chart1)
            }
        }
        return true
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        binding.apply {

            tvXMax.text = seekBar1.progress.toString()
            tvYMax.text = seekBar2.progress.toString()

            setData(seekBar1.progress, seekBar2.progress.toFloat())
        }
    }

    override fun saveToGallery() {
        saveToGallery(binding.chart1, "PieChartActivity")
    }

    private fun generateCenterSpannableText(): SpannableString {
        val s = SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)
        return s
    }

    override fun onValueSelected(e: Entry?, h: Highlight) {
        if (e == null) return
        Log.i(
            "VAL SELECTED",
            ("Value: " + e.getY() + ", index: " + h.getX()
                    + ", DataSet index: " + h.getDataSetIndex())
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}
