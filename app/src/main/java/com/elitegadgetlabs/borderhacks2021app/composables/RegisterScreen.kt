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
import androidx.compose.material.icons.filled.Person
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elitegadgetlabs.borderhacks2021app.HorizontalDottedProgressBar
import com.elitegadgetlabs.borderhacks2021app.MainViewModel
import com.elitegadgetlabs.borderhacks2021app.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    /*
    A Scaffold is a layout which implements the basic material design layout structure.
    You can add things like a TopBar, BottomBar, FAB or a Drawer.
    */
    Scaffold {
        //TextField variables
        var userName by remember { mutableStateOf(TextFieldValue("")) }
        var userEmail by remember { mutableStateOf(TextFieldValue("")) }
        var userPassword by remember { mutableStateOf(TextFieldValue("")) }
        var userConfirmedPassword by remember { mutableStateOf(TextFieldValue("")) }

        var nameHasError by remember { mutableStateOf(false) }
        var emailHasError by remember { mutableStateOf(false) }
        var passwordHasError by remember { mutableStateOf(false) }
        var confirmedPasswordHasError by remember { mutableStateOf(false) }

        var isPasswordVisible by remember {
            mutableStateOf(false)
        }

        var isConfirmedPasswordVisible by remember {
            mutableStateOf(false)
        }

        //PasswordVisualTransformation() is used to mask the characters in a password with asterisks
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }

        var confirmedPasswordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }

        val coroutineScope = rememberCoroutineScope()

        /*
        Creating MutableInteractionSources for the email and password TextFields.
        This allows listening and responding to interaction changes inside these components.
         */
        val nameInteractionState = remember { MutableInteractionSource() }
        val passwordInteractionState = remember { MutableInteractionSource() }
        val confirmedPasswordInteractionState = remember { MutableInteractionSource() }
        val emailInteractionState = remember { MutableInteractionSource() }

        fun showToast(message: String){
            Toast.makeText(navController.context, message, Toast.LENGTH_SHORT).show()
        }

        fun invalidNameInput(name: String) =
            name.trim().isBlank()

        fun invalidEmailInput(email: String) =
            email.trim().isBlank()

        fun invalidPasswordInput(password: String) =
            password.trim().isBlank()

        fun invalidConfirmedPasswordInput(confirmedPassword: String) =
            confirmedPassword.trim().isBlank() || confirmedPassword.trim() != userPassword.text

        fun registerUser(email:String, password:String){
            FirebaseAuth.getInstance().signOut()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("debug","User is registered successfully")

                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val updateProfileRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(userName.text)
                            .build()

                        firebaseUser.updateProfile(updateProfileRequest)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("debug", "User name updated.")

                                    firebaseUser.sendEmailVerification()

                                    coroutineScope.launch {
                                        delay(1000)
                                        navController.navigate("email_confirmation_screen"){
                                            popUpTo = navController.graph.startDestinationId
                                            launchSingleTop = true
                                        }
                                    }



                                }
                            }


                    } else {
                        Toast.makeText(
                            navController.context,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }


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
                    text = "Sign Up",
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = userName,
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Person,
                            "person icon",
                            tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                        )
                    },
                    maxLines = 1,
                    isError = nameHasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text(text = "Name") },
                    placeholder = { Text(text = "John Doe") },
                    onValueChange = {
                        userName = it
                    },
                    interactionSource = nameInteractionState,
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
                OutlinedTextField(
                    value = userConfirmedPassword,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_key_24),
                            "password icon",
                            tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            isConfirmedPasswordVisible = !isConfirmedPasswordVisible

                            confirmedPasswordVisualTransformation =
                                if (confirmedPasswordVisualTransformation != VisualTransformation.None) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                        }){
                            Icon(
                                imageVector = if (isConfirmedPasswordVisible){
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
                    isError = confirmedPasswordHasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = "Confirm Password") },
                    placeholder = { Text(text = "") },
                    onValueChange = {
                        userConfirmedPassword = it
                    },
                    interactionSource = confirmedPasswordInteractionState,
                    visualTransformation = confirmedPasswordVisualTransformation,
                )
            }

            item {
                var loading by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        nameHasError = invalidNameInput(userName.text)
                        emailHasError = invalidEmailInput(userEmail.text)
                        passwordHasError = invalidPasswordInput(userPassword.text)
                        confirmedPasswordHasError = invalidConfirmedPasswordInput(userConfirmedPassword.text)
                        loading = emailHasError == false && passwordHasError==false && confirmedPasswordHasError==false

                        if (nameHasError){
                            showToast("name must not be left empty")
                        }
                        if (emailHasError){
                            showToast("email must not be left empty")
                        }
                        if (passwordHasError){
                            showToast("password must not be left empty")
                        }
                        if (confirmedPasswordHasError){
                            showToast("password is not confirmed or does not match")
                        }


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

                            registerUser(userEmail.text.trim(), userPassword.text.trim())


                        }

                    } else {
                        Text(text = "Sign Up")
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
                    AnnotatedString.Builder("Already have an account? Log In!")
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
                            navController.navigate("login_screen") {
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