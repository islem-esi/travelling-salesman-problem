package model

import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created by HPPC on 12/9/2018.
 */
class GraphNode(rank : Int, order:Int,r:Double) : javafx.scene.shape.Circle() {
    var rk : Int
    init {
        radius = 35.0
        centerX = calcCenter(rank,order,r).first
        centerY = calcCenter(rank,order,r).second
        fill = Color.BLACK
        rk = rank
    }

    private fun calcCenter(rank: Int,order: Int,r: Double):Pair<Double,Double>{
        return Pair(r* cos(2*3.14/order*rank),r* sin(2*3.14/order*rank))
    }
}