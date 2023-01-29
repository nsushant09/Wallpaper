package com.neupanesushant.wallpaper

import android.app.Application
import android.content.Context
import android.widget.Toast

fun Context.showText(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}