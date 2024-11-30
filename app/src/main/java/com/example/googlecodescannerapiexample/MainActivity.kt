package com.example.googlecodescannerapiexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.googlecodescannerapiexample.ui.theme.GoogleCodeScannerAPIExampleTheme
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : ComponentActivity() {
    private val tag = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val scanner = GmsBarcodeScanning.getClient(this)

        setContent {
            var barcodeData by remember { mutableStateOf("") }
            val scrollState = rememberScrollState()

            val clipboardManager = LocalClipboardManager.current

            GoogleCodeScannerAPIExampleTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "取得したデータ: ",
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .verticalScroll(scrollState),
                            value = barcodeData,
                            onValueChange = {},
                            enabled = false,
                        )

                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            content = { Text("クリップボードへコピー") },
                            onClick = {
                                clipboardManager.setText(AnnotatedString(barcodeData))
                            },
                            enabled = barcodeData != ""
                        )

                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            content = { Text("スキャン") },
                            onClick = {
                                scanner.startScan()
                                    .addOnSuccessListener { barcode ->
                                        val code = barcode.rawValue
                                        if (code == null) {
                                            Log.e(tag, "barcode value is null.")
                                            Toast.makeText(
                                                this@MainActivity,
                                                "取得したデータがnullでした",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()

                                            return@addOnSuccessListener
                                        }

                                        barcodeData = code
                                        Log.d(tag, "code: $code")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(tag, e.toString())
                                        Toast.makeText(
                                            this@MainActivity,
                                            e.toString(),
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                            }
                        )
                    }
                }
            }
        }
    }
}