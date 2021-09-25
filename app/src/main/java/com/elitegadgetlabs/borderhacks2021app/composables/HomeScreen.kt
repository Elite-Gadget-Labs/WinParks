package com.elitegadgetlabs.borderhacks2021app.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.navArgument

@Composable
fun HomeScreen(navController: NavController) {
    Text(text = navController.currentDestination?.route.toString())
}