
package com.namma.homestay

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Inquiry(
    val name: String,
    val initial: String,
    val avatarColor: Color,
    val from: String,
    val message: String,
    val checkIn: String,
    val checkOut: String,
    val total: String,
    val phone: String,
    val isNew: Boolean,
    val guests: Int
)

@Composable
fun GuestScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showReplyDialog by remember { mutableStateOf<String?>(null) }
    var replyText by remember { mutableStateOf("") }

    val inquiries = listOf(
        Inquiry("Rahul Patil", "RP", Color(0xFF1565C0), "Pune",
            "We want to try bamboo curry and see the farm. Is the room available this weekend?",
            "May 3", "May 5", "3,400", "+919876543210", true, 2),
        Inquiry("Sneha Verma", "SV", Color(0xFFC62828), "Bengaluru",
            "Hi! Travelling solo. Are you comfortable hosting solo female guests? Heard amazing things about your food!",
            "May 7", "May 10", "2,550", "+919765432101", true, 1),
        Inquiry("Arjun Kumar", "AK", Color(0xFF2E7D32), "Mumbai",
            "Thank you for a wonderful stay! The bamboo curry was absolutely unforgettable.",
            "May 1", "May 2", "1,700", "+919654321012", false, 2)
    )

    if (showReplyDialog != null) {
        AlertDialog(
            onDismissRequest = { showReplyDialog = null },
            containerColor = DarkCard,
            title = { Text("Reply to $showReplyDialog", color = WhiteText) },
            text = {
                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    label = { Text("Your message", color = GrayText) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeMain,
                        unfocusedBorderColor = FieldBorder,
                        focusedContainerColor = DarkField,
                        unfocusedContainerColor = DarkField,
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { showReplyDialog = null; replyText = "" },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                ) { Text("Send Reply", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showReplyDialog = null }) {
                    Text("Cancel", color = GrayText)
                }
            }
        )
    }

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
                    Brush.verticalGradient(listOf(Color(0xFF0D1B2A), Color(0xFF152035), DarkBg))
                )
                .padding(20.dp)
        ) {
            Column {
                Text("Inquiry Box", color = WhiteText, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("Manage traveler requests", color = GrayText, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(20.dp), color = OrangeMain) {
                        Text("2 New",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            color = WhiteText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFF2A2A2A)) {
                        Text("1 Completed",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            color = GrayText, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        inquiries.forEach { inquiry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                border = if (inquiry.isNew) BorderStroke(1.5.dp, OrangeMain.copy(alpha = 0.5f))
                else BorderStroke(1.dp, Color(0xFF3A3A3A))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(44.dp).clip(CircleShape)
                                    .background(inquiry.avatarColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(inquiry.initial, color = WhiteText,
                                    fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(inquiry.name, fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp, color = WhiteText)
                                Text(
                                    "From ${inquiry.from} • ${inquiry.guests} guest${if (inquiry.guests > 1) "s" else ""}",
                                    color = GrayText, fontSize = 12.sp
                                )
                            }
                        }
                        if (inquiry.isNew) {
                            Surface(shape = RoundedCornerShape(10.dp), color = OrangeMain) {
                                Text("NEW",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    color = WhiteText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF1A3A1A)) {
                                Text("Done",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    color = Color(0xFF4CAF50), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF232323)) {
                        Text(
                            inquiry.message,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp, color = GrayText, lineHeight = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${inquiry.checkIn} - ${inquiry.checkOut}",
                            fontSize = 12.sp, color = GrayText)
                        Text("Rs. ${inquiry.total}", fontSize = 15.sp,
                            fontWeight = FontWeight.Bold, color = OrangeMain)
                    }

                    if (inquiry.isNew) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL,
                                        Uri.parse("tel:${inquiry.phone}"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null,
                                    modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call", fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { showReplyDialog = inquiry.name; replyText = "" },
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.5.dp, OrangeMain)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null,
                                    modifier = Modifier.size(16.dp), tint = OrangeMain)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reply", color = OrangeMain, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}