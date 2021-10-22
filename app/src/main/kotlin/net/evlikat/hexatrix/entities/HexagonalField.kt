package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.*
import net.evlikat.hexatrix.utils.SQ3
import org.andengine.engine.Engine
import org.andengine.entity.Entity
import org.andengine.entity.IEntity
import org.andengine.opengl.texture.region.TextureRegion
import java.io.Serializable
import java.util.ArrayList

/**
 * @author Roman Prokhorov
 * @version 1.0 (Jul 03, 2014)
 */
class HexagonalField private constructor(
    private val width: Int,
    private val depth: Int,
    private val spriteContext: SpriteContext,
    private val gameEventCallback: GameEventCallback
) : Entity(), IHexagonalField, Serializable {
    private val borders = Fields()
    private val backWall = Fields()
    private val fields = Fields()

    // Properties
    var floatFigure: Figure? = null
        private set
    private var droppedShadowFigure: Figure? = null
    private var movedShadowFigure: Figure? = null
    private var nextFigure: Figure? = null

    //
    private var moveDirection: MoveDirection? = null
    private var moveShift = 0

    //
    private val gravity = AxialDirection.Back
    private val originPosition = AxialPosition(q = width / 2, r = depth - 3 - (width / 2) / 2)
    private val nextFigurePosition = AxialPosition(q = width + 2, r = depth / 2 - width / 4)
    private val figureGenerator = RandomFigureGenerator()

    //
    private var currentState: GameState = FallingBlocksState(this, this.gameEventCallback)

    //
    private val gameFinishedListeners: MutableList<GameFinishedListener> = ArrayList()
    private val gamePausedListeners: MutableList<GamePausedListener> = ArrayList()

    private fun createNewFloatFigure(): Boolean {
        val newFigure: AxialFigure = figureGenerator.generate()
        newFigure.position = AxialPosition(originPosition)
        return if (floatFigure == null) {
            floatFigure = Figure(
                prototype = newFigure,
                pTextureRegion = spriteContext.textures.getFigure(),
                spriteContext = spriteContext
            )
            this.attachChild(floatFigure)
            onFloatFigureNewPosition()

            nextFigure = Figure(
                prototype = figureGenerator.next,
                pTextureRegion = spriteContext.textures.getFigure(),
                spriteContext = spriteContext
            )
            nextFigure!!.position = nextFigurePosition

            this.attachChild(nextFigure)
            true
        } else {
            detachSafely(floatFigure)
            val figureSet = setFloatFigure(newFigure)
            if (figureSet) {
                this.attachChild(floatFigure)
                onFloatFigureNewPosition()
            }
            figureSet
        }
    }

    fun addGameFinishedListener(listener: GameFinishedListener) {
        gameFinishedListeners.add(listener)
    }

    fun addPausedFinishedListener(listener: GamePausedListener) {
        gamePausedListeners.add(listener)
    }

    fun onGameFinished() {
        for (listener in gameFinishedListeners) {
            listener.onGameFinished()
        }
    }

    fun onGamePaused() {
        for (gamePausedListener in gamePausedListeners) {
            gamePausedListener.onGamePaused()
        }
    }

    fun onGameResumed() {
        for (gamePausedListener in gamePausedListeners) {
            gamePausedListener.onGameResumed()
        }
    }

    fun getFields(): Collection<AxialPosition?> {
        return fields.positions
    }

    fun setFloatFigure(floatFigure: IFigure): Boolean {
        for (axialPosition in floatFigure.partsPositions) {
            if (fields.contains(axialPosition)) {
                return false
            }
        }
        this.floatFigure = Figure(floatFigure, spriteContext.textures.getFigure(), spriteContext)
        onFloatFigureNewPosition()
        return true
    }

    private fun addBorder(border: Hexagon) {
        borders.put(border.position, border)
        this.attachChild(border)
    }

    private fun addBackWallBrick(brick: Hexagon) {
        backWall.put(brick.position, brick)
        this.attachChild(brick)
    }

    private fun addField(position: AxialPosition, field: ChangingHexagon) {
        fields.put(position, field)
        this.attachChild(field)
    }

    fun removeField(position: AxialPosition) {
        val hex = fields[position]
        if (hex != null) {
            detachSafely(hex)
            fields.remove(position)
        }
    }

    fun getHexagons(lineNumber: Int?): List<Hexagon> {
        val result: MutableList<Hexagon> = ArrayList()
        for (axQ in 0 until width) {
            val axR = lineNumber!! - axQ / 2
            val hexagon = fields[AxialPosition(axQ, axR)]
            if (hexagon != null) {
                result.add(hexagon)
            }
        }
        return result
    }

    fun fall(): Boolean {
        if (floatFigure!!.move(forbiddenFields, gravity)) {
            onFloatFigureNewPosition()
            return true
        }
        harden()
        return false
    }

    fun calcLinesToRemove(): List<Int> {
        val lineNumbersToRemove: MutableList<Int> = ArrayList()
        for (x in 0 until depth) {
            if (lineCompleted(x)) {
                lineNumbersToRemove.add(x)
            }
        }
        return lineNumbersToRemove
    }

    val forbiddenFields: Collection<AxialPosition>
        get() {
            return borders.positions + fields.positions
        }

    fun update() {
        currentState = currentState.next()
    }

    fun restart(initialFieldGenerator: InitialFieldGenerator) {
        for (hexagon in fields.hexagons) {
            detachChild(hexagon)
        }
        fields.clear()
        gameEventCallback.reset()
        figureGenerator.reset()
        // re-init with new fields
        for (position in initialFieldGenerator.generate()) {
            addField(
                position, ChangingHexagon(
                    position,
                    spriteContext.textures.getHexagon0(),
                    spriteContext.textures.getHexagon1(),
                    spriteContext
                )
            )
        }
        createNewFloatFigure()
        nextFigure!!.resetParts(figureGenerator.next.parts)
        currentState = FallingBlocksState(this, gameEventCallback)
    }

    override fun pause() {
        currentState = PauseState.create(currentState, this, gameEventCallback)
    }

    override fun turn(direction: RotateDirection): Boolean {
        return currentState.turn(direction)
    }

    override fun move(direction: MoveDirection, steps: Int): Boolean {
        moveShift = 0
        return currentState.move(direction, steps)
    }

    override fun moving(direction: MoveDirection?, steps: Int): Boolean {
        moveDirection = direction
        moveShift = steps
        val moving = currentState.moving(direction, steps)
        onFloatFigureNewPosition()
        return moving
    }

    fun turnFigure(direction: RotateDirection): Boolean {
        if (floatFigure != null) {
            if (floatFigure!!.turn(forbiddenFields, direction)) {
                onFloatFigureNewPosition()
            }
            return true
        }
        return false
    }

    fun moveFigure(axialDirection: AxialDirection, steps: Int): Boolean {
        if (floatFigure != null) {
            var movedAtAll = false
            for (i in 0 until steps) {
                val moved = floatFigure!!.move(forbiddenFields, axialDirection)
                if (!moved) {
                    break
                }
                movedAtAll = true
            }
            onFloatFigureNewPosition()
            return movedAtAll
        }
        return false
    }

    fun harden() {
        if (floatFigure == null) {
            return
        }
        // Harden float figure
        for (axialPosition in floatFigure!!.partsPositions) {
            addField(
                AxialPosition(axialPosition),
                ChangingHexagon(
                    axialPosition,
                    spriteContext.textures.getHexagon0(),
                    spriteContext.textures.getHexagon1(),
                    spriteContext
                )
            )
        }
        detachSafely(floatFigure)
        onFloatFigureNewPosition()
    }

    /**
     * @param linesRemoved
     * @return true if new float figure was set properly, false if there was no room to set new figure.
     */
    fun onFigureDropped(linesRemoved: Int): Boolean {
        if (linesRemoved > 0) {
            gameEventCallback.onLinesRemoved(linesRemoved)
        }
        val figureSet = createNewFloatFigure()
        if (!figureSet) {
            // game over
            return false
        }
        nextFigure!!.resetParts(figureGenerator.next.parts)
        return true
    }

    private fun lineCompleted(x: Int): Boolean {
        for (q in 0 until width) {
            if (!getFields().contains(AxialPosition(q, x - q / 2))) {
                return false
            }
        }
        return true
    }

    fun dropAll(steps: Int, demarcationPoints: Map<Int, Int>) {
        if (fields.isEmpty) {
            return
        }
        var fallingPart: MutableList<AxialPosition> = ArrayList<AxialPosition>()
        for (pos in fields.positions) {
            if (pos.r > demarcationPoints.getValue(pos.q)) {
                fallingPart.add(pos)
            }
        }
        if (fallingPart.isEmpty()) {
            return
        }
        for (i in 0 until steps) {
            fallingPart = fields.moveBatch(fallingPart, gravity)
        }
    }

    private fun detachSafely(entity: IEntity?) {
        val lock: Engine.EngineLock = spriteContext.engine.engineLock
        lock.lock()
        try {
            this.detachChild(entity)
        } finally {
            lock.unlock()
        }
    }

    override fun drop() {
        var moved = false
        while (floatFigure!!.move(forbiddenFields, gravity)) {
            moved = true
        }
        // Avoid multi-drops abused
        if (moved) {
            onFloatFigureNewPosition()
            currentState.cancel()
        }
    }

    private fun onFloatFigureNewPosition() {
        highlightMovedShadowHexagons()
        highlightShadowHexagons(if (movedShadowFigure == null) floatFigure!! else movedShadowFigure!!)
    }

    private fun highlightShadowHexagons(source: Figure) {
        if (droppedShadowFigure != null) {
            detachSafely(droppedShadowFigure)
        }
        if (source.parent == null) {
            droppedShadowFigure = null
            return
        }
        droppedShadowFigure = Figure(
            source,
            spriteContext.textures.getShadow(),
            spriteContext
        )
        while (droppedShadowFigure!!.move(forbiddenFields, gravity)) {
        }
        this.attachChild(droppedShadowFigure)
    }

    private fun highlightMovedShadowHexagons() {
        if (movedShadowFigure != null) {
            detachSafely(movedShadowFigure)
        }
        if (floatFigure!!.parent == null) {
            movedShadowFigure = null
            return
        }
        movedShadowFigure = Figure(
            floatFigure!!,
            spriteContext.textures.getShadow(),
            spriteContext
        )
        for (i in 0 until moveShift) {
            val moved: Boolean = movedShadowFigure!!.move(
                forbiddenFields,
                if (moveDirection === MoveDirection.LEFT) AxialDirection.Left else AxialDirection.RightBack
            )
            if (!moved) {
                break
            }
        }
        this.attachChild(movedShadowFigure)
    }

    companion object {
        fun generateJar(
            width: Int, depth: Int,
            spriteContext: SpriteContext,
            gameEventCallback: GameEventCallback,
            initialFieldGenerator: InitialFieldGenerator
        ): HexagonalField {
            val jar = HexagonalField(width, depth, spriteContext, gameEventCallback)
            val textures = spriteContext.textures
            // Set borders: right hexa-corner
            var pos = Hexagon(
                AxialPosition(0, 0).plus(AxialDirection.Left),
                textures.getBorderLeft(),
                spriteContext
            )
            val directions: Array<AxialDirection> =
                arrayOf<AxialDirection>(AxialDirection.Right, AxialDirection.RightBack)
            // borders
            for (i in -1 until width + 1) {
                // root cell
                jar.addBorder(pos)
                // stack cells for borders
                if (i == -1 || i == width) {
                    var stackPos = pos
                    val border: TextureRegion = if (i == -1) {
                        textures.getBorderLeft()
                    } else {
                        textures.getBorderRight()
                    }
                    for (j in 1 until depth) {
                        stackPos = Hexagon(
                            stackPos.position.plus(AxialDirection.Forward),
                            border,
                            spriteContext
                        )
                        jar.addBorder(stackPos)
                    }
                }
                if (i == width) {
                    break
                }
                // right bottom border should be cut
                val borderBottom: TextureRegion =
                    if (i < width - 1) textures.getBorderBottom() else textures.getBorderRight()
                // next stack
                pos = Hexagon(
                    pos.position.plus(directions[(i + 2) % directions.size]),
                    borderBottom,
                    spriteContext
                )
            }
            // back wall
            for (i in 0 until width) {
                for (h in 0 until depth - 1) {
                    val brick =
                        Hexagon(AxialPosition(i, h - i / 2), textures.getBrick(), spriteContext)
                    jar.addBackWallBrick(brick)
                }
            }
            // setting initial fields
            for (axialPosition in initialFieldGenerator.generate()) {
                jar.addField(
                    axialPosition, ChangingHexagon(
                        axialPosition,
                        spriteContext.textures.getHexagon0(),
                        spriteContext.textures.getHexagon1(),
                        spriteContext
                    )
                )
            }
            // setting start position
            jar.setPosition(
                spriteContext.size * 3 / 2,
                SQ3 * spriteContext.size * depth - SQ3 * spriteContext.size / 2
            )
            return jar
        }
    }
}