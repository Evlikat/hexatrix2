package net.evlikat.hexatrix.utils

import net.evlikat.hexatrix.utils.SpriteUtils
import java.io.Closeable
import java.io.IOException

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
object IOUtils {
    fun closeQuietly(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (ex: IOException) {
            // That's OK
        }
    }
}