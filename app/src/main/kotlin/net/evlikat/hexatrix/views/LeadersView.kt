package net.evlikat.hexatrix.views

import net.evlikat.hexatrix.scores.IScoreStorage
import net.evlikat.hexatrix.utils.SpriteUtils
import org.andengine.engine.Engine
import org.andengine.engine.camera.Camera
import org.andengine.entity.scene.background.Background
import org.andengine.entity.text.Text
import org.andengine.input.touch.TouchEvent
import org.andengine.opengl.font.IFont
import java.lang.StringBuilder
import java.text.SimpleDateFormat

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Oct 20, 2014)
 */
class LeadersView(
    engine: Engine,
    camera: Camera,
    font: IFont?,
    private val leadersCallback: LeadersCallback,
    private val scoreStorage: IScoreStorage
) : GameView(engine, camera) {
    private val table: Text = Text(
        30.0f,
        30.0f,
        font,
        "",
        800,
        engine.vertexBufferObjectManager
    )
    private var resultsTable: String? = null
    private val background: Background = SpriteUtils.BACKGROUND

    override fun populate() {
        scene.background = background
        scene.attachChild(table)
    }

    override fun onTouch(pSceneTouchEvent: TouchEvent) {
        leadersCallback.toMenuView()
    }

    override fun update() {}

    fun updateResults() {
        val result = StringBuilder("##   SCORE          DATE\n")
        var i = 1
        for (score in scoreStorage.topScores) {
            result.append(
                String.format(
                    "%02d    %04d    %s", i++, score.amount, DATE_FORMAT.format(score.date)
                )
            ).append("\n")
        }
        resultsTable = result.toString()
        table.text = resultsTable
    }

    override fun onBackPressed() {
        leadersCallback.toMenuView()
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm")
    }

}