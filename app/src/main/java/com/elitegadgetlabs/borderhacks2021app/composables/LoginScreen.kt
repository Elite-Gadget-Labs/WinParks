package com.elitegadgetlabs.borderhacks2021app.composables

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elitegadgetlabs.borderhacks2021app.HorizontalDottedProgressBar
import com.elitegadgetlabs.borderhacks2021app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavController) { //, mainViewModel: MainViewModel = viewModel()

    val coroutineScope = rememberCoroutineScope()

    //Functions:
    fun showToast(message: String){
        Toast.makeText(navController.context, message, Toast.LENGTH_SHORT).show()
    }

    fun invalidEmailInput(email: String) =
        email.trim().isBlank()

    fun invalidPasswordInput(password: String) =
        password.trim().isBlank()

    fun loginInUser(email: String, password: String){
        FirebaseAuth.getInstance().signOut()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d("debug","Log In successful")

                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    if (firebaseUser.isEmailVerified){
                        coroutineScope.launch {
                            delay(1000)
                            navController.navigate("home_screen"){
                                popUpTo = navController.graph.startDestinationId
                                launchSingleTop = true
                            }
                        }
                    }
                    else{
                        firebaseUser.sendEmailVerification()

                        coroutineScope.launch {
                            delay(1000)
                            navController.navigate("email_confirmation_screen"){
                                popUpTo = navController.graph.startDestinationId
                                launchSingleTop = true
                            }
                        }
                    }

                } else {
                    Log.d("debug","Log In Failed: "+ task.exception?.message.toString())
                    showToast("Log In Failed: "+ task.exception?.message.toString())
                }
            }
    }

    /*
    A Scaffold is a layout which implements the basic material design layout structure.
    You can add things like a TopBar, BottomBar, FAB or a Drawer.
    */
    Scaffold {

        //TextField variables
        var userEmail by remember { mutableStateOf(TextFieldValue("")) }
        var userPassword by remember { mutableStateOf(TextFieldValue("")) }

        var emailHasError by remember { mutableStateOf(false) }
        var passwordHasError by remember { mutableStateOf(false) }

        var isPasswordVisible by remember {
            mutableStateOf(false)
        }

        //PasswordVisualTransformation() is used to mask the characters in a password with asterisks
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }

        /*
        Creating MutableInteractionSources for the email and password TextFields.
        This allows listening and responding to interaction changes inside these components.
         */
        val passwordInteractionState = remember { MutableInteractionSource() }
        val emailInteractionState = remember { MutableInteractionSource() }

        //The LazyColumn is a vertically scrolling list that only composes and lays out its currently visible items.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) } //vertical spacer

            //item { LottieWorkingLoadingView(context = LocalContext.current) } //Main Image/Animation

            item {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Text(
                    text = "We have missed you, Let's start by Sign In!",
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = userEmail,
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Email,
                            "email icon",
                            tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                        )
                    },
                    maxLines = 1,
                    isError = emailHasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text(text = "Email address") },
                    placeholder = { Text(text = "abc@gmail.com") },
                    onValueChange = {
                        userEmail = it
                    },
                    interactionSource = emailInteractionState,
                )
            }
            item {
                OutlinedTextField(
                    value = userPassword,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_key_24),
                            "password icon",
                            tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            isPasswordVisible = !isPasswordVisible

                            passwordVisualTransformation =
                                if (passwordVisualTransformation != VisualTransformation.None) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                        }){
                            Icon(
                                imageVector = if (isPasswordVisible){
                                    ImageVector.vectorResource(id = R.drawable.baseline_visibility_24)
                                }
                                else{
                                    ImageVector.vectorResource(id = R.drawable.baseline_visibility_off_24)
                                    },
                                "eyeSlash icon",
                                tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                            )
                        }
                    },
                    maxLines = 1,
                    isError = passwordHasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "12334444") },
                    onValueChange = {
                        userPassword = it
                    },
                    interactionSource = passwordInteractionState,
                    visualTransformation = passwordVisualTransformation,
                )
            }
            item {
                var loading by remember { mutableStateOf(false) }
                Button(
                    onClick = {

                        emailHasError = invalidEmailInput(userEmail.text)
                        passwordHasError = invalidPasswordInput(userPassword.text)
                        loading = emailHasError == false && passwordHasError==false
                        Log.d("debug", loading.toString())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                ) {
                    if (loading) {
                        HorizontalDottedProgressBar()

                        coroutineScope.launch {
                            delay(3000)
                            loginInUser(userEmail.text.trim(), userPassword.text.trim())
                        }

                    } else {
                        Text(text = "Log In")
                    }
                }
            }
            item {
                Box(modifier = Modifier.padding(vertical = 16.dp)) {
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                    Text(
                        text = "Or",
                        color = Color.LightGray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(MaterialTheme.colors.background)
                            .padding(horizontal = 16.dp)
                    )
                }
            }



            item {
                val primaryColor = MaterialTheme.colors.primary
                val annotatedString = remember {
                    AnnotatedString.Builder("Don't have an account? Register")
                        .apply {
                            addStyle(style = SpanStyle(color = primaryColor), 23, 31)
                        }
                }
                Text(
                    text = annotatedString.toAnnotatedString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable(onClick = {
                            navController.navigate("register_screen"){
                                popUpTo = navController.graph.startDestinationId
                                launchSingleTop = true
                            }
                        }),
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

