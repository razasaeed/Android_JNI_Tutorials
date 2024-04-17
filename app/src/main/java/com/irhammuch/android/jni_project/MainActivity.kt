package com.irhammuch.android.jni_project

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.irhammuch.android.jni_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        val result = sumFromJni(20, 30)
        binding.sampleText.text = "Sum: $result"

        val bitmap = loadImageFromAssets("image.png")
        bitmap?.let {  bitmap ->
            val pixels = bitmapToIntArray(bitmap)
            val processPixels = convertToGrayscale(pixels, bitmap.width, bitmap.height)

            val processedBitmap = Bitmap.createBitmap(processPixels, bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            binding.image.setImageBitmap(processedBitmap)
        }

    }

    /**
     * A native method that is implemented by the 'jni_project' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun sumFromJni(a: Int, b: Int): Int
    external fun convertToGrayscale(pixels: IntArray, width: Int, height: Int): IntArray

    fun loadImageFromAssets(fileName: String): Bitmap? {
        return try {
            this.assets.open(fileName).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun bitmapToIntArray(bitmap: Bitmap): IntArray {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels
    }

    companion object {
        // Used to load the 'jni_project' library on application startup.
        init {
            System.loadLibrary("jni_project")
        }
    }
}