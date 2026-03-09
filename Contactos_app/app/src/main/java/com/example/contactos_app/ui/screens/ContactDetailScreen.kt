package com.example.contactos_app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.contactos_app.data.Contact
import com.example.contactos_app.viewmodel.ContactViewModel
import com.example.contactos_app.ui.theme.PrimaryBlue

@Composable
fun ContactDetailScreen(
    navController: NavController,
    viewModel: ContactViewModel,
    id: Int
) {
    var contact by remember { mutableStateOf<Contact?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Colores para los avatares (sincronizados con la lista)
    val avatarColors = listOf(
        Color(0xFF5CB85C), Color(0xFFF0AD4E), Color(0xFFD9534F),
        Color(0xFF5BC0DE), Color(0xFF9B59B6), Color(0xFFE67E22)
    )

    LaunchedEffect(id) {
        contact = viewModel.getById(id)
    }

    contact?.let { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {
            // --- ENCABEZADO AZUL ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
                    .background(PrimaryBlue)
                    .padding(16.dp)
            ) {
                // Botones Superiores (Atrás, Editar, Borrar)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }

                    Row {
                        IconButton(
                            onClick = { navController.navigate("form/${it.id}") },
                            modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { showDialog = true },
                            modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.White)
                        }
                    }
                }

                // Foto y Nombre (Centro)
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (it.photo.isNotEmpty()) {
                        AsyncImage(
                            model = it.photo,
                            contentDescription = null,
                            modifier = Modifier.size(140.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val bgColor = avatarColors[it.id % avatarColors.size]
                        Box(
                            modifier = Modifier.size(140.dp).clip(CircleShape).background(bgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(it.name.take(1).uppercase(), color = Color.White, fontSize = 60.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = it.name,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // --- ACCIONES RÁPIDAS (Llamar, Correo) ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionIconButton(Icons.Default.Phone, "Llamar") {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phone}"))
                    context.startActivity(intent)
                }
                ActionIconButton(Icons.Default.Email, "Correo") {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${it.email}"))
                    context.startActivity(intent)
                }
            }

            // --- TARJETA DE INFORMACIÓN ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    InfoRow(Icons.Default.Email, "Correo Electrónico", it.email)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
                    InfoRow(Icons.Default.Phone, "Teléfono", it.phone)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar contacto") },
            text = { Text("¿Seguro que deseas eliminar a ${contact?.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    contact?.let { viewModel.delete(it) }
                    showDialog = false
                    navController.popBackStack()
                }) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun ActionIconButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp).background(Color(0xFFDCE6FF), CircleShape)
        ) {
            Icon(icon, contentDescription = label, tint = PrimaryBlue, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
    }
}
