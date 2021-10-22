package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.views.Levels
import org.andengine.entity.text.Text
import org.andengine.opengl.font.IFont
import org.andengine.opengl.vbo.VertexBufferObjectManager
import org.andengine.util.color.Color
import kotlin.math.pow

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
class GameSession(
    levels: Levels,
    pX: Float,
    pY: Float,
    pFont: IFont?,
    pVertexBufferObjectManager: VertexBufferObjectManager?
) : Text(pX, pY, pFont, "", 80, pVertexBufferObjectManager) {
    private var score = 0
    private val levels: Levels
    fun addScore(linesRemoved: Int): Int {
        setScore(score + 2.0.pow((linesRemoved - 1).toDouble()).toInt())
        return score
    }

    private fun setScore(score: Int) {
        this.score = score
        levels.setLevelByScore(score)
        val level: Int = levels.currentLevelNum
        val scoreToNext: Int = levels.scoreToNext
        this.text = String.format(
            """
                    SCORE
                    %d
                    LEVEL
                    %d
                    TO NEXT
                    %d
                    
                    """.trimIndent(),
            this.score, level, scoreToNext
        )
    }

    fun getScore(): Int {
        return score
    }

    fun resetResults() {
        setScore(0)
    }

    init {
        setColor(Color.WHITE)
        this.levels = levels
        setScore(0)
    }
}