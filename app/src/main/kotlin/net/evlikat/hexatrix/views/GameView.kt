package net.evlikat.hexatrix.views

import org.andengine.engine.Engine
import org.andengine.engine.camera.Camera
import org.andengine.entity.scene.Scene
import org.andengine.input.touch.TouchEvent

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
abstract class GameView(engine: Engine, camera: Camera) {
    val scene: Scene
    protected val engine: Engine
    protected val camera: Camera
    protected abstract fun onTouch(pSceneTouchEvent: TouchEvent)

    abstract fun populate()
    abstract fun update()
    abstract fun onBackPressed()

    init {
        this.engine = engine
        this.camera = camera
        scene = object : Scene() {
            override fun onSceneTouchEvent(pSceneTouchEvent: TouchEvent): Boolean {
                onTouch(pSceneTouchEvent)
                return super.onSceneTouchEvent(pSceneTouchEvent)
            }
        }
    }
}