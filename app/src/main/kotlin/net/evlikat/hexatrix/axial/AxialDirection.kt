package net.evlikat.hexatrix.axial

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
class AxialDirection private constructor(override val q: Int, override val r: Int) : AxialVector {

    companion object {
        var Right = AxialDirection(1, 0)
        var RightBack = AxialDirection(1, -1)
        var Back = AxialDirection(0, -1)
        var Left = AxialDirection(-1, 0)
        var LeftForward = AxialDirection(-1, 1)
        var Forward = AxialDirection(0, 1)
    }
}