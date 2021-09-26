package com.elitegadgetlabs.borderhacks2021app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elitegadgetlabs.borderhacks2021app.components.GMap
import com.elitegadgetlabs.borderhacks2021app.composables.*
import com.elitegadgetlabs.borderhacks2021app.models.Park
import com.elitegadgetlabs.borderhacks2021app.ui.theme.BorderHacks2021AppTheme
import com.elitegadgetlabs.borderhacks2021app.viewModels.FilterViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity(), LifecycleOwner {
    private val filterViewModel: FilterViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        filterViewModel = FilterViewModel()


        //Requesting the user for app permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.READ_PHONE_STATE
            ), 1
        )

        setContent {
            BorderHacks2021AppTheme {

                ShowLoginScreen()


            }
        }
    }
    @ExperimentalComposeUiApi
    @Composable
    fun ShowLoginScreen(){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "park_detail_screen", builder = {
            composable("login_screen", content = { LoginScreen(navController = navController) })
            composable("profile_screen", content = { ProfileScreen(navController = navController) })
            composable("home_screen", content = { HomeScreen(navController = navController, filterViewModel = filterViewModel) })
            composable("park_detail_screen", content = { ParkDetailScreen(navController = navController, "url") })
            composable("register_screen", content = { RegisterScreen(navController = navController) })
            composable("maps_screen", content = { MapsScreen(navController = navController, filterViewModel = filterViewModel, mainViewModel = mainViewModel)})
            composable("email_confirmation_screen", content = { EmailConfirmationScreen(navController = navController) })
        })
    }
}



