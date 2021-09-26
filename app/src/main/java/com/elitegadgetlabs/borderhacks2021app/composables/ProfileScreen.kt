package com.elitegadgetlabs.borderhacks2021app.composables

import android.util.Log
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.CornerRadius.Companion.Zero
import androidx.compose.ui.geometry.Offset.Companion.Zero
import androidx.compose.ui.geometry.Rect.Companion.Zero
import androidx.compose.ui.geometry.Size.Companion.Zero
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize.Companion.Zero
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.model.content.CircleShape
import com.elitegadgetlabs.borderhacks2021app.R
import com.elitegadgetlabs.borderhacks2021app.ui.theme.Shapes
import com.elitegadgetlabs.borderhacks2021app.ui.theme.appBackgroundColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    var selectedState = remember { mutableStateOf("profile_screen") }

    val coroutineScope = rememberCoroutineScope()


    fun navigate(dest: String){
        coroutineScope.launch {
            delay(1000)
            navController.navigate(dest){
                popUpTo = navController.graph.startDestinationId
                launchSingleTop = true
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        BottomAppBar(
            elevation = 12.dp,
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier.align(
                Alignment.BottomCenter
            )
        ) {
            BottomNavigationItem(
                icon = {
                    Icon(Icons.Outlined.Home, "Home")
                },
                selectedContentColor = appBackgroundColor,
                unselectedContentColor = Color.Black,
                onClick = {
                    Log.d("debug", "Home")
                    selectedState.value = "home_screen"
                    navigate("home_screen")
                },
                selected = selectedState.value == "home_screen"
            )

            BottomNavigationItem(
                icon = {
                    Icon(Icons.Outlined.Place, "Maps")
                },
                selectedContentColor = appBackgroundColor,
                unselectedContentColor = Color.Black,
                onClick = {
                    Log.d("debug", "Maps")
                    selectedState.value = "maps_screen"
                    navigate("maps_screen")
                },
                selected = selectedState.value == "maps_screen"
            )

            BottomNavigationItem(
                icon = {
                    Icon(Icons.Filled.Person, "Profile")
                },
                selectedContentColor = appBackgroundColor,
                unselectedContentColor = Color.Black,
                onClick = {
                    Log.d("debug", "Profile")
                    selectedState.value = "profile_screen"
                    navigate("profile_screen")
                },
                selected = selectedState.value == "profile_screen"
            )


        }
        Box(
            modifier = Modifier
        ) {
            Spacer(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .background(color = appBackgroundColor)
            )

            Column(modifier = Modifier.padding(top = 50.dp, start = 20.dp).align(alignment = Alignment.CenterStart)) {
                Image(
                    painterResource(id = R.drawable.headshot), "Ahmad Ali", modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(5.dp, Color.White, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(text = "Ahmad Ali")
            }



        }


    }
}

