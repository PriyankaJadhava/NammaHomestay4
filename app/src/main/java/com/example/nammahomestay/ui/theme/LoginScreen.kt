
package com.namma.homestay

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.google.firebase.auth.FirebaseAuth

// Dark theme colors matching your screenshot
val DarkBg     = Color(0xFF1A1A1A)
val DarkCard   = Color(0xFF2A2A2A)
val DarkField  = Color(0xFF333333)
val OrangeMain = Color(0xFFE07B39)
val WhiteText  = Color(0xFFFFFFFF)
val GrayText   = Color(0xFF9E9E9E)
val FieldBorder= Color(0xFF444444)

@Composable
fun LoginScreen(onSuccess: () -> Unit) {
    val auth       = FirebaseAuth.getInstance()
    var email      by remember { mutableStateOf("") }
    var password   by remember { mutableStateOf("") }
    var showPw     by remember { mutableStateOf(false) }
    var loading    by remember { mutableStateOf(false) }
    var error      by remember { mutableStateOf("") }
    var forgotMode by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var resetOk    by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            // House icon
            Text("🏠", fontSize = 56.sp, textAlign = TextAlign.Center)

            Spacer(Modifier.height(16.dp))

            // Title
            Text(
                if (!forgotMode) "Welcome Back" else "Reset Password",
                fontSize    = 28.sp,
                fontWeight  = FontWeight.Bold,
                color       = WhiteText,
                textAlign   = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                if (!forgotMode) "Login to manage your homestay"
                else "Enter your email to get a reset link",
                fontSize  = 14.sp,
                color     = GrayText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // ── Card ──
            Card(
                Modifier.fillMaxWidth(),
                shape  = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(Modifier.padding(24.dp)) {

                    if (!forgotMode) {

                        // ── LOGIN FORM ──

                        // Email field
                        Text("Phone Number or Email",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = WhiteText)
                        Spacer(Modifier.height(8.dp))
                        DarkTextField(
                            value       = email,
                            hint        = "Enter your phone or email",
                            isPassword  = false,
                            onChange    = { email = it; error = "" }
                        )

                        Spacer(Modifier.height(18.dp))

                        // Password field
                        Text("Password",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = WhiteText)
                        Spacer(Modifier.height(8.dp))
                        DarkTextField(
                            value      = password,
                            hint       = "Enter your password",
                            isPassword = !showPw,
                            onChange   = { password = it; error = "" }
                        )

                        Spacer(Modifier.height(14.dp))

                        // Forgot password link
                        Text(
                            "Forgot Password?",
                            color      = OrangeMain,
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier   = Modifier.clickable {
                                forgotMode = true
                                resetEmail = email
                                error      = ""
                            }
                        )

                        // Error message
                        if (error.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "⚠️  $error",
                                color    = Color(0xFFFF6B6B),
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF3D1A1A))
                                    .padding(12.dp)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // Login button
                        Button(
                            onClick = {
                                when {
                                    email.isBlank()       ->
                                        error = "Please enter your email or phone"
                                    password.isBlank()    ->
                                        error = "Please enter your password"
                                    password.length < 6   ->
                                        error = "Password must be at least 6 characters"
                                    else -> {
                                        loading = true
                                        error   = ""
                                        auth.signInWithEmailAndPassword(
                                            email.trim(), password
                                        )
                                            .addOnSuccessListener {
                                                loading = false
                                                onSuccess()
                                            }
                                            .addOnFailureListener {
                                                loading = false
                                                error = when {
                                                    it.message?.contains("password") == true ->
                                                        "Wrong password. Use Forgot Password."
                                                    it.message?.contains("user") == true ->
                                                        "No account found with this email."
                                                    it.message?.contains("network") == true ->
                                                        "No internet. Check your WiFi."
                                                    else ->
                                                        "Login failed. Check your details."
                                                }
                                            }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OrangeMain
                            ),
                            shape   = RoundedCornerShape(12.dp),
                            enabled = !loading
                        ) {
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier    = Modifier.size(22.dp),
                                    color       = WhiteText,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    "Login",
                                    color      = WhiteText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 16.sp
                                )
                            }
                        }

                    } else {

                        // ── FORGOT PASSWORD FORM ──

                        Text("Your Email",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = WhiteText)
                        Spacer(Modifier.height(8.dp))
                        DarkTextField(
                            value      = resetEmail,
                            hint       = "Enter your email address",
                            isPassword = false,
                            onChange   = {
                                resetEmail = it
                                resetOk    = false
                                error      = ""
                            }
                        )

                        // Success message
                        if (resetOk) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "✅  Reset link sent! Check your inbox and spam folder.",
                                color    = Color(0xFF4CAF50),
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1A2E1A))
                                    .padding(12.dp)
                            )
                        }

                        // Error message
                        if (error.isNotEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "⚠️  $error",
                                color    = Color(0xFFFF6B6B),
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF3D1A1A))
                                    .padding(12.dp)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // Send reset link button
                        Button(
                            onClick = {
                                when {
                                    resetEmail.isBlank()       ->
                                        error = "Please enter your email"
                                    !resetEmail.contains("@") ->
                                        error = "Enter a valid email address"
                                    else -> {
                                        loading = true
                                        auth.sendPasswordResetEmail(resetEmail.trim())
                                            .addOnSuccessListener {
                                                loading  = false
                                                resetOk  = true
                                                error    = ""
                                            }
                                            .addOnFailureListener {
                                                loading  = false
                                                error    = "No account found with this email."
                                            }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            colors  = ButtonDefaults.buttonColors(
                                containerColor = OrangeMain
                            ),
                            shape   = RoundedCornerShape(12.dp),
                            enabled = !loading
                        ) {
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier    = Modifier.size(22.dp),
                                    color       = WhiteText,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    "Send Reset Link",
                                    color      = WhiteText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 16.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        // Back to login
                        OutlinedButton(
                            onClick  = {
                                forgotMode = false
                                error      = ""
                                resetOk    = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape  = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, OrangeMain)
                        ) {
                            Text(
                                "← Back to Login",
                                color      = OrangeMain,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "Namma HomeStay · Host Portal",
                fontSize  = 12.sp,
                color     = GrayText,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Dark styled text field ──────────────────────────────────
@Composable
fun DarkTextField(
    value: String,
    hint: String,
    isPassword: Boolean,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onChange,
        placeholder   = {
            Text(hint, color = GrayText, fontSize = 14.sp)
        },
        modifier      = Modifier.fillMaxWidth(),
        shape         = RoundedCornerShape(12.dp),
        singleLine    = true,
        visualTransformation = if (isPassword)
            PasswordVisualTransformation() else VisualTransformation.None,
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
}