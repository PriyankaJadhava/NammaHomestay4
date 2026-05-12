
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

data class DishItem(
    val name: String,
    val description: String,
    val emoji: String,
    val isSpecial: Boolean = false
)

@Composable
fun MenuScreen() {
    val db = FirebaseFirestore.getInstance()

    var specialDish      by remember { mutableStateOf("Bamboo Shoot Curry") }
    var showSavedMessage by remember { mutableStateOf(false) }
    var isSaving         by remember { mutableStateOf(false) }
    var showEditDialog   by remember { mutableStateOf(false) }
    var editDish         by remember { mutableStateOf("") }

    // READ from Firebase - using "menus" collection
    LaunchedEffect(Unit) {
        db.collection("menus")
            .document("today")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null && snapshot.exists()) {
                    snapshot.getString("special")?.let { specialDish = it }
                }
            }
    }

    val breakfastItems = listOf(
        DishItem("Akki Rotti",  "Traditional rice flatbread with coconut chutney", "🫓"),
        DishItem("Neer Dosa",   "Soft rice crepes with coastal fish curry",         "🐟", true),
        DishItem("Idli Sambar", "Steamed rice cakes with lentil soup",              "🍚")
    )

    val dinnerItems = listOf(
        DishItem(specialDish,  "Fresh from our farm — today's special!",        "🌿", true),
        DishItem("Kori Rotti", "Coastal chicken curry with crispy rice wafers", "🍗"),
        DishItem("Goli Baje",  "Crispy coastal fritters with coconut chutney",  "🧆")
    )

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            containerColor   = DarkCard,
            title = { Text("Update Today's Special", color = WhiteText) },
            text = {
                OutlinedTextField(
                    value         = editDish,
                    onValueChange = { editDish = it },
                    label         = { Text("What's special today?", color = GrayText) },
                    placeholder   = { Text("e.g. Bamboo Shoot Curry", color = GrayText.copy(0.5f)) },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(12.dp),
                    singleLine    = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor      = OrangeMain,
                        unfocusedBorderColor    = FieldBorder,
                        focusedContainerColor   = DarkField,
                        unfocusedContainerColor = DarkField,
                        focusedTextColor        = WhiteText,
                        unfocusedTextColor      = WhiteText,
                        cursorColor             = OrangeMain
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editDish.isBlank()) return@Button
                        isSaving       = true
                        showEditDialog = false
                        // SAVE to "menus" — same collection as READ
                        db.collection("menus")
                            .document("today")
                            .set(mapOf(
                                "special"   to editDish.trim(),
                                "updatedAt" to System.currentTimeMillis()
                            ))
                            .addOnSuccessListener {
                                isSaving         = false
                                showSavedMessage = true
                                specialDish      = editDish.trim()
                            }
                            .addOnFailureListener { isSaving = false }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                ) { Text("Save", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = GrayText)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1A2A1A), Color(0xFF243024), DarkBg)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text("Today's Menu", color = WhiteText,
                    fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text("Malpe, Udupi - Home-cooked with love",
                    color = GrayText, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { editDish = specialDish; showEditDialog = true },
                        colors  = ButtonDefaults.buttonColors(containerColor = OrangeMain),
                        shape   = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Update Menu", fontWeight = FontWeight.Bold)
                    }
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = OrangeMain, modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF2A2A2A)) {
                    Text("Today's Special: $specialDish",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = OrangeMain, fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold)
                }
                if (showSavedMessage) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF1A3A1A)) {
                        Text("Menu updated successfully!",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color(0xFF4CAF50), fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        DarkSectionHeader("Breakfast")
        breakfastItems.forEach { DarkDishCard(it) }
        Spacer(modifier = Modifier.height(8.dp))
        DarkSectionHeader("Dinner")
        dinnerItems.forEach { DarkDishCard(it) }
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape    = RoundedCornerShape(16.dp),
            colors   = CardDefaults.cardColors(containerColor = DarkCard),
            border   = BorderStroke(1.dp, OrangeMain.copy(alpha = 0.25f))
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically) {
                Column {
                    Text("850 per person", fontSize = 18.sp,
                        fontWeight = FontWeight.Bold, color = OrangeMain)
                    Text("Includes all meals + stay",
                        color = GrayText, fontSize = 12.sp)
                }
                Text("2 rooms open", color = GrayText, fontSize = 13.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DarkSectionHeader(title: String) {
    Text(title,
        modifier   = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        fontSize   = 15.sp, fontWeight = FontWeight.Bold, color = WhiteText)
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun DarkDishCard(dish: DishItem) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(containerColor = DarkCard),
        border   = if (dish.isSpecial) BorderStroke(1.5.dp, OrangeMain)
        else BorderStroke(1.dp, Color(0xFF3A3A3A))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(dish.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(dish.name, fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold, color = WhiteText)
                    if (dish.isSpecial) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(shape = RoundedCornerShape(10.dp), color = OrangeMain) {
                            Text("Special",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp, color = WhiteText,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(dish.description, fontSize = 12.sp, color = GrayText)
            }
        }
    }
}