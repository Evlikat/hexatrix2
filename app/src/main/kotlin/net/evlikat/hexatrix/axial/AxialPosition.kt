package net.evlikat.hexatrix.axial

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
data class AxialPosition(
    override val q: Int,
    override val r: Int
) : AxialVector {

    constructor(position: AxialPosition) : this(position.q, position.r)

    operator fun plus(otherPos: AxialVector): AxialPosition {
        return AxialPosition(q + otherPos.q, r + otherPos.r)
    }

    override fun toString(): String {
        return String.format("[%d, %d]", q, r)
    }

    val line: Int
        get() = r + q / 2
}