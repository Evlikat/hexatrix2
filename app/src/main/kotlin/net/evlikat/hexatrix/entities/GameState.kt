package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.MoveDirection
import net.evlikat.hexatrix.axial.RotateDirection

/**
 * Created by RSProkhorov on 18.03.2015.
 */
internal abstract class GameState(
    protected val hexagonalField: HexagonalField,
    protected val gameEventCallback: GameEventCallback
) {
    abstract operator fun next(): GameState

    // TODO: remove
    abstract fun cancel()
    abstract fun turn(rotateDirection: RotateDirection): Boolean
    abstract fun move(direction: MoveDirection, steps: Int): Boolean
    abstract fun moving(direction: MoveDirection?, steps: Int): Boolean
}