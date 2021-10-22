package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialPosition
import net.evlikat.hexatrix.axial.RotateDirection
import net.evlikat.hexatrix.axial.MoveDirection
import java.util.LinkedHashMap

/**
 * Created by RSProkhorov on 23.03.2015.
 */
internal class RemovingLinesState(
    hexagonalField: HexagonalField,
    gameEventCallback: GameEventCallback,
    hexagonLinesToRemove: List<List<Hexagon>>
) : GameState(hexagonalField, gameEventCallback) {
    private var framesPassed = 0
    private val compositeIterator: CompositeIterator<Hexagon>
    private var currentLineIndex: Int? = null
    private val linesRemoved: Int = hexagonLinesToRemove.size
    private var lastDemarcationPoints: MutableMap<Int, Int>? = null

    private fun putLastDemarcationPointsFor(position: AxialPosition?) {
        if (lastDemarcationPoints == null || position!!.line != currentLineIndex) {
            // position is from new line
            lastDemarcationPoints = LinkedHashMap()
            currentLineIndex = position!!.line
        }
        lastDemarcationPoints!![position.q] = position.r
    }

    override fun next(): GameState {
        framesPassed++
        return if (framesPassed >= FRAMES_PER_BLOCK) {
            if (compositeIterator.hasNext()) {
                // while there is a block to remove we remove it
                val hexagon = compositeIterator.next()
                hexagonalField.removeField(hexagon.position)
                putLastDemarcationPointsFor(hexagon.position)
                this
            } else {
                // if there is no more block to remove we drop all other blocks
                if (lastDemarcationPoints != null) {
                    hexagonalField.dropAll(1, lastDemarcationPoints!!)
                }
                if (hexagonalField.onFigureDropped(linesRemoved)) FallingBlocksState(
                    hexagonalField,
                    gameEventCallback
                ) else GameFinishedState(hexagonalField, gameEventCallback)
            }
        } else this
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

    companion object {
        private const val FRAMES_PER_BLOCK = 3
    }

    init {
        compositeIterator = object : CompositeIterator<Hexagon>(hexagonLinesToRemove.iterator()) {
            override fun afterLine() {
                if (lastDemarcationPoints != null) {
                    hexagonalField.dropAll(1, lastDemarcationPoints!!)
                }
            }
        }
    }
}