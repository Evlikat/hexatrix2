package net.evlikat.hexatrix.views

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
interface ITouchListener {
    fun onFling(x1: Float, y1: Float, x2: Float, y2: Float): Boolean
    fun onMove(x1: Float, y1: Float, x2: Float, y2: Float): Boolean
}