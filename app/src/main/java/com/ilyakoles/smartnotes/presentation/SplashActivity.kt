package com.ilyakoles.smartnotes.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.presentation.folders.FoldersActivity
import com.ilyakoles.smartnotes.presentation.login.LoginActivity

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.

        prefs = getSharedPreferences("SmartNotes_Settings", Context.MODE_PRIVATE)
        val userPass = prefs.getString("Password", "")
        val userId = prefs.getInt("UserID", 0)

        if (userPass == "")
            Handler().postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.
        else
            Handler().postDelayed({
                val intent = Intent(this, FoldersActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.

    }
}