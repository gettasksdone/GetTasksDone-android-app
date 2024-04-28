package com.gettasksdone.gettasksdone
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.gettasksdone.gettasksdone.databinding.ActivityMenuBinding
import com.gettasksdone.gettasksdone.io.ApiService
import com.gettasksdone.gettasksdone.model.UserInfo
import com.gettasksdone.gettasksdone.util.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_inbox,
                R.id.nav_proyectos,
                R.id.nav_esperando,
                R.id.nav_agendado,
                R.id.nav_ad,
                R.id.nav_gallery,
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            val jwtHelper: JwtHelper by lazy {
                JwtHelper(this)
            }

            val apiService : ApiService by lazy {
                ApiService.create()
            }
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_logout -> {
                    signOut()
                    true
                }

                R.id.nav_perfil -> {
                    val authHeader = "Bearer ${jwtHelper.getToken()}"
                    apiService.getUserData(authHeader).enqueue(object : Callback<UserInfo> {
                        override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                            if (response.isSuccessful) {
                                val userInfo = response.body()
                                if (userInfo != null) {
                                    val intent = Intent(this@Menu, CompletarRegistro::class.java)
                                    intent.putExtra("nombre", userInfo.nombre)
                                    intent.putExtra("telefono", userInfo.telefono)
                                    intent.putExtra("departamento", userInfo.departamento)
                                    intent.putExtra("puesto", userInfo.puesto)
                                    startActivity(intent)
                                }
                            } else {
                                // Maneja el caso en que la respuesta HTTP no es exitosa
                            }
                        }

                        override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                            // Maneja el caso en que la llamada a la API falla
                        }
                    })
                    true
                }

                else -> {
                    // Delega la navegación al NavController para los demás elementos del menú
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)

                    // Cierra el Navigation Drawer si se manejó el elemento del menú
                    if (handled) {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }

                    handled
                }
            }
        }
    }
    private fun signOut() {
        val preferences = PreferenceHelper.defaultPrefs(this)
        val editor = preferences.edit()


        val urlList = preferences.getStringSet("urlList", emptySet())



        // Limpia todas las preferencias
        editor.clear()
        editor.apply()  // Aplicar el clear antes de continuar

        // Si urlBase no es null, lo guarda de nuevo en las preferencias limpias
        if (urlList != null) {
            with(preferences.edit()){
                putStringSet("urlList", urlList)
                apply()
            }
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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