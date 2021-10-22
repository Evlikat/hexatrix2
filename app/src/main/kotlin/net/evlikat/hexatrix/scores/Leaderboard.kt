package net.evlikat.hexatrix.scores

import android.util.Log
import net.evlikat.hexatrix.utils.IOUtils
import net.evlikat.hexatrix.views.GameResults
import java.io.*
import java.util.*
import kotlin.math.sign

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 17, 2014)
 */
class Leaderboard(private val filename: String, private val topSize: Int) : IScoreStorage {

    override var topScores: MutableList<Score> = ArrayList()

    override fun save(newResult: GameResults) {
        if (topScores.isNotEmpty() && topScores[topScores.size - 1].amount > newResult.score) {
            // there is no need to save not top result
            return
        }
        topScores.add(Score(Date(), newResult.score))
        topScores.sortWith { t1, t2 ->
            sign((t2.amount - t1.amount).toFloat()).toInt()
        }
        topScores = ArrayList(topScores.subList(0, Math.min(topScores.size, topSize)))
        saveToFile(filename)
    }

    private fun saveToFile(filename: String) {
        var out: ObjectOutputStream? = null
        try {
            val file = File(filename)
            if (!file.exists()) {
                File(file.parent).mkdirs()
            }
            out = ObjectOutputStream(FileOutputStream(file))
            out.writeObject(topScores)
        } catch (ex: FileNotFoundException) {
            Log.e(TAG, "File not found: $filename", ex)
        } catch (ex: IOException) {
            Log.e(TAG, "Can not save the leaderboard to file $filename", ex)
        } finally {
            IOUtils.closeQuietly(out)
        }
    }

    private fun load(filename: String): MutableList<Score> {
        var ois: ObjectInputStream? = null
        try {
            ois = ObjectInputStream(FileInputStream(File(filename)))
            return ois.readObject() as MutableList<Score>
        } catch (ex: ClassNotFoundException) {
            Log.e(TAG, "File not found: $filename", ex)
        } catch (ex: FileNotFoundException) {
            Log.e(TAG, "File not found: $filename", ex)
        } catch (ex: IOException) {
            Log.e(TAG, "Can not load the leaderboard from file $filename", ex)
        } finally {
            IOUtils.closeQuietly(ois)
        }
        return ArrayList()
    }

    companion object {
        private val TAG = Leaderboard::class.java.simpleName
    }

    init {
        topScores = load(filename)
    }
}