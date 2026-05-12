
package com.namma.homestay

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SecretSpot(
    val emoji: String,
    val name: String,
    val distance: String,
    val time: String,
    val description: String,
    val tags: List<String>,
    val accentColor: Color
)

@Composable
fun GuideScreen() {
    val scrollState = rememberScrollState()

    val spots = listOf(
        SecretSpot(
            "💦", "Shivaganga Falls",
            "4.2 km", "12 min drive",
            "Hidden waterfall deep inside coconut grove. Locals only know this!",
            listOf("🤫 Secret", "🌿 Nature"),
            Color(0xFF1565C0)
        ),
        SecretSpot(
            "🌅", "Coconut Hill Viewpoint",
            "2.1 km", "6 min drive",
            "180° view of the Arabian Sea and paddy fields at sunrise.",
            listOf("📸 Viewpoint", "🌄 Sunrise"),
            Color(0xFFE65100)
        ),
        SecretSpot(
            "🌾", "Our Paddy Farm Tour",
            "On-site", "Free entry",
            "Walk through our own paddy and coconut farm. Meet the cows!",
            listOf("🌾 Agri-Tourism", "🐄 Farm"),
            Color(0xFF2E7D32)
        ),
        SecretSpot(
            "🪔", "500-Year-Old Temple",
            "1.8 km", "5 min walk",
            "Ancient Shiva temple hidden in the forest. Very peaceful.",
            listOf("🏛️ Heritage", "🧘 Spiritual"),
            Color(0xFF6A1B9A)
        ),
        SecretSpot(
            "🐟", "Morning Fish Market",
            "3.5 km", "10 min drive",
            "Malpe harbour fish market at 6 AM. Fresh catch, amazing photos!",
            listOf("🐟 Local Life", "📸 Photo Spot"),
            Color(0xFF00838F)
        ),
        SecretSpot(
            "🌊", "Secret Beach Cove",
            "5.0 km", "15 min drive",
            "Isolated cove beach with black rocks. No tourists, just peace.",
            listOf("🏖️ Beach", "🤫 Hidden"),
            Color(0xFF1565C0)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1B2A3B), DarkBg)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    "🗺️ Secret Spots",
                    color = WhiteText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Near Our Homestay • Curated by Kamalamma",
                    color = GrayText,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Places only locals know — not on Google Maps!",
                    color = OrangeMain,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        spots.forEach { spot ->
            SpotCard(spot)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SpotCard(spot: SecretSpot) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Emoji icon box
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(spot.accentColor.copy(alpha = 0.2f))
                    .border(1.dp, spot.accentColor.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(spot.emoji, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    spot.name,
                    color = WhiteText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        "${spot.distance}  ·  ${spot.time}",
                        color = GrayText,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    spot.description,
                    color = GrayText,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    spot.tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = spot.accentColor.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, spot.accentColor.copy(alpha = 0.3f))
                        ) {
                            Text(
                                tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = spot.accentColor.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}