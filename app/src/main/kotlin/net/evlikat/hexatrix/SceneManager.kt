package net.evlikat.hexatrix

import android.os.Process
import net.evlikat.hexatrix.scores.IScoreStorage
import net.evlikat.hexatrix.views.*
import org.andengine.engine.Engine
import org.andengine.engine.camera.Camera

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
class SceneManager(
    private val activity: MainActivity,
    private val engine: Engine,
    private val camera: Camera,
    private val textures: Textures,
    private val scoreStorage: IScoreStorage
) : PlayCallback, MenuCallback, LeadersCallback {

    private val font = textures.font

    // Views
    private var menuView = MainMenuView(engine, camera, textures, font, this, scoreStorage).also { it.populate() }
    private var playView = lazy {
        PlayView(engine, camera, textures, font, this, scoreStorage).also { it.populate() }
    }
    private var leadersView = lazy {
        LeadersView(engine, camera, font, this, scoreStorage).also { it.populate() }
    }

    var currentView: GameView = menuView
        private set(value) {
            engine.scene = currentView.scene
            field = value
        }

    fun updateCurrentView() {
        currentView.update()
    }

    override fun quit() {
        activity.finish()
        Process.killProcess(Process.myPid())
    }

    // --- Callbacks ---
    override fun toMenuView(gameResults: GameResults) {
        menuView.registerResult(gameResults)
        currentView = menuView
    }

    override fun toPlayView() {
        playView.value.startNewGame()
        currentView = playView.value
    }

    override fun toLeadersView() {
        leadersView.value.updateResults()
        currentView = leadersView.value
    }

    override fun toMenuView() {
        currentView = menuView
    }

    fun onBackPressed() {
        currentView.onBackPressed()
    }
}