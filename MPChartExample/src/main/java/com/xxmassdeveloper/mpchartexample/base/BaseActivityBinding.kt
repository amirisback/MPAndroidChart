package com.xxmassdeveloper.mpchartexample.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding

/**
 * Created by faisalamircs on 29/09/2025
 * -----------------------------------------
 * Name     : Muhammad Faisal Amir
 * E-mail   : faisalamircs@gmail.com
 * Github   : github.com/amirisback
 * -----------------------------------------
 */


abstract class BaseActivityBinding<VB : ViewBinding> : BaseActivity() {

    protected val binding: VB by lazy {
        setupViewBinding()
    }

    abstract fun setupViewBinding(): VB

    open fun onCreateExt(savedInstanceState: Bundle?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onCreateExt(savedInstanceState)
    }

}