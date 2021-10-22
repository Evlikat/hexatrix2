package net.evlikat.hexatrix.views

import java.util.*

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 15, 2014)
 */
class Levels {
    private var iterator: Iterator<Map.Entry<Int, Int>> = LEVELS.entries.iterator()
    var currentLevelNum = 0
        private set
    private var currentLevel: Map.Entry<Int, Int>
    private var nextLevel: Map.Entry<Int, Int>
    fun reset() {
        this.currentLevelNum = 0
        iterator = LEVELS.entries.iterator()
        this.currentLevel = iterator.next()
        nextLevel = iterator.next()
    }

    fun framesPerTick(): Long {
        return currentLevel.value.toLong()
    }

    fun setLevelByScore(totalScore: Int) {
        if (totalScore >= nextLevel.key) {
            this.currentLevel = nextLevel
            this.currentLevelNum++
            nextLevel = if (iterator.hasNext()) iterator.next() else nextLevel
        }
    }

    val scoreToNext: Int
        get() = nextLevel.key

    companion object {
        /**
         * Scores to next level -> milliseconds per tick
         */
        private val LEVELS = mapOf(
            0 to 52,
            30 to 50,
            60 to 48,
            100 to 46,
            150 to 44,
            200 to 42,
            275 to 40,
            350 to 38,
            500 to 36,
            650 to 34,
            800 to 32,
            1000 to 30,
            1200 to 28,
            1500 to 26,
            1750 to 24,
            2000 to 22,
            2500 to 20,
            99999 to 20,
        )
    }

    init {
        this.currentLevel = iterator.next()
        nextLevel = iterator.next()
    }
}