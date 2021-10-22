package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialDirection
import net.evlikat.hexatrix.axial.AxialPosition
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 *
 * @author Evlikat
 */
class Fields {
    private val hexToPos: MutableMap<Hexagon, AxialPosition>
    private val posToHex: MutableMap<AxialPosition, Hexagon>
    private val lock: Lock = ReentrantLock(true)
    fun put(position: AxialPosition, hexagon: Hexagon) {
        lock.lock()
        try {
            hexToPos[hexagon] = position
            posToHex[position] = hexagon
        } finally {
            lock.unlock()
        }
    }

    operator fun get(position: AxialPosition): Hexagon? {
        return posToHex[position]
    }

    operator fun get(hexagon: Hexagon): AxialPosition? {
        return hexToPos[hexagon]
    }

    val positions: Collection<AxialPosition>
        get() = posToHex.keys
    val hexagons: Collection<Hexagon?>
        get() = hexToPos.keys

    operator fun contains(axialPosition: AxialPosition): Boolean {
        return posToHex.containsKey(axialPosition)
    }

    fun with(fields: Fields): Fields {
        val result = Fields()
        for ((key, value) in hexToPos) {
            result.put(value, key)
        }
        for ((key, value) in fields.hexToPos) {
            result.put(value, key)
        }
        return result
    }

    fun remove(position: AxialPosition?) {
        lock.lock()
        try {
            val hex = posToHex[position]
            posToHex.remove(position)
            hexToPos.remove(hex)
        } finally {
            lock.unlock()
        }
    }

    val isEmpty: Boolean
        get() = hexToPos.isEmpty()

    fun clear() {
        lock.lock()
        try {
            posToHex.clear()
            hexToPos.clear()
        } finally {
            lock.unlock()
        }
    }

    fun moveBatch(
        fallingParts: List<AxialPosition>,
        direction: AxialDirection
    ): MutableList<AxialPosition> {
        lock.lock()
        return try {
            val newHexagonPositions: MutableMap<AxialPosition, Hexagon> = LinkedHashMap()
            for (fallingPart in fallingParts) {
                val hex = posToHex[fallingPart]
                val newPosition = fallingPart.plus(direction)
                // re-set position
                hex!!.position = newPosition
                hexToPos[hex] = newPosition
                // remove all old part positions
                posToHex.remove(fallingPart)
                newHexagonPositions[newPosition] = hex
            }
            // add new positions at once
            posToHex.putAll(newHexagonPositions)
            ArrayList(newHexagonPositions.keys)
        } finally {
            lock.unlock()
        }
    }

    init {
        hexToPos = HashMap()
        posToHex = HashMap()
    }
}