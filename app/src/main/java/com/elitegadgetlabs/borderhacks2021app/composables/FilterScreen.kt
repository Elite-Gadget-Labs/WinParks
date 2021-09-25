package com.elitegadgetlabs.borderhacks2021app.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elitegadgetlabs.borderhacks2021app.ui.theme.appBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun FilterScreen(navController: NavController) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Filters",
                    color = White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        backgroundColor = appBackgroundColor
    )
}