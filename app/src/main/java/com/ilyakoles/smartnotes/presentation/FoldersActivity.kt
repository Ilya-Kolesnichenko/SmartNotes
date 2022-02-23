package com.ilyakoles.smartnotes.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.ActivityFoldersBinding
import com.ilyakoles.smartnotes.databinding.ActivityLoginBinding

class FoldersActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFoldersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoldersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_folders, R.id.navigation_calendar
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val fm = supportFragmentManager

        if (fm.backStackEntryCount == 0)
            finish();
    }

    companion object {
        private const val EXTRA_USED_ID = "UserID"

        fun newIntent(context: Context, userId: Int): Intent {
            val intent = Intent(context, FoldersActivity::class.java)
            intent.putExtra(EXTRA_USED_ID, userId)
            return intent
        }
    }
}