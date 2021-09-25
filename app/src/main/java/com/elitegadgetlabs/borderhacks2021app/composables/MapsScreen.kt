package com.elitegadgetlabs.borderhacks2021app.composables


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.elitegadgetlabs.borderhacks2021app.MainViewModel
import com.elitegadgetlabs.borderhacks2021app.R
import com.elitegadgetlabs.borderhacks2021app.components.GMap
import com.elitegadgetlabs.borderhacks2021app.ui.theme.Shapes
import com.elitegadgetlabs.borderhacks2021app.ui.theme.appBackgroundColor
import com.elitegadgetlabs.borderhacks2021app.viewModels.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.JdkConstants
import java.util.*
import kotlin.collections.ArrayList


@ExperimentalComposeUiApi
@Composable
fun MapsScreen(navController: NavController, mainViewModel: MainViewModel = MainViewModel()) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var selectedState = remember { mutableStateOf("maps_screen")}

    var filterDialogState = remember { mutableStateOf(false)}

    var queryText = remember { mutableStateOf(TextFieldValue("")) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val letterList = arrayListOf<String>("apple", "banana", "pineapple", "orange", "pomegranate")
    var filteredLetters: ArrayList<String>


    Box(modifier = Modifier.fillMaxSize()) {


        GMap(
            modifier = Modifier.fillMaxSize()
        ) { googleMap ->

            val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(navController.context as Activity)
//                            googleMap.setOnMapClickListener(this@MainActivity)

            if (ActivityCompat.checkSelfPermission(
                    navController.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    navController.context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    navController.context as Activity,
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
                return@GMap
            }
            googleMap.isMyLocationEnabled = true
            fusedLocationProviderClient.lastLocation.addOnSuccessListener(navController.context as Activity) { location ->
                if (location != null) {
                    val currentLatLng =
                        LatLng(location.latitude, location.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(currentLatLng)
                            .title("Current Location")
                    )
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            15f
                        )
                    )
                } else {
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                42.317432,
                                -83.026772
                            ), 15f
                        )
                    )
                }
            }

            val parks = mainViewModel.getParkData()

            for (park in parks!!.parks) {
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(park.location[0], park.location[1]))
                        .title(park.name)
                )
            }

            val layer = mainViewModel.getParkGeoData(googleMap)

            for (feature in layer?.features!!) {
                Log.d("debug", feature.getProperty("ADDRESS"))
                val polygonStyle = GeoJsonPolygonStyle()
                polygonStyle.strokeColor = Color.Red.toArgb()
                polygonStyle.fillColor = Color.Red.toArgb()
                feature.polygonStyle = polygonStyle
            }
            layer.addLayerToMap()


        }

        BottomAppBar(elevation = 0.dp, backgroundColor = MaterialTheme.colors.surface, modifier = Modifier.align(
            Alignment.BottomCenter)) {
            BottomNavigationItem(
                icon = {
                    Icon(Icons.Outlined.Home, "Home")
                },
                selectedContentColor = appBackgroundColor,
                unselectedContentColor = Color.Black,
                onClick = {
                    Log.d("debug", "Home")
                    selectedState.value = "home"
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
                    selectedState.value = "Maps"
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
                    selectedState.value = "profile"
                },
                selected = selectedState.value == "profile_screen"
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {


            TextField(
                value = queryText.value,

                trailingIcon = {
                    if (queryText.value != TextFieldValue("")) {
                        IconButton(
                            onClick = {
                                queryText.value = TextFieldValue("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                modifier = Modifier
                                    .padding(15.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                    else{
                        IconButton(
                            onClick = {
                                filterDialogState.value = true
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_filter_list_24),
                                contentDescription = "Filter",
                                modifier = Modifier
                                    .padding(15.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                },
                textStyle = androidx.compose.ui.text.TextStyle.Default,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 3.dp, shape = Shapes.medium),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                placeholder = { Text(text = "Search for parks in Windsor...") },
                onValueChange = {
                    queryText.value = it
                },
                singleLine = true,
                shape = Shapes.medium,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    leadingIconColor = Color.Black,
                    trailingIconColor = Color.Black,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                val searchText = queryText.value.text
                filteredLetters = if (searchText.isEmpty()) {
                    arrayListOf()
                } else {
                    val resultList = ArrayList<String>()
                    for (letter in letterList) {
                        if (letter.lowercase(Locale.getDefault())
                                .contains(searchText.lowercase(Locale.getDefault()))
                        ) {
                            resultList.add(letter)
                        }
                    }
                    resultList
                }
                items(filteredLetters) { filteredLetters ->
                    LetterListItem(
                        itemText = filteredLetters
                    ) { selectedLetter ->
                        Toast.makeText(
                            navController.context,
                            selectedLetter,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }


            Spacer(modifier = Modifier.height(30.dp))  //vertical spacer


        }

    }

}


@Composable
fun LetterListItem(
    itemText: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onItemClick(itemText)
            }
            .background(Color.White)
            .height(60.dp)
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = itemText,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}







