package com.neupanesushant.wallpaper

import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Context.showText(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun FragmentManager.showFragment(containerViewId: Int, fragment: Fragment) {
    this.beginTransaction().replace(containerViewId, fragment).commitNowAllowingStateLoss()
}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View?.hideKeyboard() {
    if (this != null) {
        val inputManager =
            this.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(
                this.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}