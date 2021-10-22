package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialDirection
import net.evlikat.hexatrix.axial.AxialPosition
import net.evlikat.hexatrix.axial.RotateDirection

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
interface IFigure {
    val position: AxialPosition
    fun move(forbiddenPositions: Collection<AxialPosition>, direction: AxialDirection): Boolean
    fun turn(forbiddenPositions: Collection<AxialPosition>, direction: RotateDirection): Boolean
    val partsPositions: Collection<AxialPosition>
    val parts: Collection<AxialPosition>
}