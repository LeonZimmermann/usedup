package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import org.jetbrains.anko.dip
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

class CropImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var width = 0f
    private var height = 0f

    private var image: Bitmap? = null
    private var imageMargin: Pair<Int, Int> = Pair(0, 0)
    private var imageMatrix = Matrix()
    private var imagePaint = Paint()

    private var cropRect: Rect = Rect()
    private var cropRectSize: Pair<Int, Int> = Pair(0, 0)
    private var cropRectPosition: Pair<Int, Int> = Pair(0, 0)

    private var cropRectStrokePaint = Paint().apply {
        color = Color.parseColor("#FFFFFF")
        style = Paint.Style.STROKE
        strokeWidth = dip(2f).toFloat()
        isAntiAlias = true
    }

    private var cropRectFillPaint = Paint().apply {
        color = Color.parseColor("#44444444")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w.toFloat()
        height = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            image?.let { drawBitmap(it, imageMatrix, imagePaint) }
            drawRect(cropRect, cropRectFillPaint)
            drawRect(cropRect, cropRectStrokePaint)
        }
    }

    fun setImage(image: Bitmap) {
        val imageAspect = image.height / image.width.toFloat()

        var imageWidth = image.height.toFloat()
        var imageHeight = image.width.toFloat()
        if (image.height - width > 0 || image.width - height > 0) {
            if (image.height - width > image.width - height) {
                imageWidth = width
                imageHeight = imageWidth / imageAspect
                Pair(imageWidth, imageHeight)
            } else {
                imageHeight = height
                imageWidth = imageHeight * imageAspect
                Pair(imageWidth, imageHeight)
            }
        }

        val scaleX = imageWidth / image.height.toFloat()
        val scaleY = imageHeight / image.width.toFloat()

        val matrix = Matrix().apply {
            preRotate(90f)
            preScale(scaleX, scaleY)
        }

        this.image = Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)

        val xMargin = width - imageWidth
        val yMargin = height - imageHeight
        imageMargin = Pair(ceil(xMargin).toInt(), ceil(yMargin).toInt())
        imageMatrix.setTranslate(imageMargin.first / 2f, imageMargin.second / 2f)

        val cropRectWidth = imageWidth
        val cropRectHeight = imageWidth / (16 / 9f)
        cropRectSize = Pair(floor(cropRectWidth).toInt(), floor(cropRectHeight).toInt())

        updateCropRectPosition(Pair(width / 2f, height / 2f))
    }

    fun getCroppedImage(): Bitmap {
        val x = (cropRect.left - (width - image!!.width) / 2f).toInt()
        val y = (cropRect.top - (height - image!!.height) / 2f).toInt()
        val width = cropRectSize.first
        val height = cropRectSize.second
        Log.d(
            "IMG_SIZE",
            "cropLeft:${cropRect.left}; cropTop:${cropRect.top}"
        )
        Log.d(
            "IMG_SIZE",
            "x:$x; y:$y; width:$width; height:$height; imageWidth:${image!!.width}; imageHeight:${image!!.height}"
        )
        return Bitmap.createBitmap(image!!, x, y, width, height, matrix, true)
    }

    private fun updateCropRectPosition(cropRectPosition: Pair<Float, Float>) {
        var x = cropRectPosition.first
        if (x > (imageMargin.first + cropRectSize.first) / 2f) x = (imageMargin.first + cropRectSize.first) / 2f
        else if (x < width - (imageMargin.first + cropRectSize.first) / 2f) x =
            width - (imageMargin.first + cropRectSize.first) / 2f

        var y = cropRectPosition.second
        if (y < (imageMargin.second + cropRectSize.second) / 2f) y = (imageMargin.second + cropRectSize.second) / 2f
        else if (y > height - (imageMargin.second + cropRectSize.second) / 2f) y =
            height - (imageMargin.second + cropRectSize.second) / 2f

        this.cropRectPosition = Pair(x.roundToInt(), y.roundToInt())
        cropRect.left = ceil(this.cropRectPosition.first - cropRectSize.first / 2f).toInt()
        cropRect.right = floor(this.cropRectPosition.first + cropRectSize.first / 2f).toInt()
        cropRect.top = ceil(this.cropRectPosition.second - cropRectSize.second / 2f).toInt()
        cropRect.bottom = floor(this.cropRectPosition.second + cropRectSize.second / 2f).toInt()
        invalidate()
    }

    private val onGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            updateCropRectPosition(Pair(cropRectPosition.first - distanceX, cropRectPosition.second - distanceY))
            return true
        }
    }

    private val gestureDetector = GestureDetector(context, onGestureListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }
}