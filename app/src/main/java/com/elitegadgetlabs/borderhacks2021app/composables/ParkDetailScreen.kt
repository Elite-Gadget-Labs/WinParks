package com.elitegadgetlabs.borderhacks2021app.composables

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.elitegadgetlabs.borderhacks2021app.HorizontalDottedProgressBar
import com.elitegadgetlabs.borderhacks2021app.MainViewModel
import com.elitegadgetlabs.borderhacks2021app.R
import com.elitegadgetlabs.borderhacks2021app.models.Park
import com.elitegadgetlabs.borderhacks2021app.ui.theme.appBackgroundColor
import com.google.android.material.chip.Chip
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@Composable
fun ParkDetailScreen(navController: NavController, mainViewModel: MainViewModel) {
    val imgRead = remember{
        mutableStateOf(false)
    }
    val localprk = remember{
        mutableStateOf("")
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Card(elevation = 10.dp,backgroundColor = Color.White,modifier = Modifier
            .fillMaxWidth(0.90f)
            .fillMaxHeight(0.90f)
            .align(
                Alignment.Center
            )){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                Text("Brock Park")

                mainViewModel.currentPark.observeForever{
                    imgRead.value = true
                    localprk.value = it.img_url
                }

                if (imgRead.value){
                    CoilImage(
                        imageModel = localprk.value,
                        // Crop, Fit, Inside, FillHeight, FillWidth, None
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
                        shimmerParams = ShimmerParams(
                            baseColor = MaterialTheme.colors.background,
                            highlightColor = Color.LightGray,
                            durationMillis = 350,
                            dropOff = 0.65f,
                            tilt = 20f
                        ),
                        // shows an error ImageBitmap when the request failed.
                        error = ImageBitmap.imageResource(R.drawable.brock_park)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ratingStar()
                    ratingStar()
                    ratingStar()
                    ratingStar()
                    ratingStar()
                    Text(text = "5.0")
                }

                Column() {
                    Text(text = "This small neighbourhood park on the shore of the Detroit River is named after Major General Isaac Brock, a skilled military strategist who led the Upper Canada forces to victory in the War of 1812. Today, the tranquil setting is home to a bike path, benches and picnic tables where you can watch the shipping and recreational activity along the river")
                }
                Row(){
                    Button(
                        onClick = {
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = appBackgroundColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(50.dp)
                            .clip(CircleShape)
                    ) {
                            Text(text = "Show Directions")
                        }
                    }

                Row(horizontalArrangement = Arrangement.SpaceBetween){
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_chair_alt_black_24dp),
                        "bench icon",
                        tint = Color.Black
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_directions_bike_black_24dp),
                        "bike icon",
                        tint = Color.Black
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_table_restaurant_black_24dp),
                        "picnic icon",
                        tint = Color.Black
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_hiking_black_24dp),
                        "picnic icon",
                        tint = Color.Black
                    )
                }
                }



            }
        }



}

@Composable
fun ratingStar(){
    val isFavourite = remember { mutableStateOf(false) }
    IconButton(onClick = { isFavourite.value = !isFavourite.value }) {
        Icon(
            imageVector = if (isFavourite.value) Icons.Outlined.Star else ImageVector.vectorResource(id = R.drawable.ic_star_outline_black_24dp),
            contentDescription = ""
        )
    }
}