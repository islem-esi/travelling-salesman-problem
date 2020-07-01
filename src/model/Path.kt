package model

/**
 * Created by HPPC on 12/6/2018.
 */
class Path(private val graph : CompleteGraph, first: Int) {
    private lateinit var visited : ArrayList<Int>
    private var cost : Int

    init {
        visited = ArrayList()
        cost = 0
        visited.add(first)
    }

    private fun updateCost(node: Int){
        val p = Pair(visited.last(),node)
        cost = graph.getArcVal(p)
    }

    fun addNode(node:Int){
        if(visited.indexOf(node)==-1){
            visited.add(node)
            updateCost(node)
           // println("node added")
        }else{
           // return "duplicated item"
        }
        //return ""
    }
    fun isComplete():Boolean= visited.size==graph.order
    fun path() = visited
    fun cost() = cost

    fun greedy(){
        (1..graph.order).forEach{
            _->
            run {
                var next = 0
                var min = Int.MAX_VALUE
                (1..graph.order).forEach { k ->
                    if (visited.indexOf(k) == -1) {
                        if (graph.getArcVal(Pair(visited.last(), k)) < min) {
                            next = k
                            min = graph.getArcVal(Pair(visited.last(), k))
                           // println("min added")
                        }
                    }
                }
                if(next!=0) addNode(next)
            }
        }
    }
    fun doublePrecision(){
        while (!isComplete()){
            var next1 = 0
            var next2 = 0
            var min = Int.MAX_VALUE
            var cmp : Int
            (1..graph.order).forEach { k->if(visited.indexOf(k)==-1){
                (1..graph.order).forEach { l->if (visited.indexOf(l)==-1){
                    cmp = graph.getArcVal(Pair(visited.last(),k))+
                            graph.getArcVal(Pair(k,l))
                    if(cmp<min){
                        next1 = k
                    }
                }
                }
            }
            }
            addNode(next1)
        }
    }
    fun kruskal(){

    }
   /* fun exact(nodes : ArrayList<Int>,rank :Int):ArrayList<Int>{
        if(nodes.size==rank){
            return nodes
        }else{
            var min = null
            (1..graph.order).forEach {
                k->if(nodes.indexOf(k)==-1){

            }
        }
    }
}*/

    fun permutations(){

    }
}