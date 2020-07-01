package model

/**
 * Created by HPPC on 12/6/2018.
 */
interface GraphInterface {
    fun setArcVal(arc:Pair<Int,Int>,arcVal :Int)
    fun getArcVal(arc:Pair<Int,Int>):Int
}