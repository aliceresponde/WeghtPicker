package com.aliceresponde.weghtpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var weight by remember { mutableIntStateOf(80) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Text("Select your weight\n ${weight} Kg",
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                Scale(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.BottomCenter),
                    style = ScaleStyle( scaleWidth = 150.dp),
                ) {
                    weight = it
                }
            }

        }
    }
}
