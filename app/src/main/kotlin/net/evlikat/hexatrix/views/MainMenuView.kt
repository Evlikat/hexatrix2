package net.evlikat.hexatrix.views

import net.evlikat.hexatrix.Textures
import net.evlikat.hexatrix.scores.IScoreStorage
import net.evlikat.hexatrix.scores.Score
import net.evlikat.hexatrix.utils.SpriteUtils
import org.andengine.engine.Engine
import org.andengine.engine.camera.Camera
import org.andengine.entity.scene.background.Background
import org.andengine.entity.sprite.ButtonSprite
import org.andengine.entity.text.Text
import org.andengine.input.touch.TouchEvent
import org.andengine.opengl.font.IFont
import java.lang.StringBuilder
import java.util.*

/**
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
class MainMenuView(
    engine: Engine,
    camera: Camera,
    textures: Textures,
    font: IFont?,
    private val callback: MenuCallback,
    scoreStorage: IScoreStorage
) : GameView(engine, camera) {
    private var lastScore = -1
    private var topScore = -1
    private val text: Text
    private val background: Background
    private val startButton: ButtonSprite
    private val leadersButton: ButtonSprite
    private val quitButton: ButtonSprite
    override fun populate() {
        scene.background = background
        scene.attachChild(text)
        scene.registerTouchArea(startButton)
        scene.registerTouchArea(leadersButton)
        scene.registerTouchArea(quitButton)
        scene.attachChild(startButton)
        scene.attachChild(leadersButton)
        scene.attachChild(quitButton)
        displayResults()
    }

    override fun update() {}
    fun registerResult(gameResults: GameResults) {
        lastScore = gameResults.score
        topScore = Math.max(topScore, gameResults.score)
        displayResults()
    }

    private fun displayResults() {
        val builder = StringBuilder()
        if (lastScore > -1 && topScore > -1) {
            builder.append("Last score: ").append(lastScore).append("\n")
            builder.append("Top  score: ").append(topScore)
        }
        text.text = builder.toString()
    }

    override fun onTouch(pSceneTouchEvent: TouchEvent) {}
    override fun onBackPressed() {
        callback.quit()
    }

    companion object {
        private fun lastScore(scores: List<Score>): Int {
            var lastScore = -1
            var latestDate: Date? = Date(0)
            for (sc in scores) {
                val date: Date = sc.date
                if (date.after(latestDate)) {
                    latestDate = date
                    lastScore = sc.amount
                }
            }
            return lastScore
        }

        private fun topScore(scores: List<Score>): Int {
            return if (scores.isEmpty()) {
                -1
            } else scores[0].amount
        }
    }

    init {
        text = Text(30f, 30f, font, "", 255, engine.vertexBufferObjectManager)
        background = SpriteUtils.BACKGROUND
        val buttonHeight: Float = camera.height / 5
        val buttonLeft: Float = (camera.width - textures.getStartBtn()!!.width) / 2
        // load scores
        lastScore = lastScore(scoreStorage.topScores)
        topScore = topScore(scoreStorage.topScores)
        //
        var i = 1

        startButton = ButtonSprite(
            buttonLeft, buttonHeight * i++, textures.getStartBtn(),
            engine.vertexBufferObjectManager
        ) { _, _, _ -> callback.toPlayView() }

        leadersButton = ButtonSprite(
            buttonLeft, buttonHeight * i++, textures.getLeadersBtn(),
            engine.vertexBufferObjectManager
        ) { _, _, _ -> callback.toLeadersView() }

        quitButton = ButtonSprite(
            buttonLeft, buttonHeight * i++, textures.getQuitBtn(),
            engine.vertexBufferObjectManager
        ) { _, _, _ -> callback.quit() }
    }
}