package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialPosition
import net.evlikat.hexatrix.utils.SQ3
import org.andengine.entity.sprite.Sprite
import org.andengine.opengl.texture.region.ITextureRegion

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
abstract class AxialEntity(
    pX: Float,
    pY: Float,
    width: Float,
    height: Float,
    pTextureRegion: ITextureRegion?,
    protected val spriteContext: SpriteContext
) : Sprite(pX, pY, width, height, pTextureRegion, spriteContext.vertexBufferObjectManager) {

    private fun onMoved(q: Float, r: Float) {
        val size = spriteContext.size
        val x = size * 3 / 2 * q
        val y = -size * SQ3 * (r + q / 2)
        setPosition(x, y)
    }

    fun onMoved(newPosition: AxialPosition) {
        onMoved(newPosition.q.toFloat(), newPosition.r.toFloat())
    }
}