package net.evlikat.hexatrix.axial

import java.util.*

/**
 * @author Roman Prokhorov
 * @version 1.0 (Jun 27, 2014)
 */
class RandomFigureGenerator : FigureGenerator {
    private val rnd = Random()
    override var next = FIGURES[rnd.nextInt(FIGURES.size)]
        private set

    override fun generate(): AxialFigure {
        val current = next
        next = FIGURES[rnd.nextInt(FIGURES.size)]
        return current
    }

    override fun reset() {
        next = FIGURES[rnd.nextInt(FIGURES.size)]
    }

    companion object {
        val STICK = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(0, -1),
                AxialPosition(0, -2)
            )
        )
        private val LEFT_LIGHTNING = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(-1, 0),
                AxialPosition(-1, -1)
            )
        )
        private val RIGHT_LIGHTNING = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(1, -1),
                AxialPosition(1, -2)
            )
        )
        private val LEFT_LEG = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(0, -1),
                AxialPosition(-1, -1)
            )
        )
        private val RIGHT_LEG = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(0, -1),
                AxialPosition(1, -2)
            )
        )
        private val LEFT_BOLT = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(0, -1),
                AxialPosition(-1, 0)
            )
        )
        private val RIGHT_BOLT = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(0, -1),
                AxialPosition(1, -1)
            )
        )
        private val SQUARE = AxialFigure(
            listOf(
                AxialPosition(1, -1),
                AxialPosition(-1, 0),
                AxialPosition(0, -1)
            )
        )
        private val STAR = AxialFigure(
            listOf(
                AxialPosition(0, 1),
                AxialPosition(-1, 0),
                AxialPosition(1, -1)
            )
        )
        private val RAINBOW = AxialFigure(
            listOf(
                AxialPosition(1, 0),
                AxialPosition(-1, 1),
                AxialPosition(1, 1)
            )
        )
        private val FIGURES = listOf(
            LEFT_LIGHTNING,
            RIGHT_LIGHTNING,
            STICK,
            STICK,
            LEFT_LEG,
            RIGHT_LEG,
            LEFT_BOLT,
            RIGHT_BOLT,
            SQUARE,
            SQUARE,
            STAR,
            RAINBOW
        )
    }
}