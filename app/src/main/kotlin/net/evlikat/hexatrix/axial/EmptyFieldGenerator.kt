package net.evlikat.hexatrix.axial

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
class EmptyFieldGenerator : InitialFieldGenerator {
    override fun generate(): Collection<AxialPosition> {
        return emptyList()
    }

    companion object {
        var INITIAL_FIELDS: Collection<AxialPosition> = listOf(
            AxialPosition(0, 0),
            AxialPosition(1, 0),
            AxialPosition(2, -1),
            AxialPosition(3, -1),  //new AxialPosition(4, -2),
            AxialPosition(5, -2),
            AxialPosition(6, -3),
            AxialPosition(7, -3),
            AxialPosition(8, -4),
            AxialPosition(9, -4),
            AxialPosition(10, -5),
            AxialPosition(0, 2),
            AxialPosition(1, 2),
            AxialPosition(2, 1),
            AxialPosition(3, 1),  //new AxialPosition(4, -1),
            AxialPosition(5, 0),
            AxialPosition(6, -1),
            AxialPosition(7, -1),
            AxialPosition(8, -2),
            AxialPosition(9, -2),
            AxialPosition(10, -3)
        )
    }
}