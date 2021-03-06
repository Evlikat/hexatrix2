/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.evlikat.hexatrix.entities

import net.evlikat.hexatrix.axial.MoveDirection
import net.evlikat.hexatrix.axial.RotateDirection

/**
 *
 * @author Roman Prokhorov
 * @version 1.0 (Jul 04, 2014)
 */
interface IHexagonalField {
    fun turn(direction: RotateDirection): Boolean
    fun move(direction: MoveDirection, steps: Int): Boolean
    fun moving(direction: MoveDirection?, steps: Int): Boolean
    fun pause()
    fun drop()
}