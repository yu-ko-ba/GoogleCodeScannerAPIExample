package com.example.googlecodescannerapiexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.googlecodescannerapiexample.ui.theme.GoogleCodeScannerAPIExampleTheme
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : ComponentActivity() {
    val TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scanner = GmsBarcodeScanning.getClient(this)
        setContent {
            GoogleCodeScannerAPIExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        var barcodeData by remember { mutableStateOf("") }
                        Button(onClick = {
                            scanner.startScan()
                                .addOnSuccessListener { barcode ->
                                    val code = barcode.rawValue
                                    if (code == null) {
                                        Log.e(TAG, "barcode value is null.")
                                        Toast.makeText(
                                            this@MainActivity,
                                            "barcode value is null.",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                        return@addOnSuccessListener
                                    }
                                    barcodeData = code
                                    Log.d(TAG, "code: $code")
                                }
                                .addOnFailureListener { e ->
                                    Log.e(TAG, e.toString())
                                    Toast.makeText(
                                        this@MainActivity,
                                        e.toString(),
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                        }) {
                            Text(text = "スキャン")
                        }
                        TextField(
                            value = barcodeData,
                            onValueChange = {},
                            label = { Text(text = "取得したデータ: ") },
                            enabled = false
                        )
                    }
                }
            }
        }
    }
}