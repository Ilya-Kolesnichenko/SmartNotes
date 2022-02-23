package com.ilyakoles.smartnotes.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private var fm = supportFragmentManager
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        prefs = getSharedPreferences("SmartNotes_Settings", Context.MODE_PRIVATE)

        val userPass = prefs.getString("Password", "")
        val userId = prefs.getInt("UserID", 0)

        if (userPass == "")
            launchFragment(LoginFragment.newInstance("", ""), "Login")
        else {
            val intent = FoldersActivity.newIntent(this@LoginActivity, userId)
            startActivity(intent)
        }
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        fm.popBackStack()
        fm.beginTransaction()
            .add(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
        fm.executePendingTransactions()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (fm.backStackEntryCount == 0)
            finish();
    }
}