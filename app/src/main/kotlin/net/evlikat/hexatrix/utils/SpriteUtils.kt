package net.evlikat.hexatrix.utils

import org.andengine.entity.scene.background.Background

object SpriteUtils {
    @JvmField
    val BACKGROUND: Background = createBackground(255, 255, 255)

    //    public static final Background BACKGROUND = createBackground(253, 234, 202);
    fun createBackground(r: Int, g: Int, b: Int): Background {
        return Background(r.toFloat() / 255, g.toFloat() / 255, b.toFloat() / 255)
    }
}