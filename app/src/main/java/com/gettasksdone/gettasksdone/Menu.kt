package com.gettasksdone.gettasksdone
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.gettasksdone.gettasksdone.databinding.ActivityMenuBinding
import com.gettasksdone.gettasksdone.util.PreferenceHelper

class Menu : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMenu.toolbar)

        binding.appBarMenu.fab.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.fab_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_tarea -> {
                        val intent = Intent(this, AnadirTask::class.java)
                        startActivity(intent)
                        true
                    }
                    //Aqui se añade la actividad de crear proyecto
                    R.id.action_proyecto -> {
                        val intent = Intent(this, CreateProject::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_inbox, R.id.nav_proyectos,R.id.nav_esperando,R.id.nav_agendado,R.id.nav_ad, R.id.nav_gallery, R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                // maneja otros elementos del menú aquí...
                else ->{
                    // Delega la navegación al NavController para los demás elementos del menú
                    NavigationUI.onNavDestinationSelected(menuItem, navController)
                }
            }
        }

        NavHeaderUtils.updateNavHeader(this)
    }

    private fun signOut() {
        val preferences = PreferenceHelper.defaultPrefs(this)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}