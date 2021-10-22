package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.AxialPosition
import net.evlikat.hexatrix.utils.SQ3
import org.andengine.opengl.texture.region.ITextureRegion
import java.io.Serializable

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 07, 2014)
 */
open class Hexagon(
    var position: AxialPosition,
    hexagon0: ITextureRegion,
    spriteContext: SpriteContext
) : AxialEntity(
    position.getX(spriteContext.size),
    position.getY(spriteContext.size),
    spriteContext.size * 2, spriteContext.size * SQ3,
    hexagon0, spriteContext
), Serializable