package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialPosition
import net.evlikat.hexatrix.utils.SQ3

fun AxialPosition.getX(size: Float): Float {
    val q: Float = this.q.toFloat()
    return size * 3 / 2 * q
}

fun AxialPosition.getY(size: Float): Float {
    val q: Float = this.q.toFloat()
    val r: Float = this.r.toFloat()
    return -size * SQ3 * (r + q / 2)
}