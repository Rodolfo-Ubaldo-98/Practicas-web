package com.example.contactos_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.contactos_app.ui.screens.*
import com.example.contactos_app.ui.theme.Contactos_appTheme
import com.example.contactos_app.viewmodel.ContactViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Contactos_appTheme {
                val navController = rememberNavController()
                val viewModel: ContactViewModel = viewModel()

                NavHost(navController, startDestination = "list") {

                    composable("list") {
                        ContactListScreen(navController, viewModel)
                    }

                    composable("detail/{id}") { backStack ->
                        val id = backStack.arguments?.getString("id")?.toInt() ?: 0
                        ContactDetailScreen(navController, viewModel, id)
                    }

                    composable("form/{id}") { backStack ->
                        val id = backStack.arguments?.getString("id")?.toInt() ?: 0
                        ContactFormScreen(navController, viewModel, id)
                    }
                }
            }
        }
    }
}

