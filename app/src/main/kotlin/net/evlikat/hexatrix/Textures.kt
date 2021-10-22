package net.evlikat.hexatrix

import android.content.res.AssetManager
import android.graphics.Color
import org.andengine.opengl.font.Font
import org.andengine.opengl.font.FontFactory
import org.andengine.opengl.font.FontManager
import org.andengine.opengl.font.IFont
import org.andengine.opengl.texture.TextureManager
import org.andengine.opengl.texture.TextureOptions
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory
import org.andengine.opengl.texture.region.TextureRegion

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 02, 2014)
 */
class Textures(
    fontManager: FontManager,
    textureManager: TextureManager,
    assetManager: AssetManager
) {
    private var hexagon0: TextureRegion? = null
    private var hexagon1: TextureRegion? = null
    private var borderBottom: TextureRegion? = null
    private var brick: TextureRegion? = null
    private var borderLeft: TextureRegion? = null
    private var borderRight: TextureRegion? = null
    private var figure: TextureRegion? = null
    private var shadow: TextureRegion? = null
    private var pause: TextureRegion? = null
    private var startBtn: TextureRegion? = null
    private var leadersBtn: TextureRegion? = null
    private var quitBtn: TextureRegion? = null
    var font: Font? = null
    private fun loadTextures(
        fontManager: FontManager,
        textureManager: TextureManager,
        assetManager: AssetManager
    ) {
        borderBottom = loadTexture(textureManager, assetManager, "border", 180, 157)
        borderLeft = loadTexture(textureManager, assetManager, "border-left", 180, 157)
        borderRight = loadTexture(textureManager, assetManager, "border-right", 180, 157)
        hexagon0 = loadTexture(textureManager, assetManager, "hexagon0", 180, 157)
        hexagon1 = loadTexture(textureManager, assetManager, "hexagon1", 180, 157)
        brick = loadTexture(textureManager, assetManager, "brick", 180, 157)
        figure = loadTexture(textureManager, assetManager, "figure", 180, 157)
        shadow = loadTexture(textureManager, assetManager, "shadow", 180, 157)
        pause = loadTexture(textureManager, assetManager, "pause", 420, 364)
        startBtn = loadTexture(textureManager, assetManager, "start-btn", 420, 364)
        leadersBtn = loadTexture(textureManager, assetManager, "leaders-btn", 420, 364)
        quitBtn = loadTexture(textureManager, assetManager, "quit-btn", 420, 364)
        FontFactory.setAssetBasePath("font/")
        val fontTexture = BitmapTextureAtlas(textureManager, 1024, 1024, TextureOptions.BILINEAR)
        font = FontFactory.createFromAsset(
            fontManager, fontTexture,
            assetManager, "consola.ttf", 144f, true, Color.BLACK
        )
        font!!.load()
    }

    private fun loadTexture(
        textureManager: TextureManager,
        assetManager: AssetManager,
        id: String, width: Int, height: Int
    ): TextureRegion {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/")
        val texture = BitmapTextureAtlas(
            textureManager,
            width,
            height,
            TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA
        )
        val textureRegion: TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
            texture,
            assetManager,
            "$id.png",
            0,
            0
        )
        texture.load()
        return textureRegion
    }

    fun getBrick(): TextureRegion {
        return brick!!
    }

    fun getHexagon0(): TextureRegion {
        return hexagon0!!
    }

    fun getHexagon1(): TextureRegion {
        return hexagon1!!
    }

    fun getBorderLeft(): TextureRegion {
        return borderLeft!!
    }

    fun getBorderRight(): TextureRegion {
        return borderRight!!
    }

    fun getBorderBottom(): TextureRegion {
        return borderBottom!!
    }

    fun getFigure(): TextureRegion {
        return figure!!
    }

    fun getPause(): TextureRegion {
        return pause!!
    }

    fun getStartBtn(): TextureRegion {
        return startBtn!!
    }

    fun getLeadersBtn(): TextureRegion {
        return leadersBtn!!
    }

    fun getQuitBtn(): TextureRegion {
        return quitBtn!!
    }

    fun getShadow(): TextureRegion {
        return shadow!!
    }

    init {
        loadTextures(fontManager, textureManager, assetManager)
    }
}