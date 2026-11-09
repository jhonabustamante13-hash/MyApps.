
package com.eventflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EventFlow() }
    }
}

data class Evento(
    val nombre: String,
    val fecha: String
)

@Composable
fun EventFlow() {
    var pantalla by remember { mutableIntStateOf(0) }
    var nombre by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val eventos = remember {
        mutableStateListOf(
            Evento("Mi cumpleaños", "25 de diciembre"),
            Evento("Vacaciones", "15 de enero")
        )
    }

    val fondo = Brush.verticalGradient(
        listOf(
            Color(0xFF090D20),
            Color(0xFF252044),
            Color(0xFF10172A)
        )
    )

    MaterialTheme(colorScheme = darkColorScheme()) {
        Box(
            Modifier.fillMaxSize().background(fondo)
        ) {
            Box(
                Modifier
                    .offset(240.dp, 70.dp)
                    .size(170.dp)
                    .blur(65.dp)
                    .background(Color(0xFF906AFF), CircleShape)
            )

            Column(
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(20.dp))

                Text(
                    "EVENTFLOW",
                    color = Color(0xFFBCA6FF),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    "Tus momentos,\ntu organización.",
                    color = Color.White,
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(24.dp))

                GlassPanel {
                    Text(
                        "TU ESPACIO PERSONAL",
                        color = Color(0xFFC5B5FF),
                        fontSize = 11.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${eventos.size} eventos",
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Organiza cada detalle.",
                        color = Color.LightGray
                    )
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        if (pantalla == 0) "Mis eventos" else "Preparativos",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = { mostrarDialogo = true }) {
                        Text("+ Añadir", color = Color(0xFFC5B5FF))
                    }
                }

                if (pantalla == 0) {
                    Column(
                        Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        eventos.forEach { evento ->
                            GlassPanel {
                                Text(
                                    "✦ EVENTO",
                                    color = Color(0xFFC5B5FF),
                                    fontSize = 11.sp
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    evento.nombre,
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    evento.fecha,
                                    color = Color.LightGray
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        listOf(
                            "Preparar invitados",
                            "Comprar decoración",
                            "Organizar alimentos",
                            "Preparar presupuesto"
                        ).forEach { tarea ->
                            var hecha by remember {
                                mutableStateOf(false)
                            }
                            GlassPanel {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = hecha,
                                        onCheckedChange = { hecha = it }
                                    )
                                    Text(tarea, color = Color.White)
                                }
                            }
                        }
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.2f),
                            RoundedCornerShape(30.dp)
                        )
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NavButton("✦", "Eventos", pantalla == 0) {
                        pantalla = 0
                    }
                    NavButton("✓", "Tareas", pantalla == 1) {
                        pantalla = 1
                    }
                }
            }

            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    title = { Text("Nuevo evento") },
                    text = {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del evento") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (nombre.isNotBlank()) {
                                eventos.add(
                                    Evento(nombre, "Fecha por definir")
                                )
                                nombre = ""
                                mostrarDialogo = false
                            }
                        }) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            mostrarDialogo = false
                        }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GlassPanel(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(15.dp, RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.16f),
                        Color.White.copy(alpha = 0.045f)
                    )
                )
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.20f),
                RoundedCornerShape(25.dp)
            )
            .padding(18.dp),
        content = content
    )
}

@Composable
fun NavButton(
    icon: String,
    title: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .background(
                if (active) Color.White.copy(alpha = 0.16f)
                else Color.Transparent
            )
            .padding(horizontal = 25.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, color = Color.White, fontSize = 20.sp)
        Text(title, color = Color.White, fontSize = 11.sp)
    }
}
