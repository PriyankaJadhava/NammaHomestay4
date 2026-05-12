
package com.namma.homestay

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(onLogout: () -> Unit) {

    val scrollState = rememberScrollState()

    val DarkBg = Color(0xFF121212)
    val DarkCard = Color(0xFF1E1E1E)
    val WhiteText = Color(0xFFFFFFFF)
    val GrayText = Color(0xFFB0B0B0)
    val OrangeMain = Color(0xFFFF9800)

    val checklistItems = listOf(
        Triple("Cleanliness & Hygiene", true, "🧹"),
        Triple("Safe Drinking Water", true, "💧"),
        Triple("Clean Bathroom", true, "🚿"),
        Triple("Home-cooked Meals", true, "🍽️"),
        Triple("First Aid Kit", false, "🩹"),
        Triple("Mosquito Protection", true, "🦟")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(scrollState)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2A1A0A), Color(0xFF3A2010), DarkBg)
                    )
                )
                .padding(20.dp)
        ) {

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text("Namma HomeStay", color = WhiteText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Kamalamma Homestay", color = GrayText, fontSize = 13.sp)
                        Text("Malpe, Udupi", color = GrayText, fontSize = 12.sp)
                    }

                    IconButton(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            onLogout()
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = GrayText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Verification Checklist",
            modifier = Modifier.padding(16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = WhiteText
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                checklistItems.forEach { (label, verified, emoji) ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(emoji, fontSize = 18.sp)

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            label,
                            modifier = Modifier.weight(1f),
                            color = WhiteText
                        )

                        Text(
                            if (verified) "Verified" else "Pending",
                            color = if (verified) Color.Green else OrangeMain
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}