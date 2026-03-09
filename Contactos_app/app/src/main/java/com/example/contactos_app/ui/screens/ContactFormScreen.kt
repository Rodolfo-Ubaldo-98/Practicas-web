package com.example.contactos_app.ui.screens

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import android.net.Uri
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.contactos_app.data.Contact
import com.example.contactos_app.viewmodel.ContactViewModel
import kotlinx.coroutines.launch

@Composable
fun ContactFormScreen(
    navController: NavController,
    viewModel: ContactViewModel,
    id: Int
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    var nameErrorText by remember { mutableStateOf("") }
    var phoneErrorText by remember { mutableStateOf("") }
    var emailErrorText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Nueva paleta de colores formales (Azules, Grises y Carbón)
    val avatarColors = listOf(
        Color(0xFF455A64), // Azul Grisáceo
        Color(0xFF2C3E50), // Azul Medianoche
        Color(0xFF34495E), // Asfalto Húmedo
        Color(0xFF5D6D7E), // Azul Acero
        Color(0xFF283747), // Carbón Oscuro
        Color(0xFF1B2631)  // Marino Profundo
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = it
        }
    }

    LaunchedEffect(id) {
        if (id != 0) {
            val contact = viewModel.getById(id)
            contact?.let {
                name = it.name
                phone = it.phone
                email = it.email
                if (it.photo.isNotEmpty()) {
                    selectedImageUri = Uri.parse(it.photo)
                }
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // --- BOTÓN REGRESAR ---
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 📷 FOTO O INICIAL CON COLOR
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .clickable { launcher.launch(arrayOf("image/*")) },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Foto",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Color formal basado en el nombre
                        val bgColor = if (name.isNotEmpty()) {
                            avatarColors[name.length % avatarColors.size]
                        } else {
                            Color.LightGray
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            if (name.isNotEmpty()) {
                                Text(
                                    text = name.take(1).uppercase(),
                                    color = Color.White,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Toca para cambiar foto", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 NOMBRE (Máximo 25 caracteres)
            OutlinedTextField(
                value = name,
                onValueChange = { if (it.length <= 25) name = it },
                label = { Text("Nombre") },
                isError = nameError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${name.length} / 25")
                        if (nameError) {
                            Text(
                                text = nameErrorText,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 TELÉFONO (Exactamente 10 números)
            OutlinedTextField(
                value = phone,
                onValueChange = { 
                    if (it.all { char -> char.isDigit() } && it.length <= 10) phone = it 
                },
                label = { Text("Teléfono") },
                isError = phoneError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${phone.length} / 10")
                        if (phoneError) {
                            Text(
                                text = phoneErrorText,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 CORREO
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo (Opcional)") },
                isError = emailError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                supportingText = {
                    if (emailError) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = emailErrorText,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    nameError = false
                    phoneError = false
                    emailError = false

                    scope.launch {
                        var isValid = true

                        if (name.isBlank()) {
                            nameError = true
                            nameErrorText = "El nombre es obligatorio"
                            isValid = false
                        } else if (name.length < 3) {
                            nameError = true
                            nameErrorText = "Debe tener mínimo 3 letras"
                            isValid = false
                        }

                        if (phone.isBlank()) {
                            phoneError = true
                            phoneErrorText = "El teléfono es obligatorio"
                            isValid = false
                        } else if (phone.length != 10) {
                            phoneError = true
                            phoneErrorText = "Debe tener exactamente 10 dígitos"
                            isValid = false
                        }

                        if (email.isNotBlank()) {
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailError = true
                                emailErrorText = "Correo no válido"
                                isValid = false
                            } else {
                                val existingContact = viewModel.getByEmail(email)
                                if (existingContact != null && existingContact.id != id) {
                                    emailError = true
                                    emailErrorText = "Este correo ya está registrado"
                                    isValid = false
                                }
                            }
                        }

                        if (!isValid) return@launch

                        val contact = Contact(
                            id = if (id == 0) 0 else id,
                            name = name,
                            phone = phone,
                            email = email,
                            photo = selectedImageUri?.toString() ?: ""
                        )

                        if (id == 0) viewModel.insert(contact) else viewModel.update(contact)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Contacto")
            }
        }
    }
}
