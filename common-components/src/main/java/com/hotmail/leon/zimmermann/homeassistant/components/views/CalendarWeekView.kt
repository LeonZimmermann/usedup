package com.hotmail.leon.zimmermann.homeassistant.components.views

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.withClip
import com.hotmail.leon.zimmermann.homeassistant.components.R
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.pow

class CalendarWeekView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var width: Float = 0f
    private var height: Float = 0f

    // Paints
    private val guidelinePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val timeTextPaint = Paint().apply {
        textAlign = Paint.Align.RIGHT
        isAntiAlias = true
    }
    private val dayTextPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    private val entryPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // Left Area
    private val timeTexts: Array<String>
    private val timeTextsMaxWidth: Float
    private var timeYCenterOffset: Float
    private var leftXPadding: Float
    private var leftXSpace: Float

    // Top Area
    private val weekdayFormatter = SimpleDateFormat("E")
    private val weekdayTexts: Array<String> = Array(7) { index ->
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_WEEK] = (index + 2) % 7
        weekdayFormatter.format(calendar.time)
    }
    private var weekdayTextsMaxHeight: Float
    private var year: Int = Calendar.getInstance()[Calendar.YEAR]
    private var weekOfYear: Int = Calendar.getInstance()[Calendar.WEEK_OF_YEAR]
    private var dayTexts: Array<String> = Array(7) { index ->
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.WEEK_OF_YEAR] = weekOfYear
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        calendar[Calendar.DAY_OF_MONTH] += index
        calendar[Calendar.DAY_OF_MONTH].toString()
    }
    private var dayTextsMaxHeight: Float
    private var topYPadding: Float
    private var topYSpace: Float

    // Main Area
    private var weekXSpace: Float = 0f
    private var weekYSpace: Float = 0f
    private val mainYPadding: Float

    // Entries
    private var entryList: MutableList<Entry> = mutableListOf() // Contains all entries
    private var entryRectList: MutableList<Pair<Entry, RectF>> =
        mutableListOf() // Contains all valid entries and the corresponding rect
    private var entryRectRadius: Float

    // Scrolling
    private var yScrollOffset: Float = 0f
    private var yScrollScale: Float = 0.5f
    private var scrollClipRect: RectF = RectF()
    private var flingScrollAnimator: ValueAnimator? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CalendarWeekView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                mainYPadding = getDimension(R.styleable.CalendarWeekView_mainYPadding, dip(40).toFloat())
                leftXPadding = getDimension(R.styleable.CalendarWeekView_leftXPadding, dip(20).toFloat())
                topYPadding = getDimension(R.styleable.CalendarWeekView_topYPadding, dip(40).toFloat())
                guidelinePaint.color = getColor(R.styleable.CalendarWeekView_guidelineColor, Color.LTGRAY)
                guidelinePaint.strokeWidth = getDimension(R.styleable.CalendarWeekView_guidelineWidth, dip(2).toFloat())
                getColor(R.styleable.CalendarWeekView_textColor, Color.BLACK).let {
                    timeTextPaint.color = it
                    dayTextPaint.color = it
                }
                getDimension(R.styleable.CalendarWeekView_textSize, sp(20).toFloat()).let {
                    timeTextPaint.textSize = it
                    dayTextPaint.textSize = it
                }
                entryRectRadius = getDimension(R.styleable.CalendarWeekView_entryRectRadius, dip(8).toFloat())
                entryPaint.color = getColor(
                    R.styleable.CalendarWeekView_entryRectColor,
                    Color.parseColor("#FF444444")
                )
            } finally {
                recycle()
            }
        }

        yScrollOffset = -mainYPadding / 2f

        timeTexts = getTimeTexts()
        timeTextsMaxWidth = calculateMaxWidthOf(timeTexts, timeTextPaint)
        timeYCenterOffset = calculateYCenterOffsetOf(timeTexts, timeTextPaint)

        leftXSpace = timeTextsMaxWidth + leftXPadding
        weekdayTextsMaxHeight = calculateMaxHeightOf(weekdayTexts, dayTextPaint)
        dayTextsMaxHeight = calculateMaxHeightOf(dayTexts, dayTextPaint)
        topYSpace = weekdayTextsMaxHeight + dayTextsMaxHeight + topYPadding
    }

    fun setDate(year: Int, weekOfYear: Int) {
        this.year = year
        this.weekOfYear = weekOfYear
        dayTexts = Array(7) { index ->
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = year
            calendar[Calendar.WEEK_OF_YEAR] = weekOfYear
            calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            (calendar[Calendar.DAY_OF_MONTH] + index).toString()
        }
        dayTextsMaxHeight = calculateMaxHeightOf(dayTexts, dayTextPaint)
        topYSpace = weekdayTextsMaxHeight + dayTextsMaxHeight + topYPadding
        invalidate()
    }

    private fun getTimeTexts(): Array<String> {
        val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT)
        val timeCalendar = Calendar.getInstance().apply {
            this[Calendar.HOUR_OF_DAY] = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
        }
        return Array(25) { index ->
            timeCalendar[Calendar.HOUR_OF_DAY] = index % 24
            timeFormatter.format(timeCalendar.time)
        }
    }

    private fun calculateMaxHeightOf(texts: Array<String>, paint: Paint) = texts.map {
        val rect = Rect()
        paint.getTextBounds(it, 0, it.length, rect)
        rect.height().toFloat()
    }.max() ?: 0f

    private fun calculateMaxWidthOf(texts: Array<String>, paint: Paint) = texts.map {
        val rect = Rect()
        paint.getTextBounds(it, 0, it.length, rect)
        rect.width().toFloat()
    }.max() ?: 0f

    private fun calculateYCenterOffsetOf(texts: Array<String>, paint: Paint) = texts.map {
        val rect = Rect()
        paint.getTextBounds(it, 0, it.length, rect)
        rect.centerY().toFloat()
    }.max() ?: 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w.toFloat()
        height = h.toFloat()
        weekXSpace = width - leftXSpace
        weekYSpace = height - topYSpace
        scrollClipRect = RectF(0f, topYSpace, width, height)
        entryRectList = calculateRectList(entryList)
        invalidate()
    }

    private fun calculateRectList(entryList: List<Entry>): MutableList<Pair<Entry, RectF>> =
        entryList.map {
            val calendar = Calendar.getInstance()
            calendar.time = it.dateFrom
            val fromX = getXForWeekday(calendar[Calendar.DAY_OF_WEEK] - 2)
            val fromY = getYForHourAndMinute(calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])
            calendar.time = it.dateTo
            val toX = getXForWeekday(calendar[Calendar.DAY_OF_WEEK] - 2 + 1)
            val toY = getYForHourAndMinute(calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE])
            Pair(it, RectF(fromX, fromY, toX, toY))
        }.toMutableList()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawVerticalLinesAndDays(it)
            drawHorizontalLinesAndTimes(it)
            drawEntries(it)
        }
    }

    private fun drawHorizontalLinesAndTimes(canvas: Canvas) {
        for (i in 0..24) {
            val y = getYForHour(i)
            canvas.withClip(scrollClipRect) {
                drawLine(leftXSpace, y, leftXSpace + weekXSpace, y, guidelinePaint)
                drawText(
                    timeTexts[i],
                    leftXSpace - leftXPadding / 2f,
                    y - timeYCenterOffset,
                    timeTextPaint
                )
            }
        }
    }

    private fun drawVerticalLinesAndDays(canvas: Canvas) {
        for (i in 0..7) {
            var x = getXForWeekday(i)
            canvas.withClip(scrollClipRect) {
                drawLine(x, topYSpace, x, topYSpace + weekYSpace, guidelinePaint)
            }
            if (i < weekdayTexts.size) {
                x += weekXSpace * (1 / 14f)
                val weekdayY = weekdayTextsMaxHeight + topYPadding * (1 / 3f)
                canvas.drawText(weekdayTexts[i], x, weekdayY, dayTextPaint)
                val dayY = weekdayTextsMaxHeight + dayTextsMaxHeight + topYPadding * (2 / 3f)
                canvas.drawText(dayTexts[i], x, dayY, dayTextPaint)
            }
        }
    }

    private fun drawEntries(canvas: Canvas) {
        canvas.withClip(scrollClipRect) {
            for (entryRect in entryRectList) {
                drawRoundRect(entryRect.second, entryRectRadius, entryRectRadius, entryPaint)
            }
        }
    }

    private fun getXForWeekday(weekday: Int) = leftXSpace + weekXSpace * (weekday / 7f)
    private fun getYForHour(hour: Int) =
        topYSpace + weekYSpace * (hour / (24f * yScrollScale)) - yScrollOffset

    private fun getYForHourAndMinute(hour: Int, minute: Int) =
        topYSpace + weekYSpace * ((hour.toFloat() + (minute / 60f)) / (24f * yScrollScale)) - yScrollOffset

    private val onScaleGestureListener =
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                detector?.scaleFactor?.let { yScrollScale *= (1f / it) }
                if (yScrollScale > 1) yScrollScale = 1f
                else if (yScrollScale < 1 / 4f) yScrollScale = 1 / 4f
                return true
            }
        }

    private val onGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent): Boolean {
            return true
        }

        override fun onScroll(
            event1: MotionEvent?,
            event2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            flingScrollAnimator?.cancel()
            flingScrollAnimator = null
            yScrollOffset += distanceY
            if (yScrollOffset < -mainYPadding / 2f) yScrollOffset = -mainYPadding / 2f
            if (yScrollOffset > (weekYSpace / yScrollScale) - weekYSpace + mainYPadding / 2f)
                yScrollOffset = (weekYSpace / yScrollScale) - weekYSpace + mainYPadding / 2f
            entryRectList = calculateRectList(entryList)
            onScrollListener?.onScroll(yScrollOffset)
            invalidate()
            return true
        }

        override fun onFling(event1: MotionEvent?, event2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            flingScrollAnimator?.cancel()
            flingScrollAnimator = ObjectAnimator.ofFloat(velocityY).apply {
                setFloatValues(velocityY, 0f)
                duration = 2000
                addUpdateListener {
                    val direction = if (velocityY < 0) -1f else 1f
                    yScrollOffset -= ((it.animatedValue as Float).toDouble() / 1000.0).pow(2.0).toFloat() * direction
                    if (yScrollOffset < -mainYPadding / 2f) {
                        yScrollOffset = -mainYPadding / 2f
                        cancel()
                    }
                    if (yScrollOffset > (weekYSpace / yScrollScale) - weekYSpace + mainYPadding / 2f) {
                        yScrollOffset = (weekYSpace / yScrollScale) - weekYSpace + mainYPadding / 2f
                        cancel()
                    }
                    entryRectList = calculateRectList(entryList)
                    onScrollListener?.onScroll(yScrollOffset)
                    invalidate()
                }
                start()
            }
            return true
        }

        override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
            val x = event!!.x - leftXSpace
            var y = event.y - topYSpace - mainYPadding / 2f
            if (x <= 0 || y <= 0) return false
            y = (y + yScrollOffset + mainYPadding / 2f) * 24f * (yScrollScale / weekYSpace)
            val weekday = floor(7f * x / weekXSpace).toInt()
            val hour = floor(y).toInt()
            val minute = floor(60f * (y - floor(y))).toInt()
            val selectedDate = Calendar.getInstance().apply {
                this[Calendar.DAY_OF_WEEK] = weekday + 2
                this[Calendar.HOUR_OF_DAY] = hour
                this[Calendar.MINUTE] = minute
                this[Calendar.SECOND] = 30
                this[Calendar.MILLISECOND] = 0
            }.time
            for ((index, entryRect) in entryRectList.withIndex()) {
                if (selectedDate.after(entryRect.first.dateFrom) && selectedDate.before(entryRect.first.dateTo)) {
                    onEntryClickedListener?.onEntryClicked(entryRect.first, entryRect.second, index)
                    return true
                }
            }
            onDateTimeSelectedListener?.let {
                it.onDateTimeSelected(weekday, hour, minute)
                return true
            }
            return false
        }
    }

    private val gestureDetector: GestureDetector = GestureDetector(context, onGestureListener)
    private val scaleGestureDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, onScaleGestureListener)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    var onEntryClickedListener: OnEntryClickedListener? = null
    var onDateTimeSelectedListener: OnDateTimeSelectedListener? = null
    var onScrollListener: OnScrollListener? = null

    @FunctionalInterface
    interface OnEntryClickedListener {
        fun onEntryClicked(entry: Entry, rect: RectF, index: Int)
    }

    @FunctionalInterface
    interface OnDateTimeSelectedListener {
        fun onDateTimeSelected(weekday: Int, hour: Int, minute: Int)
    }

    @FunctionalInterface
    interface OnScrollListener {
        fun onScroll(scrollOffset: Float)
    }

    data class Entry(
        val dateFrom: Date,
        val dateTo: Date,
        val text: String = "",
        val color: Color = Color()
    ) : Serializable

    /**
     * Stores the raw entries in the field entryList and stores the filtered entries, where the fromDate or the toDate is
     * not the same week as the one displayed by the view, in the entryRectList. The entryRectList is the list of the rects
     * that are displayed in the view. Calls invalidate() to ensure that the view is redrawn.
     */
    fun setEntryList(entryList: MutableList<Entry>) {
        this.entryList = entryList
        this.entryRectList = calculateRectList(entryList.filter {
            val calendar = Calendar.getInstance()
            calendar.time = it.dateFrom
            val fromDateInWeek = calendar[Calendar.YEAR] == year && calendar[Calendar.WEEK_OF_YEAR] == weekOfYear
            calendar.time = it.dateTo
            val toDateInWeek = calendar[Calendar.YEAR] == year && calendar[Calendar.WEEK_OF_YEAR] == weekOfYear
            fromDateInWeek && toDateInWeek
        })
        invalidate()
    }
}