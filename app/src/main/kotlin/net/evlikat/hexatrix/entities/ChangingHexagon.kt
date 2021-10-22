package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialPosition
import org.andengine.opengl.texture.region.ITextureRegion

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
class ChangingHexagon(
    position: AxialPosition,
    hexagon0: ITextureRegion,
    private val hexagon1: ITextureRegion,
    spriteContext: SpriteContext
) : Hexagon(position, hexagon0, spriteContext) {

    override fun getTextureRegion(): ITextureRegion {
        return if (position.line % 2 == 0) {
            super.getTextureRegion()
        } else hexagon1
    }
}