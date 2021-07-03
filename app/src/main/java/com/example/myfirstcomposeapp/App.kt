package com.example.myfirstcomposeapp

import android.app.Application
import android.util.Log

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        val list = ArrayList<Int>()
        list.add(1)
        list.add(2)
        list.add(11)
        list.add(21)
        list.add(0)
        list.add(13)
        val positives = list.filter { x -> x > 5 }
        Log.e("study_01", "main: $positives")
    }
}