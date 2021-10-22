package net.evlikat.hexatrix.axial

import net.evlikat.hexatrix.entities.IFigure
import java.lang.UnsupportedOperationException
import java.util.ArrayList

/**
 * Logical hexagonal figure
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
class AxialFigure : IFigure {
    private interface CubeRotator {
        fun turn(xyz: CubeCoordinates): CubeCoordinates
    }

    override var position = AxialPosition(0, 0)
    override var parts: Collection<AxialPosition>
    override val partsPositions: Collection<AxialPosition>
        get() {
            val result: MutableList<AxialPosition> = ArrayList()
            result.add(position)
            for (partPos in parts) {
                result.add(AxialPosition(partPos.q + position.q, partPos.r + position.r))
            }
            return result
        }

    constructor() {
        parts = ArrayList()
    }

    constructor(relativeParts: Collection<AxialPosition>) {
        parts = relativeParts
    }

    constructor(position: AxialPosition, absoluteParts: Collection<AxialPosition>) {
        this.position = position
        parts = toRelatives(position, absoluteParts)
    }

    override fun turn(
        forbiddenPositions: Collection<AxialPosition>,
        direction: RotateDirection
    ): Boolean {
        return when (direction) {
            RotateDirection.COUNTERCLOCKWISE -> {
                turn(forbiddenPositions, object : CubeRotator {
                    override fun turn(xyz: CubeCoordinates): CubeCoordinates {
                        return CubeCoordinates(-xyz.z, -xyz.x, -xyz.y)
                    }
                })
            }
            RotateDirection.CLOCKWISE -> {
                turn(forbiddenPositions, object : CubeRotator {
                    override fun turn(xyz: CubeCoordinates): CubeCoordinates {
                        return CubeCoordinates(-xyz.y, -xyz.z, -xyz.x)
                    }
                })
            }
        }
    }

    override fun move(
        forbiddenPositions: Collection<AxialPosition>,
        direction: AxialDirection
    ): Boolean {
        val newPosition = AxialPosition(position.q + direction.q, position.r + direction.r)
        if (forbiddenPositions.contains(newPosition)) {
            // Figure center can't be moved
            return false
        }
        for (partPos in parts) {
            if (forbiddenPositions.contains(partPos.plus(newPosition))) {
                // Figure can't be moved
                return false
            }
        }
        position = newPosition
        return true
    }

    private fun turn(forbiddenPositions: Collection<AxialPosition>, turner: CubeRotator): Boolean {
        val newPartPositions: MutableCollection<AxialPosition> = ArrayList()
        for (partPos in parts) {
            val newPos = fromCubeToAxial(turner.turn(fromAxialToCube(partPos)))
            if (forbiddenPositions.contains(newPos.plus(position))) {
                // Figure can't be turned
                return false
            }
            newPartPositions.add(newPos)
        }
        // If all positions are allowed then turn the figure
        parts = newPartPositions
        return true
    }

    private fun toRelatives(
        position: AxialPosition,
        absoluteParts: Collection<AxialPosition>
    ): Collection<AxialPosition> {
        val relatives: MutableCollection<AxialPosition> = ArrayList()
        for (absolutePosition in absoluteParts) {
            relatives.add(
                AxialPosition(
                    absolutePosition.q - position.q,
                    absolutePosition.r - position.r
                )
            )
        }
        return relatives
    }

    private fun fromCubeToAxial(cubeCoordinates: CubeCoordinates): AxialPosition {
        return AxialPosition(cubeCoordinates.x, cubeCoordinates.z)
    }

    private fun fromAxialToCube(axialCoordinates: AxialPosition): CubeCoordinates {
        return CubeCoordinates(axialCoordinates.q, -axialCoordinates.q - axialCoordinates.r, axialCoordinates.r)
    }
}