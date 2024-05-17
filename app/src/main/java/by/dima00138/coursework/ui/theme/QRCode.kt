package by.dima00138.coursework.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun QRCodeComposable(
    data: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center
) {
    val bitmap = generateQRCodeBitmap(data)

    Image(
        alignment = alignment,
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "QR Code",
        modifier = modifier
    )
}

private fun generateQRCodeBitmap(data: String): Bitmap {
    val writer = QRCodeWriter()
    val size = 300
    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size)
    val pixels = IntArray(size * size)
    for (y in 0 until size) {
        val offset = y * size
        for (x in 0 until size) {
            pixels[offset + x] = if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb()
        }
    }
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
    return bitmap
}