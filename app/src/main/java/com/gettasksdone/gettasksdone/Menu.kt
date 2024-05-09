package com.gettasksdone.gettasksdone
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
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
        // Comprueba las preferencias compartidas para ver si el fondo debe ser blanco
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val whiteBackground = preferences.getBoolean("whiteBackground", false)

        // Establece el tema correspondiente
        if (whiteBackground) {
            setTheme(R.style.Theme_MyApplication_WhiteBackground)
        } else {
            setTheme(R.style.Theme_MyApplication)
        }

        super.onCreate(savedInstanceState)

        val apiService : ApiService? by lazy {
            ApiService.create()
        }

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMenu.toolbar)
        //Si se arranca en modo solo lectura se esconde el botón de crear tareas y proyectos
        if(apiService == null){
            binding.appBarMenu.fab.hide()
        }
        binding.appBarMenu.fab.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view, 0, 0, R.style.MyPopupMenu) //si se ve raro dejar asi  val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.fab_menu, popupMenu.menu)
            for (i in 0 until popupMenu.menu.size()) {
                val menuItem = popupMenu.menu.getItem(i)
                val color = if (whiteBackground) R.color.black else R.color.white
                val spannableString = SpannableString(menuItem.title)
                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spannableString.length, 0)
                menuItem.title = spannableString
            }
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
                R.id.nav_contextos,
                R.id.nav_completadas,
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
                    if(apiService != null){
                        apiService?.getUserData(authHeader)?.enqueue(object : Callback<UserInfo> {
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
                    }
                    true
                }
                R.id.nav_theme -> {
                    // Cambia el estado del fondo en las preferencias compartidas
                    val editor = preferences.edit()
                    editor.putBoolean("whiteBackground", !whiteBackground)
                    editor.apply()
                    // Encuentra el Switch en el NavigationView
                    val menuItem = binding.navView.menu.findItem(R.id.nav_theme)
                    val switch = (menuItem.actionView as LinearLayout).findViewById<Switch>(R.id.theme_switch)

                    // Actualiza el estado del Switch
                    switch.isChecked = whiteBackground

                    // Recrea la actividad para aplicar el nuevo tema
                    recreate()
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