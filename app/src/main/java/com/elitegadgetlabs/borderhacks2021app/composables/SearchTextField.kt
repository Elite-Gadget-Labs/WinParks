package com.elitegadgetlabs.borderhacks2021app.composables

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elitegadgetlabs.borderhacks2021app.R
import com.elitegadgetlabs.borderhacks2021app.ui.theme.Shapes
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalComposeUiApi
@Composable
fun SearchTextField(
    queryText: MutableState<TextFieldValue>,
    filterDialogState: MutableState<Boolean>,
    letterList: ArrayList<String>,
    navController: NavController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var filteredLetters: ArrayList<String>

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
        textStyle = TextStyle.Default,
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
}