package com.styleme.app.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import com.styleme.app.models.*
import kotlinx.coroutines.delay


object MockRepository {

    var enabled: Boolean = false

    val fakeUser = User(1, "demo_user", "Alex", "Demo", "demo@styleme.app")

    val fakeHairColours = listOf(
        HairColour(id = 1,  colourName = "jet_black",       colourHash = "#0A0A0A", faceShapeId = 10),
        HairColour(id = 2,  colourName = "dark_brown",      colourHash = "#3B1F0E", faceShapeId = 59),
        HairColour(id = 3,  colourName = "medium_brown",    colourHash = "#6B3A2A", faceShapeId = 107),
        HairColour(id = 4,  colourName = "light_brown",     colourHash = "#A0522D", faceShapeId = 160),
        HairColour(id = 5,  colourName = "dirty_blonde",    colourHash = "#C8A96E", faceShapeId = 200),
        HairColour(id = 6,  colourName = "golden_blonde",   colourHash = "#F0C040", faceShapeId = 240),
        HairColour(id = 7,  colourName = "platinum_blonde", colourHash = "#F5E6C8", faceShapeId = 245),
        HairColour(id = 8,  colourName = "strawberry",      colourHash = "#E8846A", faceShapeId = 232),
        HairColour(id = 9,  colourName = "auburn",          colourHash = "#922B21", faceShapeId = 146),
        HairColour(id = 10, colourName = "copper_red",      colourHash = "#C0392B", faceShapeId = 192),
        HairColour(id = 11, colourName = "bright_red",      colourHash = "#E74C3C", faceShapeId = 231),
        HairColour(id = 12, colourName = "sunny_yellow",    colourHash = "#F9CA24", faceShapeId = 249),
        HairColour(id = 13, colourName = "rose_gold",       colourHash = "#E8B4B8", faceShapeId = 232),
        HairColour(id = 14, colourName = "pastel_pink",     colourHash = "#FFB6C1", faceShapeId = 255),
        HairColour(id = 15, colourName = "electric_blue",   colourHash = "#1A73E8", faceShapeId = 26),
        HairColour(id = 16, colourName = "teal",            colourHash = "#009688", faceShapeId = 0),
        HairColour(id = 17, colourName = "violet_purple",   colourHash = "#8E44AD", faceShapeId = 142),
        HairColour(id = 18, colourName = "silver_grey",     colourHash = "#95A5A6", faceShapeId = 149)
    )
    val fakeHairStyles = listOf(
        HairStyle(1,  "straight", "Straight"),
        HairStyle(2,  "wavy",     "Wavy"),
        HairStyle(3,  "curly",    "Curly"),
        HairStyle(4,  "coily",    "Coily / Natural"),
        HairStyle(5,  "bob",      "Bob"),
        HairStyle(6,  "pixie",    "Pixie Cut"),
        HairStyle(7,  "lob",      "Long Bob (Lob)"),
        HairStyle(8,  "layered",  "Layered"),
        HairStyle(9,  "bangs",    "Bangs / Fringe"),
        HairStyle(10, "updo",     "Updo / Bun")
    )

    val fakeFaceShapes = listOf(
        FaceShape(1, "oval",    "Oval"),
        FaceShape(2, "round",   "Round"),
        FaceShape(3, "square",  "Square"),
        FaceShape(4, "heart",   "Heart"),
        FaceShape(5, "oblong",  "Oblong"),
        FaceShape(6, "diamond", "Diamond")
    )

    val fakeModelPictures = listOf(
        ModelPicture(1, "model_straight_short.jpg", null, null, 1,  null, 1, null),
        ModelPicture(2, "model_wavy_medium.jpg",    null, null, 2,  null, 2, null),
        ModelPicture(3, "model_curly_long.jpg",     null, null, 3,  null, 3, null),
        ModelPicture(4, "model_bob_short.jpg",      null, null, 5,  null, 1, null),
        ModelPicture(5, "model_pixie.jpg",          null, null, 6,  null, 1, null),
        ModelPicture(6, "model_layered_long.jpg",   null, null, 8,  null, 3, null),
        ModelPicture(7, "model_bangs_medium.jpg",   null, null, 9,  null, 2, null),
        ModelPicture(8, "model_updo_formal.jpg",    null, null, 10, null, 2, null)
    )

    private var pictureCounter = 100
    fun makeFakePicture(name: String = "photo.jpg") =
        Picture(++pictureCounter, name, "/fake/$name", "1.2 MB", 800, 600, "2024-01-01", null)


    fun makePlaceholderBitmap(colour: Int, label: String): Bitmap {
        val size = 600
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val gradient = RadialGradient(
            size / 2f, size / 2f, size / 1.5f,
            colour, darken(colour, 0.5f), Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(),
            Paint().apply { shader = gradient })
        canvas.drawText(label, size / 2f, size / 2f,
            Paint().apply {
                color = 0xDDFFFFFF.toInt(); textSize = 52f
                textAlign = Paint.Align.CENTER; isAntiAlias = true
            })
        canvas.drawText("(Demo Mode)", size / 2f, size / 2f + 62f,
            Paint().apply {
                color = 0xAAFFFFFF.toInt(); textSize = 30f
                textAlign = Paint.Align.CENTER; isAntiAlias = true
            })
        return bmp
    }

    fun makeStyleBitmap(styleId: Int?): Bitmap {
        val palette = listOf(0xFF7B4FBE, 0xFFE75480, 0xFFFF8C42,
            0xFF1A73E8, 0xFF009688, 0xFF8E44AD)
        val style = fakeHairStyles.find { it.id == styleId }
        return makePlaceholderBitmap(
            palette[(styleId ?: 1) % palette.size].toInt(),
            style?.label ?: "Hair Style"
        )
    }

    private fun darken(color: Int, factor: Float) = Color.rgb(
        (Color.red(color)   * factor).toInt().coerceIn(0, 255),
        (Color.green(color) * factor).toInt().coerceIn(0, 255),
        (Color.blue(color)  * factor).toInt().coerceIn(0, 255)
    )

    suspend fun fakeDelay(ms: Long = 800)     = delay(ms)
    suspend fun fakeLongDelay(ms: Long = 2000) = delay(ms)
}
