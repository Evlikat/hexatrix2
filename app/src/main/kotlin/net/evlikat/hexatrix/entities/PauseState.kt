package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.MoveDirection
import net.evlikat.hexatrix.axial.RotateDirection

/**
 * Created by RSProkhorov on 23.03.2015.
 */
internal class PauseState private constructor(
    private val prevState: GameState,
    hexagonalField: HexagonalField,
    gameEventCallback: GameEventCallback
) : GameState(hexagonalField, gameEventCallback) {
    private var nextState: GameState? = null

    override fun next(): GameState {
        return if (nextState != null) {
            hexagonalField.onGameResumed()
            nextState!!
        } else {
            hexagonalField.onGamePaused()
            this
        }
    }

    override fun cancel() {}
    override fun turn(rotateDirection: RotateDirection): Boolean {
        // next update restores pre-pause state
        nextState = prevState
        return false
    }

    override fun move(direction: MoveDirection, steps: Int): Boolean {
        // next update restores pre-pause state
        nextState = prevState
        return false
    }

    override fun moving(direction: MoveDirection?, steps: Int): Boolean {
        return false
    }

    companion object {
        fun create(
            prevState: GameState,
            hexagonalField: HexagonalField,
            gameEventCallback: GameEventCallback
        ): PauseState {
            if (prevState is PauseState) {
                // to avoid nested pause states
                hexagonalField.onGameFinished()
                return prevState
            }
            return PauseState(prevState, hexagonalField, gameEventCallback)
        }
    }
}