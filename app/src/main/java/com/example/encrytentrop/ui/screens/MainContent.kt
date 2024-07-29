package com.example.encrytentrop.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.encrytentrop.components.ScaleButton

@Composable
fun MainContent(
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {

        ScaleButton(onClick = {}) {
            Icon(Icons.Filled.ShoppingCart, "")
            Spacer(Modifier.padding(8.dp))
            Text(text = "Add to cart!")
        }

    }

}
