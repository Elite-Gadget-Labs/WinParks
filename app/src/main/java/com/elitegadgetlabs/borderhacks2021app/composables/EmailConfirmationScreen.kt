package com.elitegadgetlabs.borderhacks2021app.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.elitegadgetlabs.borderhacks2021app.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EmailConfirmationScreen(navController: NavController) {

    // to keep track if the animation is playing
    // and play pause accordingly
    var isPlaying by remember {
        mutableStateOf(true)
    }
    // for speed
    var speed by remember {
        mutableStateOf(1f)
    }

    // remember lottie composition ,which
    // accepts the lottie composition result
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.confirm_email)
    )


    // to control the animation
    val progress by animateLottieCompositionAsState(
        // pass the composition created above
        composition,

        // Iterates Forever
        iterations = LottieConstants.IterateForever,

        // pass isPlaying we created above,
        // changing isPlaying will recompose
        // Lottie and pause/play
        isPlaying = isPlaying,

        // pass speed we created above,
        // changing speed will increase Lottie
        speed = speed,

        // this makes animation to restart when paused and play
        // pass false to continue the animation at which is was paused
        restartOnPlay = false)

    val currentUser = FirebaseAuth.getInstance().currentUser

    fun showToast(message: String){
        Toast.makeText(navController.context, message, Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) } //vertical spacer

        item { LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(400.dp)
        ) }

        item {
            Text(
                text = "Confirm your email address",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }

        item {
            Text(
                text = "We sent a confirmation email to:",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }

        item {
            if (currentUser != null) {
                Text(
                    text = currentUser.email.toString(),
                    style = MaterialTheme.typography.body1.copy(fontWeight = Bold),
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            }
        }

        item {
            Text(
                text = "Check your email and click on the\nconfirmation link to continue.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = "Ensure you check your spam or junk folder.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            val primaryColor = MaterialTheme.colors.primary
            val annotatedString = remember {
                AnnotatedString.Builder("Resend Email")
                    .apply {
                        addStyle(style = SpanStyle(color = primaryColor), 0, 11)
                    }
            }
            Text(
                text = annotatedString.toAnnotatedString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clickable(onClick = {
                        currentUser?.sendEmailVerification()
                        showToast("Confirmation email sent")
                    }),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = Bold)
            )
        }
    }
}