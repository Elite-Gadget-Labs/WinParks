package com.elitegadgetlabs.borderhacks2021app.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navArgument
import com.elitegadgetlabs.borderhacks2021app.MainViewModel
import com.elitegadgetlabs.borderhacks2021app.models.Park

import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.elitegadgetlabs.borderhacks2021app.ui.theme.Shapes
import com.elitegadgetlabs.borderhacks2021app.ui.theme.appBackgroundColor
import com.elitegadgetlabs.borderhacks2021app.viewModels.FilterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//import coil.annotation.ExperimentalCoilApi


@ExperimentalComposeUiApi
@Composable
fun HomeScreen(navController: NavController, filterViewModel: FilterViewModel, mainViewModel: MainViewModel = MainViewModel()) {
    val parks = mainViewModel.getParkData()
    var filterDialogState = remember { mutableStateOf(false) }
    var queryText = remember { mutableStateOf(TextFieldValue("")) }

    val letterList = (parks?.parks as ArrayList<Park>).filter { true}.map { it.name} as ArrayList<String>

    var selectedState = remember { mutableStateOf("home_screen") }

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


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            SearchTextField(
                queryText,
                filterDialogState,
                letterList,
                navController
            )
            Spacer(Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.padding(end = 16.dp)
            ) {

                items(parks?.parks as ArrayList<Park>) {
                    VerticalListItem(
                        park = it,
                        Modifier.padding(start = 16.dp, bottom = 16.dp),
                        navController.context
                    )
                }
            }


            Spacer(modifier = Modifier.height(30.dp))  //vertical spacer


        }

        if (filterDialogState.value){
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth(0.97f)
                    .fillMaxHeight(1f)
                    .align(Alignment.Center)
                    .padding(horizontal = 0.dp, vertical = 12.dp),
                backgroundColor = Color.White
            ){
                Column {
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(color = appBackgroundColor, shape = Shapes.medium),
                        Arrangement.SpaceEvenly
                    ){
                        IconButton(onClick = {
                            filterDialogState.value = false
                        }) {
                            Icon(Icons.Filled.Close, "close icon", tint = Color.White)
                        }

                        Text(text = "Filters", color = Color.White, modifier = Modifier.align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.h6)

                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Filled.Refresh, "refresh icon", tint = Color.White)
                        }
                    }



                    FilterScreen(navController = navController, filterViewModel = filterViewModel)


                }


            }

        }

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
    }



}