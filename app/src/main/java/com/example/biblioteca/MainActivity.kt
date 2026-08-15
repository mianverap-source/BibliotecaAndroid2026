package com.example.biblioteca

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.biblioteca.data.SessionManager
import com.example.biblioteca.data.local.DatabaseHelper
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Configuramos los destinos de nivel superior
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.catalogoFragment, R.id.misPrestamosFragment, R.id.historialFragment, R.id.perfilFragment),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Lógica de Auto-Login y Sesión
        val sessionManager = SessionManager(this)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if (sessionManager.getUserEmail() != null) {
            navGraph.setStartDestination(R.id.catalogoFragment)
            actualizarHeader(navView)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
        }
        navController.graph = navGraph

        // Bloquear el drawer en Login y Registro
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.registroFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                actualizarHeader(navView) // Refrescar por si cambió la foto
            }
        }

        // Manejar clics personalizados en el menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    sessionManager.clearSession()
                    navController.navigate(R.id.loginFragment)
                    drawerLayout.closeDrawers()
                    true
                }
                else -> {
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    if (handled) drawerLayout.closeDrawers()
                    handled
                }
            }
        }
    }

    private fun actualizarHeader(navView: NavigationView) {
        val sessionManager = SessionManager(this)
        val email = sessionManager.getUserEmail() ?: return
        val headerView = navView.getHeaderView(0)
        
        val tvNombre = headerView.findViewById<TextView>(R.id.tvHeaderNombre)
        val tvCorreo = headerView.findViewById<TextView>(R.id.tvHeaderCorreo)
        val ivFoto = headerView.findViewById<ImageView>(R.id.ivHeaderFoto)

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                DatabaseHelper(this@MainActivity).obtenerUsuarioPorCorreo(email)
            }
            if (user != null) {
                tvNombre.text = user.nombreCompleto
                tvCorreo.text = user.correo
                if (user.fotoUri != null) {
                    ivFoto.load(user.fotoUri) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            (it as NavHostFragment).navController
        } ?: return false
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}