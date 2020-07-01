package model

/**
 * Created by HPPC on 12/6/2018.
 */
class CompleteGraph(var order : Int): GraphInterface {
    private var initMat: (Int)->Array<Int> = { _->Array(order,{ _->0})}
    private var evaluation : Array<Array<Int>> = Array(order,initMat)

    override fun setArcVal(arc: Pair<Int, Int>,arcVal: Int) {
        evaluation[arc.first-1][arc.second-1] = arcVal
        evaluation[arc.second-1][arc.first-1]  = arcVal
    }

    override fun getArcVal(arc: Pair<Int, Int>) :Int{
        return evaluation[arc.first-1][arc.second-1]
    }
    fun exact(order:Int,progress:ArrayList<Int>):ArrayList<Int>{
        if(progress.size==order) return progress
        else{
            var min = Int.MAX_VALUE
            var next = -1
            var minpath  = progress
            for(k in 1..order){
                if((progress.indexOf(k)==-1)) {
                    progress.add(k)
                    var a = progress.toList()
                    progress.remove(k)
                    //println(progress)
                    var mp = exact(order, ArrayList(a))
                    var temp = costOf(mp)
                    if (temp < min) {
                        min = temp
                        minpath = mp
                    }
                }
            }
            return minpath
        }
    }
    private fun costOf(list: ArrayList<Int>):Int{
        var fst = list.first()
        var cost = 0
        for(i in 1..(list.size-1)){
            cost += getArcVal(Pair(fst,list[i]))
            fst = list[i]
        }
        cost += getArcVal(Pair(list.last(),list.first()))
        return cost
    }

    fun printMat(){
        for(i in 0..(order-1)){
            for(j in 0..(order-1)){
                println(i.toString() +" "+j.toString()+":"+evaluation[i][j])
            }
    }
}
}