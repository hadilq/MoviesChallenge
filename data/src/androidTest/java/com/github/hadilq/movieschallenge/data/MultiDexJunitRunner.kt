package com.github.hadilq.movieschallenge.data

import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.test.runner.AndroidJUnitRunner


class MultiDexJunitRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        MultiDex.install(targetContext)
        super.onCreate(arguments)
    }
}