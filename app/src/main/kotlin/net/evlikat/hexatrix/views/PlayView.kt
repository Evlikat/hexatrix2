package net.evlikat.hexatrix.views

import net.evlikat.hexatrix.Textures
import net.evlikat.hexatrix.axial.EmptyFieldGenerator
import net.evlikat.hexatrix.entities.*
import net.evlikat.hexatrix.scores.IScoreStorage
import net.evlikat.hexatrix.utils.SpriteUtils
import org.andengine.engine.Engine
import org.andengine.engine.camera.Camera
import org.andengine.entity.scene.background.Background
import org.andengine.entity.sprite.Sprite
import org.andengine.input.touch.TouchEvent
import org.andengine.opengl.font.IFont
import org.andengine.opengl.texture.region.TextureRegion

/**
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
class PlayView(
    engine: Engine, camera: Camera, textures: Textures, font: IFont?,
    playCallback: PlayCallback, scoreStorage: IScoreStorage
) : GameView(engine, camera) {
    private class GameEventCallbackImpl(private val levels: Levels, gameSession: GameSession) :
        GameEventCallback {
        private val gameSession: GameSession
        override fun onLinesRemoved(linesRemoved: Int) {
            val totalScore: Int = gameSession.addScore(linesRemoved)
            levels.setLevelByScore(totalScore)
        }

        override fun reset() {
            levels.reset()
        }

        override fun framesPerTick(): Long {
            return levels.framesPerTick()
        }

        init {
            this.gameSession = gameSession
        }
    }

    private val field: HexagonalField
    private val background: Background

    //private final Textures textures;
    private val size: Float
    private val touchListener: TouchListener
    private val gameSession: GameSession
    private val pauseSprite: Sprite
    private val levels = Levels()
    fun startNewGame() {
        field.restart(EmptyFieldGenerator())
        gameSession.resetResults()
        levels.reset()
    }

    override fun onTouch(pSceneTouchEvent: TouchEvent) {
        touchListener.onTouchEvent(pSceneTouchEvent)
    }

    override fun populate() {
        scene.background = background
        scene.attachChild(field)
        scene.attachChild(gameSession)
        scene.attachChild(pauseSprite)
    }

    override fun update() {
        field.update()
    }

    override fun onBackPressed() {
        field.pause()
    }

    companion object {
        private val SQ3 = Math.sqrt(3.0).toFloat()
        private const val DEPTH = 20
        private const val WIDTH = 7
    }

    init {
        size = camera.height / ((DEPTH + 1) * SQ3) // pixels per hexagon
        val leftShift = size + (WIDTH + 2) * size * 3 / 2
        gameSession =
            GameSession(levels, leftShift, 10f, font, engine.getVertexBufferObjectManager())
        val gameEventCallback = GameEventCallbackImpl(levels, gameSession)
        field = HexagonalField.generateJar(
            WIDTH, DEPTH, SpriteContext(size, camera, textures, engine),
            gameEventCallback,
            EmptyFieldGenerator()
        )
        val pauseTexture: TextureRegion = textures.getPause()
        pauseSprite = Sprite(
            (camera.width - pauseTexture.width) / 2,
            (camera.height - pauseTexture.height) / 2,
            pauseTexture,
            engine.vertexBufferObjectManager
        )
        pauseSprite.isVisible = false
        touchListener = TouchListener(field, (camera.width / (WIDTH + 2)).toInt())
        background = SpriteUtils.BACKGROUND
        field.addGameFinishedListener(object : GameFinishedListener {
            override fun onGameFinished() {
                val gameResults = GameResults(gameSession.getScore())
                playCallback.toMenuView(gameResults)
                scoreStorage.save(gameResults)
                pauseSprite.isVisible = false
            }
        })
        field.addPausedFinishedListener(object : GamePausedListener {
            override fun onGamePaused() {
                pauseSprite.isVisible = true
            }

            override fun onGameResumed() {
                pauseSprite.isVisible = false
            }
        })
    }
}