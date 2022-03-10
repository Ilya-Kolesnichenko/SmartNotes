package com.ilyakoles.smartnotes.presentation.folders

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity
import com.ilyakoles.smartnotes.R
import com.ilyakoles.smartnotes.databinding.ActivityFoldersBinding

class FoldersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoldersBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoldersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_folders, R.id.navigation_calendar
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        NavigationUI.setupActionBarWithNavController(this, navController)
        navController.addOnDestinationChangedListener { controller, _, _ ->
            val shouldShowUpButton = controller.previousBackStackEntry != null
            supportActionBar?.setDisplayHomeAsUpEnabled(shouldShowUpButton)
        }

    }

 /*   override fun onBackPressed() {
       // super.onBackPressed()

      /*  val fm = supportFragmentManager
        if (fm.findFragmentByTag("edit_folder")?.isAdded == true)
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        else
            if (fm.backStackEntryCount == 0)
                finish();*/
    }*/

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
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