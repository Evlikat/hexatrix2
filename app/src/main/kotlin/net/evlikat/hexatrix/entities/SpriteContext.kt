package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.Textures
import org.andengine.engine.Engine
import org.andengine.engine.camera.Camera
import org.andengine.opengl.vbo.VertexBufferObjectManager

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
class SpriteContext(val size: Float, val camera: Camera, val textures: Textures, val engine: Engine) {
    val vertexBufferObjectManager: VertexBufferObjectManager = engine.vertexBufferObjectManager
}