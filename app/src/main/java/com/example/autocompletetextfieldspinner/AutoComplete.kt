package com.example.autocompletetextfieldspinner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoComplete() {

    val countries = getListOfCountries()

    var category by remember {
        mutableStateOf(value = "")
    }

    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(value = false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                })
    ) {
        Text(
            modifier = Modifier.padding(
                start = 3.dp,
                bottom = 6.dp
            ),
            text = "Pilih Negara",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .border(
                            width = 1.8.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = category,
                    onValueChange = {
                        category = it
                        expanded = true
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = { expanded = !expanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    },
                )
            }

            AnimatedVisibility(
                visible = expanded
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize.width.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 15.dp
                    ),
                    shape = RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                ) {
                    LazyColumn {
                        val filteredCountries = countries.filter {
                            it.lowercase().contains(category.lowercase()) || it.lowercase()
                                .contains("others")
                        }.sorted()
                        val itemsToDisplay =
                            if (category.isNotEmpty()) filteredCountries else countries.sorted()
                        items(itemsToDisplay) { title ->
                            CategoryItems(title = title) { selectedTitle ->
                                category = selectedTitle
                                expanded = false
                            }
                        }
                    }
                }
            }

        }

    }


}

@Composable
fun CategoryItems(
    title: String, onSelect: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onSelect(title)
        }
        .padding(10.dp)) {
        Text(text = title, fontSize = 16.sp)
    }
}

fun getListOfCountries(): List<String> {
    return Locale.getISOCountries().map { countryCode ->
        val locale = Locale("", countryCode)
        val countryName = locale.displayCountry
        val flagBuilder = StringBuilder()
        countryCode.forEach { char ->
            flagBuilder.appendCodePoint(
                Character.codePointAt(
                    char.toString(), 0
                ) - 0x41 + 0x1F1E6
            )
        }
        val flag = flagBuilder.toString()
        "$countryName $flag"
    }
}






