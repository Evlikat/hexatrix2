package net.evlikat.hexatrix.entities

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 07, 2014)
 */
interface GameEventCallback {
    fun onLinesRemoved(linesRemoved: Int)
    fun reset()
    fun framesPerTick(): Long
}