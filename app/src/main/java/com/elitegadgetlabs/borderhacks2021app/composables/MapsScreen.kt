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
import androidx.compose.ui.graphics.Color.Companion.White
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
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.elitegadgetlabs.borderhacks2021app.MainViewModel
import com.elitegadgetlabs.borderhacks2021app.R
import com.elitegadgetlabs.borderhacks2021app.components.GMap
import com.elitegadgetlabs.borderhacks2021app.models.Park
import com.elitegadgetlabs.borderhacks2021app.ui.theme.Shapes
import com.elitegadgetlabs.borderhacks2021app.ui.theme.appBackgroundColor
import com.elitegadgetlabs.borderhacks2021app.viewModels.FilterViewModel
import com.elitegadgetlabs.borderhacks2021app.viewModels.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.intellij.lang.annotations.JdkConstants
import java.util.*
import kotlin.collections.ArrayList


@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun MapsScreen(
    navController: NavController,
    filterViewModel: FilterViewModel,
    mainViewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var selectedState = remember { mutableStateOf("maps_screen") }
    var markerClicked = remember { mutableStateOf(false) }
    var markerVal = remember { mutableStateOf("apple") }
    var filterDialogState = remember { mutableStateOf(false) }
    var queryText = remember { mutableStateOf(TextFieldValue("")) }
    val letterList = (mainViewModel.parks?.parks as ArrayList<Park>).filter { true }
        .map { it.name } as ArrayList<String>


    val coroutineScope = rememberCoroutineScope()

    fun navigate(dest: String) {
        coroutineScope.launch {
            delay(1000)
            navController.navigate(dest) {
                popUpTo = navController.graph.startDestinationId
                launchSingleTop = true
            }
        }
    }

    @Composable
    fun miniDetail(park: Park) {
        VerticalListItem(
            park = park,
            modifier = Modifier.clickable {  },
            context = navController.context
        )
    }


    Box(modifier = Modifier.fillMaxSize()) {


        GMap(
            modifier = Modifier.fillMaxSize()
        ) { googleMap ->

            val fusedLocationProviderClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(navController.context as Activity)
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

//            mainViewModel.parks.parks.forEachIndexed{index, park ->
//
//            }
            filterViewModel.currentFilters.observeForever { currentFilter ->
                googleMap.clear()
                for (park in mainViewModel.parks.parks) {
                    if (currentFilter?.let { it1 ->
                            park.tags.intersect(it1.asIterable()).isNotEmpty()
                        } == true) {
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(park.location[0], park.location[1]))
                                .title(park.name)
                        )
                    }
                }
            }

            googleMap.setOnMarkerClickListener { marker ->
                markerClicked.value = true
                markerVal.value = marker.title!!
                return@setOnMarkerClickListener true
            }

            googleMap.setOnMapClickListener {
                markerClicked.value = false
            }


//            mainViewModel.parksLayer = mainViewModel.getParkGeoData(googleMap)
//            for (feature in mainViewModel.parksLayer?.features!!) {
////                    Log.d("debug", feature.getProperty("ADDRESS"))
//                val polygonStyle = GeoJsonPolygonStyle()
//                polygonStyle.strokeColor = Color.Red.toArgb()
//                polygonStyle.fillColor = Color.Red.toArgb()
//                feature.polygonStyle = polygonStyle
//            }
//            mainViewModel.parksLayer!!.addLayerToMap()
//
//            mainViewModel.trailsLayer = mainViewModel.getTrailData(googleMap)
//            for (feature in mainViewModel.trailsLayer?.features!!) {
//                val polygonStyle = GeoJsonLineStringStyle()
//                polygonStyle.color = Color.Red.toArgb()
//                feature.lineStringStyle = polygonStyle
//            }
//            mainViewModel.trailsLayer!!.addLayerToMap()


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


            Spacer(modifier = Modifier.height(350.dp))  //vertical spacer

            if (markerClicked.value) {
                miniDetail(park = mainViewModel.parks.parks.filter { park -> park.name == markerVal.value }[0])
            }

        }

        if (filterDialogState.value) {
            Snackbar(
                modifier = Modifier
                    .fillMaxWidth(0.97f)
                    .fillMaxHeight(1f)
                    .align(Alignment.Center)
                    .padding(horizontal = 0.dp, vertical = 12.dp),
                backgroundColor = Color.White
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(color = appBackgroundColor, shape = Shapes.medium),
                        Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = {
                            filterDialogState.value = false
                        }) {
                            Icon(Icons.Filled.Close, "close icon", tint = Color.White)
                        }

                        Text(
                            text = "Filters",
                            color = White,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.h6
                        )

                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Filled.Refresh, "refresh icon", tint = Color.White)
                        }
                    }



                    FilterScreen(navController = navController, filterViewModel = filterViewModel)


                }


            }

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








