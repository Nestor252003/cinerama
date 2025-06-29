package com.nestor.cinerama.desing.peliculas.activities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SeatSelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val seatPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }

    private val selectedSeatPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    internal val selectedSeats: MutableList<Int> = mutableListOf()
    private val maxSelectableSeats = 10
    private val totalSeats = 48
    private val columns = 6
    private val seatRadius = 30
    private var seatSelectedListener: OnSeatSelectedListener? = null

    fun setOnSeatSelectedListener(listener: OnSeatSelectedListener) {
        seatSelectedListener = listener
    }

    fun getSelectedSeats(): List<String> {
        return selectedSeats.map { getSeatIdentifier(it) }
    }

    val selectedSeatsRaw: List<Int>
        get() = selectedSeats

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rowCount = Math.ceil(totalSeats / columns.toDouble()).toInt()
        val seatSpacing = 80

        for (i in 0 until totalSeats) {
            val row = i / columns
            val col = i % columns
            val cx = col * seatSpacing + seatRadius + 20f
            val cy = row * seatSpacing + seatRadius + 20f

            canvas.drawCircle(cx, cy, seatRadius.toFloat(), if (i in selectedSeats) selectedSeatPaint else seatPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val seatSpacing = 80
            for (i in 0 until totalSeats) {
                val row = i / columns
                val col = i % columns
                val cx = col * seatSpacing + seatRadius + 20f
                val cy = row * seatSpacing + seatRadius + 20f

                val distance = Math.pow((event.x - cx).toDouble(), 2.0) + Math.pow((event.y - cy).toDouble(), 2.0)
                if (distance <= seatRadius * seatRadius) {
                    if (i in selectedSeats) {
                        selectedSeats.remove(i)
                    } else if (selectedSeats.size < maxSelectableSeats) {
                        selectedSeats.add(i)
                    }
                    invalidate()

                    val seatIdentifiers = selectedSeats.map { getSeatIdentifier(it) }
                    seatSelectedListener?.onSeatSelected(seatIdentifiers)
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun getSeatIdentifier(index: Int): String {
        val row = 'A' + (index / columns)
        val col = (index % columns) + 1
        return "$row$col"
    }

    interface OnSeatSelectedListener {
        fun onSeatSelected(seatIdentifiers: List<String>)
    }
}