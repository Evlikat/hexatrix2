package net.evlikat.hexatrix.axial

import java.io.Serializable

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 07, 2014)
 */
interface AxialVector : Serializable {
    val q: Int
    val r: Int
}