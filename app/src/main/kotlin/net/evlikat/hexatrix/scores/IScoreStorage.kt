package net.evlikat.hexatrix.scores

import net.evlikat.hexatrix.views.GameResults

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
interface IScoreStorage {
    fun save(newResult: GameResults)
    val topScores: List<Score>
}