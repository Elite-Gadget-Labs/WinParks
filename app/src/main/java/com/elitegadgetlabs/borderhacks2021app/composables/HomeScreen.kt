package com.elitegadgetlabs.borderhacks2021app.composables

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
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
import coil.annotation.ExperimentalCoilApi

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun HomeScreen(navController: NavController, mainViewModel: MainViewModel = MainViewModel()) {
    val parks = mainViewModel.getParkData()
    var filterDialogState = remember { mutableStateOf(false) }
    var queryText = remember { mutableStateOf(TextFieldValue("")) }

    val letterList = (parks?.parks as ArrayList<Park>).filter { true}.map { it.name} as ArrayList<String>

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

            items(parks?.parks as ArrayList<Park>){
                VerticalListItem(
                    park = it,
                    Modifier.padding(start = 16.dp, bottom = 16.dp),
                    navController.context
                )
            }
        }


        Spacer(modifier = Modifier.height(30.dp))  //vertical spacer


    }

}