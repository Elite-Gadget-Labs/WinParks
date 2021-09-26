package com.elitegadgetlabs.borderhacks2021app.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elitegadgetlabs.borderhacks2021app.ui.theme.appBackgroundColor
import com.elitegadgetlabs.borderhacks2021app.viewModels.FilterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FilterScreen(navController: NavController, filterViewModel: FilterViewModel) {

    val coroutineScope = rememberCoroutineScope()

    //var filterDialogState = remember { mutableStateOf(filterViewModel.getDialogClosed())}


    Scaffold(



    ) {

        LazyColumn(modifier = Modifier.fillMaxSize()){
            itemsIndexed(filterViewModel.filterList){ index, filter->
                FilterListItem(
                    index = index,
                    filterText = filter.name,
                    filterImageVector = ImageVector.vectorResource(id = filter.iconImageId),
                    filterViewModel
                )
            }
        }
    }


}

@Composable
fun FilterListItem(
    index:Int,
    filterText: String,
    filterImageVector: ImageVector,
    filterViewModel: FilterViewModel
) {
    Log.d("debug", "composed")
    Row(
        modifier = Modifier
            .background(Color.White)
            .height(60.dp)
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = {}){
                Icon(
                    imageVector = filterImageVector,
                    contentDescription = filterText,
                    tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                )
            }

            Text(
                text = filterText,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        val isChecked = rememberSaveable { mutableStateOf(filterViewModel.getFilter(index)) }

        isChecked.value?.let {
            Checkbox(
                modifier = Modifier,
                checked = it,
                onCheckedChange ={
                    isChecked.value = it
                    filterViewModel.setFilter(it, index)
                    Log.d("debug", filterViewModel.getFilter(index).toString())
                }
            )
        }

        
    }
}