package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialDirection
import net.evlikat.hexatrix.axial.AxialPosition
import net.evlikat.hexatrix.axial.RotateDirection
import net.evlikat.hexatrix.utils.SQ3
import org.andengine.opengl.texture.region.ITextureRegion
import java.util.*

/**
 * Physical hexagonal figure applied to AndEngine
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
class Figure(
    prototype: IFigure,
    pTextureRegion: ITextureRegion,
    spriteContext: SpriteContext
) : AxialEntity(
    pX = prototype.position.getX(spriteContext.size),
    pY = prototype.position.getY(spriteContext.size),
    width = spriteContext.size * 2,
    height = spriteContext.size * SQ3,
    pTextureRegion = pTextureRegion,
    spriteContext = spriteContext
), IFigure {
    private interface Rotator {
        fun turn(qr: AxialPosition): AxialPosition
    }

    private val hexagonPositions = HashMap<AxialPosition, Hexagon>(prototype.parts.size)
    private var center: AxialPosition = prototype.position
    override var position: AxialPosition
        get() = center
        set(newPosition) {
            center = newPosition
            onMoved(newPosition)
        }

    override val parts: Collection<AxialPosition>
        get() {
            return hexagonPositions.keys
        }

    init {
        for (position in prototype.parts) {
            val hexagon = Hexagon(position, pTextureRegion, spriteContext)
            addPart(position, hexagon)
            hexagon.position = position
        }
    }

    private fun addPart(relativePosition: AxialPosition, hexagon: Hexagon) {
        attachChild(hexagon)
        hexagonPositions[relativePosition] = hexagon
    }

    fun resetParts(newPartPositions: Collection<AxialPosition>) {
        val hexagons: Collection<Hexagon> = ArrayList(hexagonPositions.values)
        if (hexagons.size != newPartPositions.size) {
            throw UnsupportedOperationException("Part reset is not implemented for different size figures")
        }
        hexagonPositions.clear()
        val hexIt = hexagons.iterator()
        for (axialPosition in newPartPositions) {
            val hexagon = hexIt.next()
            hexagonPositions[axialPosition] = hexagon
            hexagon.position = axialPosition
        }
    }

    override fun turn(
        forbiddenPositions: Collection<AxialPosition>,
        direction: RotateDirection
    ): Boolean {
        return when (direction) {
            RotateDirection.COUNTERCLOCKWISE -> {
                turn(forbiddenPositions, object : Rotator {
                    override fun turn(qr: AxialPosition): AxialPosition {
                        return AxialPosition(-qr.r, qr.q + qr.r)
                    }
                })
            }
            RotateDirection.CLOCKWISE -> {
                turn(forbiddenPositions, object : Rotator {
                    override fun turn(qr: AxialPosition): AxialPosition {
                        return AxialPosition(qr.q + qr.r, -qr.q)
                    }
                })
            }
        }
    }

    override fun move(
        forbiddenPositions: Collection<AxialPosition>,
        direction: AxialDirection
    ): Boolean {
        val newFigurePosition = AxialPosition(
            center.q + direction.q,
            center.r + direction.r
        )
        if (forbiddenPositions.contains(newFigurePosition)) {
            // Figure center can't be moved
            return false
        }
        for (partPos in hexagonPositions.keys) {
            if (forbiddenPositions.contains(partPos.plus(newFigurePosition))) {
                // Figure can't be moved
                return false
            }
        }
        position = newFigurePosition
        return true
    }

    override val partsPositions: Collection<AxialPosition>
        get() {
            val result: MutableList<AxialPosition> = ArrayList<AxialPosition>()
            val centerPos = position
            result.add(centerPos)
            for (partPos in hexagonPositions.keys) {
                val q: Int = partPos.q
                val r: Int = partPos.r
                result.add(AxialPosition(q + centerPos.q, r + centerPos.r))
            }
            return result
        }

    private fun turn(forbiddenPositions: Collection<AxialPosition>, rotator: Rotator): Boolean {
        val newPartPositions: MutableMap<Hexagon, AxialPosition> = HashMap<Hexagon, AxialPosition>()
        for ((key, value) in hexagonPositions) {
            val newPos: AxialPosition = rotator.turn(key)
            if (forbiddenPositions.contains(newPos.plus(center))) {
                // Figure can't be turned
                return false
            }
            newPartPositions[value] = newPos
        }
        // If all positions are allowed then turn the figure
        hexagonPositions.clear()
        for ((hexagon, newPosition) in newPartPositions) {
            hexagonPositions[newPosition] = hexagon
            hexagon.position = newPosition
        }
        return true
    }
}