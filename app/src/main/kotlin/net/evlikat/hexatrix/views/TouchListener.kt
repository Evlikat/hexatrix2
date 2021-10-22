package net.evlikat.hexatrix.views

import android.util.Log
import net.evlikat.hexatrix.axial.MoveDirection
import net.evlikat.hexatrix.axial.RotateDirection
import net.evlikat.hexatrix.entities.IHexagonalField
import org.andengine.input.touch.TouchEvent
import java.lang.Exception
import kotlin.math.abs

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
class TouchListener(
    private val field: IHexagonalField,
    private val pixelsPerStep: Int
) : ITouchListener {

    private var downX = -1f
    private var downY = -1f

    /**
     * Define how to handle event here
     *
     * @param pSceneTouchEvent
     */
    fun onTouchEvent(pSceneTouchEvent: TouchEvent) {
        if (pSceneTouchEvent.isActionDown) {
            downX = pSceneTouchEvent.x
            downY = pSceneTouchEvent.y
        } else if (pSceneTouchEvent.isActionMove) {
            onMove(downX, downY, pSceneTouchEvent.x, pSceneTouchEvent.y)
        } else if (pSceneTouchEvent.isActionUp) {
            if (downX > 0 && downY > 0) {
                onFling(downX, downY, pSceneTouchEvent.x, pSceneTouchEvent.y)
                downX = -1f
                downY = -1f
            }
        }
    }

    override fun onMove(x1: Float, y1: Float, x2: Float, y2: Float): Boolean {
        val diffY = y2 - y1
        val diffX = x2 - x1
        if (diffX * diffX + diffY * diffY > CLICK_RADIUS * CLICK_RADIUS) {
            if (abs(diffX) > abs(diffY)) {
                val steps = calcSteps(diffX)
                if (diffX > 0) {
                    field.moving(MoveDirection.RIGHT, steps)
                } else {
                    field.moving(MoveDirection.LEFT, steps)
                }
            }
        }
        return false
    }

    override fun onFling(x1: Float, y1: Float, x2: Float, y2: Float): Boolean {
        val result = false
        try {
            val diffY = y2 - y1
            val diffX = x2 - x1
            // if fling is out of single click circle
            if (diffX * diffX + diffY * diffY > CLICK_RADIUS * CLICK_RADIUS) {
                // protects from both: dropping and moving
                if (abs(diffX) > pixelsPerStep.toFloat().coerceAtMost(abs(diffY))) {
                    val steps = calcSteps(diffX)
                    if (diffX > 0) {
                        field.move(MoveDirection.RIGHT, steps)
                    } else {
                        field.move(MoveDirection.LEFT, steps)
                    }
                } else {
                    if (diffY < 0) {
                        if (diffX > 0) {
                            field.turn(RotateDirection.CLOCKWISE)
                        } else {
                            field.turn(RotateDirection.COUNTERCLOCKWISE)
                        }
                    } else {
                        field.drop()
                    }
                }
            } else {
                // handle like single click
                field.turn(RotateDirection.CLOCKWISE)
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.message, ex)
        }
        return result
    }

    private fun calcSteps(diffX: Float): Int {
        var steps = abs(diffX).toInt() / pixelsPerStep
        if (steps == 0) {
            steps = 1 // handle short slides
        }
        return steps
    }

    companion object {
        private const val CLICK_RADIUS = 30
        private val TAG = TouchListener::class.java.simpleName
    }

}