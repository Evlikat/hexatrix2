package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.MoveDirection
import net.evlikat.hexatrix.axial.RotateDirection
import net.evlikat.hexatrix.entities.PauseState

/**
 * Created by RSProkhorov on 23.03.2015.
 */
internal class GameFinishedState(
    hexagonalField: HexagonalField,
    gameEventCallback: GameEventCallback
) : GameState(hexagonalField, gameEventCallback) {
    private var framesPassed = 0
    override fun next(): GameState {
        framesPassed++
        if (framesPassed >= 50) {
            hexagonalField.onGameFinished()
            return PauseState.create(this, hexagonalField, gameEventCallback)
        }
        return this
    }

    override fun cancel() {}
    override fun turn(rotateDirection: RotateDirection): Boolean {
        return false
    }

    override fun move(direction: MoveDirection, steps: Int): Boolean {
        return false
    }

    override fun moving(direction: MoveDirection?, steps: Int): Boolean {
        return false
    }
}