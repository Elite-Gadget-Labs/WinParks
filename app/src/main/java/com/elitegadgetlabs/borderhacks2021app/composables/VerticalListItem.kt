package com.elitegadgetlabs.borderhacks2021app.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.elitegadgetlabs.borderhacks2021app.models.Park
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage
import com.elitegadgetlabs.borderhacks2021app.R
import com.skydoves.landscapist.ShimmerParams

@ExperimentalCoilApi
@Composable
fun VerticalListItem(park: Park, modifier: Modifier = Modifier, context: Context) {
    val typography = MaterialTheme.typography
    Card(
        modifier = modifier
            .fillMaxWidth().clickable{ Toast.makeText(context, park.name, Toast.LENGTH_LONG).show()},
        shape = RoundedCornerShape(12.dp),
        elevation = 10.dp
    ) {

        Column(modifier = Modifier) {
            CoilImage(
                imageModel = park.img_url,
                // Crop, Fit, Inside, FillHeight, FillWidth, None
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.LightGray,
                    durationMillis = 350,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
                // shows an error ImageBitmap when the request failed.
                error = ImageBitmap.imageResource(R.drawable.linxy)
            )

//
            Column(modifier = Modifier.padding(16.dp)){
                Text(
                    text = park.name,
                    style = typography.h6,
                    modifier = Modifier
                )
                Text(
                    text = getFirstNWords(park.description, 8) + "...",
                    style = typography.body2
                )

                Text(
                    text = park.address,
                    style = typography.subtitle2
                )
            }
        }

    }
}

fun getFirstNWords(s: String?, n: Int): String? {
    val sArr: List<String> = s?.split(" ")!!
    var firstStrs = ""
    for (i in 0 until n) firstStrs += sArr[i] + " "
    return firstStrs.trim { it <= ' ' }
}