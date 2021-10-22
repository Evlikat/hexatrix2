package net.evlikat.hexatrix.axial

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 01, 2014)
 */
interface InitialFieldGenerator {
    fun generate(): Collection<AxialPosition>
}