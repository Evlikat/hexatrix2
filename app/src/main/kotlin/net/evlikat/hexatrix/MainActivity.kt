package net.evlikat.hexatrix

import android.view.KeyEvent
import net.evlikat.hexatrix.scores.Leaderboard
import org.andengine.engine.camera.Camera
import org.andengine.engine.camera.ZoomCamera
import org.andengine.engine.handler.timer.TimerHandler
import org.andengine.engine.options.EngineOptions
import org.andengine.engine.options.ScreenOrientation
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy
import org.andengine.entity.scene.Scene
import org.andengine.entity.util.FPSLogger
import org.andengine.ui.IGameInterface
import org.andengine.ui.activity.BaseGameActivity

class MainActivity : BaseGameActivity() {

    private lateinit var camera: Camera
    private lateinit var textures: Textures
    private lateinit var sceneManager: SceneManager

    override fun onCreateEngineOptions(): EngineOptions {
        camera = ZoomCamera(0f, 0f, CAMERA_WIDTH, CAMERA_HEIGHT)
        return EngineOptions(
            true,
            ScreenOrientation.PORTRAIT_FIXED,
            FillResolutionPolicy(),
            camera
        )
    }

    override fun onCreateResources(ocrc: IGameInterface.OnCreateResourcesCallback) {
        textures = Textures(fontManager, textureManager, assets)
        sceneManager = SceneManager(
            this, mEngine, camera, textures, Leaderboard(
                "/data/data/" + MainActivity::class.java.getPackage().name + "/leaderboard.lb", 20
            )
        )
        ocrc.onCreateResourcesFinished()
    }

    override fun onCreateScene(ocsc: IGameInterface.OnCreateSceneCallback) {
        this.mEngine.registerUpdateHandler(FPSLogger())
        val scene: Scene = sceneManager.currentView.scene
        this.mEngine.registerUpdateHandler(
            TimerHandler(
                0.05f,
                true
            ) { sceneManager.updateCurrentView() })
        ocsc.onCreateSceneFinished(scene)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sceneManager.onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPopulateScene(scene: Scene?, opsc: IGameInterface.OnPopulateSceneCallback) {
        opsc.onPopulateSceneFinished()
    }

    companion object {
        private const val CAMERA_WIDTH = 1080f
        private const val CAMERA_HEIGHT = 1920f
    }
}