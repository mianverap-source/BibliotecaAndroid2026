package com.example.biblioteca

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.biblioteca.data.SessionManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sessionManager = SessionManager(this)
        
        // Configurar el destino inicial dinámicamente
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        if (sessionManager.getUserEmail() != null) {
            navGraph.setStartDestination(R.id.catalogoFragment)
        } else {
            navGraph.setStartDestination(R.id.loginFragment)
        }
        
        navController.graph = navGraph
    }
}