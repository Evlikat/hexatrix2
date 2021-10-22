package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialDirection
import net.evlikat.hexatrix.axial.MoveDirection
import net.evlikat.hexatrix.axial.RotateDirection
import java.util.*

/**
 * Created by RSProkhorov on 23.03.2015.
 */
internal class FallingBlocksState(
    hexagonalField: HexagonalField,
    gameEventCallback: GameEventCallback
) : GameState(hexagonalField, gameEventCallback) {
    private var framesPassed: Long = 0
    override fun next(): GameState {
        framesPassed++
        if (framesPassed >= gameEventCallback.framesPerTick()) {
            val state = tick()
            framesPassed = 0
            return state
        }
        return this
    }

    private fun tick(): GameState {
        if (hexagonalField.floatFigure == null) {
            return this
        }
        if (hexagonalField.fall()) {
            return this
        }
        val lineNumbersToRemove = hexagonalField.calcLinesToRemove()
        if (lineNumbersToRemove.isEmpty()) {
            return if (hexagonalField.onFigureDropped(0)) this else GameFinishedState(
                hexagonalField,
                gameEventCallback
            )
        }
        val hexagonLines = ArrayList<List<Hexagon>>(
            lineNumbersToRemove.size
        )
        for (lineNumber in lineNumbersToRemove) {
            hexagonLines.add(hexagonalField.getHexagons(lineNumber))
        }
        return RemovingLinesState(hexagonalField, gameEventCallback, hexagonLines)
    }

    override fun cancel() {
        framesPassed = gameEventCallback.framesPerTick() / 2
    }

    override fun turn(rotateDirection: RotateDirection): Boolean {
        return hexagonalField.turnFigure(rotateDirection)
    }

    override fun move(direction: MoveDirection, steps: Int): Boolean {
        val axialDirection = when (direction) {
            MoveDirection.LEFT -> AxialDirection.Left
            MoveDirection.RIGHT -> AxialDirection.RightBack
        }
        return hexagonalField.moveFigure(axialDirection, steps)
    }

    override fun moving(direction: MoveDirection?, steps: Int): Boolean {
        return false
    }
}