
package com.namma.homestay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppRoot() }
    }
}

@Composable
fun AppRoot() {
    var showSplash by remember { mutableStateOf(true) }
    val auth = FirebaseAuth.getInstance()
    var loggedIn by remember { mutableStateOf(auth.currentUser != null) }

    LaunchedEffect(Unit) {
        delay(2500)
        showSplash = false
    }

    val state = when {
        showSplash -> "splash"
        !loggedIn  -> "login"
        else       -> "main"
    }

    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(tween(500)) togetherWith fadeOut(tween(300))
        },
        label = "app_root"
    ) { s ->
        when (s) {
            "splash" -> SplashScreen()
            "login"  -> LoginScreen(onSuccess = { loggedIn = true })
            else     -> MainScreen(onLogout = {
                auth.signOut()
                loggedIn = false
            })
        }
    }
}

@Composable
fun SplashScreen() {
    val scale = remember { Animatable(0.3f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        alpha.animateTo(1f, tween(800))
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                Modifier
                    .size(130.dp)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                    }
                    .clip(CircleShape)
                    .background(OrangeMain),
                contentAlignment = Alignment.Center
            ) {
                Text("🏠", fontSize = 64.sp)
            }

            Spacer(Modifier.height(28.dp))

            Text(
                "Namma HomeStay",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = WhiteText,
                modifier = Modifier.graphicsLayer { this.alpha = alpha.value }
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "HOST PORTAL",
                fontSize = 13.sp,
                color = GrayText,
                letterSpacing = 4.sp,
                modifier = Modifier.graphicsLayer { this.alpha = alpha.value }
            )

            Spacer(Modifier.height(60.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = OrangeMain,
                strokeWidth = 2.5.dp
            )

            Spacer(Modifier.height(14.dp))

            Text(
                "Loading your homestay...",
                color = GrayText,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MainScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = DarkBg,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF2A2A2A),
                tonalElevation = 0.dp
            ) {
                listOf(
                    Triple("Home",   Icons.Filled.Home,              0),
                    Triple("Menu",   Icons.AutoMirrored.Filled.List, 1),
                    Triple("Guests", Icons.Filled.MailOutline,       2),
                    Triple("Guide",  Icons.Filled.LocationOn,        3),
                ).forEach { (title, icon, idx) ->
                    NavigationBarItem(
                        selected = selectedTab == idx,
                        onClick  = { selectedTab = idx },
                        icon = {
                            Box {
                                Icon(
                                    icon,
                                    contentDescription = title,
                                    modifier = Modifier.size(24.dp)
                                )
                                if (idx == 2 && selectedTab != 2) {
                                    Box(
                                        Modifier
                                            .size(8.dp)
                                            .align(Alignment.TopEnd)
                                            .clip(CircleShape)
                                            .background(OrangeMain)
                                    )
                                }
                            }
                        },
                        label = {
                            Text(
                                title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = OrangeMain,
                            selectedTextColor   = OrangeMain,
                            indicatorColor      = Color(0xFF3A3A3A),
                            unselectedIconColor = GrayText,
                            unselectedTextColor = GrayText
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> HomeScreen(onLogout)
                1 -> MenuScreen()
                2 -> GuestScreen()
                3 -> GuideScreen()
            }
        }
    }
}