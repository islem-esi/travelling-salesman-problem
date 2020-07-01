package control

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextField
import com.sun.glass.ui.Robot
import com.sun.javafx.robot.FXRobotFactory.createRobot
import com.sun.javafx.robot.FXRobotImage
import javafx.embed.swing.SwingFXUtils
import javafx.event.Event
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.SnapshotParameters
import javafx.scene.control.*
import javafx.scene.image.WritableImage
import javafx.scene.input.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.stage.FileChooser
import javafx.stage.Window
import model.CompleteGraph

import model.GraphNode
import model.Path
import java.io.*
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import javax.imageio.ImageIO.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Created by HPPC on 11/10/2018.
 */
class CreateController:Initializable {

    @FXML lateinit var order : JFXTextField
    @FXML lateinit var drawArea: AnchorPane
    @FXML lateinit var solve : JFXButton
    @FXML lateinit var nodeNum : Label
    @FXML lateinit var coutExacte : Label
    @FXML lateinit var approximation : Label
    @FXML lateinit var pathapproxim1 : Label
    @FXML lateinit var pathapproxim2 : Label
    @FXML lateinit var pathExacte1 : Label
    @FXML lateinit var pathExacte2 :Label
    @FXML lateinit var e1 : Rectangle
    @FXML lateinit var e2 : Rectangle
    @FXML lateinit var e3 : Label
    @FXML lateinit var e4 : Label
    @FXML lateinit var h1 : VBox
    @FXML lateinit var h2 : VBox
    @FXML lateinit var startpoint : JFXComboBox<Int>
    @FXML lateinit var texec : Label
    @FXML lateinit var texec2: Label

    companion object Instance : Serializable{
        lateinit var graph :CompleteGraph
        var center = Pair(650.0,350.0)
        var radius = 100.0
        var drawn = false
        var nodes = ArrayList<GraphNode>()
        lateinit var selectedNode : GraphNode
        var isSelectedNode = false
        var linesLabels: HashMap<Pair<Int,Int>,Triple<Line,TextField,Label>> = HashMap()
        var first = 1
    }


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
      /* colors.add(Pair("green",false))
        colors.add(Pair("blue",false))
        colors.add(Pair("orange",false))
        colors.add(Pair("yellow",false))
        colors.add(Pair("magenta",false))
        colors.add(Pair("Chartreuse",false))
        colors.add(Pair("Chocolate",false))
        colors.add(Pair("red",false))
        colors.add(Pair("Gray",false))
        colors.add(Pair("Indigo",false))
        colors.add(Pair("OrangeRed",false))
        colors.add(Pair("SpringGreen",false))
        colors.add(Pair("YellowGreen",false))
        colors.add(Pair("YellowGreen",false))
        colors.add(Pair("Aqua",false))
        colors.add(Pair("black",false))

        selectedBtn.text=""
        addButton.onAction = EventHandler {
            _ -> handleAdd()
        }
        drawingArea.onMouseClicked = EventHandler {
            e -> handleDraw(e.x,e.y)
        }
        export.onAction= EventHandler {
            saveAsPng()
        }
        redo.onAction= EventHandler {
            var oui = ButtonType("OUI")
            var alert = Alert(Alert.AlertType.NONE,"Voulez vous supprimer le graph?",oui,ButtonType("NON") )
            //alert.alertType = Alert.AlertType.NONE
            alert.title= "Confirmer pour refaire"
            alert.showAndWait()
            if(alert.result==oui) init()
        }
        mark.onAction= EventHandler {markArticNodes()}
        undo.onAction = EventHandler { undo() }*/




       e1.visibleProperty().set(false)
       e2.visibleProperty().set(false)
       e3.visibleProperty().set(false)
       e4.visibleProperty().set(false)
       h1.visibleProperty().set(false)
       h2.visibleProperty().set(false)

        order.onKeyPressed = EventHandler {
            event -> if(event.code==KeyCode.ENTER){
            graph = CompleteGraph(order.text.toInt())
            println(graph.order)
            if(drawn){
                var oui = ButtonType("OUI")
                var alert = Alert(Alert.AlertType.NONE,"Voulez vous supprimer le graph?",oui,ButtonType("NON") )
                //alert.alertType = Alert.AlertType.NONE
                alert.title= "Confirmer pour refaire"
                alert.showAndWait()
                if(alert.result==oui) {
                    deleteGraph()
                    for(i in 1..graph.order) startpoint.items.add(i)
                    drawGraph(graph)
                }
            }else {
                drawGraph(graph)
                for(i in 1..graph.order) startpoint.items.add(i)
            }
        }
        }
        solve.onAction = EventHandler {
            _->solve()
        }
        startpoint.onAction = EventHandler {
            _-> if(startpoint.selectionModel.selectedItem!=null) first = startpoint.selectionModel.selectedItem
        }

    }
    private fun drawGraph(graph: CompleteGraph){
        for (k in 1..graph.order){
            var node = GraphNode(k,graph.order,radius)
            node.layoutX = node.centerX+center.first
            node.layoutY = node.centerY+center.second
            node.onMouseClicked = EventHandler {
                _-> addArcVal(node)
            }
            var lab = Label(k.toString())
            lab.style = "-fx-text-fill : white;-fx-font-size : 18"
            val center1 = calcCenter(k,graph.order, 200.0)
            lab.layoutX = center.first+center1.first-5
            lab.layoutY = center.second+center1.second-10
            drawArea.children.add(node)
            drawArea.children.add(lab)
            nodes.add(node)
            for(i in (k+1)..graph.order){
                val line = Line()
               // line.fill = Color.BLUEVIOLET
                val center1 = calcCenter(k,graph.order, 200.0)
                val center2 = calcCenter(i,graph.order, 200.0)
                line.startX = center.first+center1.first
                line.startY = center.second+center1.second
                line.endX = center.first+center2.first
                line.endY = center.second+center2.second
               // line.onMouseClicked = EventHandler { arcClicked(k,i) }
                var lab = Label("0")
                lab.layoutX = (line.startX + line.endX)/2
                lab.layoutY = (line.startY + line.endY)/2
                lab.style = "-fx-text-fill : green;-fx-font-size : 18"
                if(i!=k+1 && !(k==1 && i==graph.order)){
                    lab.layoutX = (line.startX + lab.layoutX)/2
                    lab.layoutY = (line.startY + lab.layoutY)/2
                }
                drawArea.children.add(lab)
                drawArea.children.add(line)
                linesLabels[Pair(k,i)] = Triple(line,TextField(),lab)
                linesLabels[Pair(i,k)] = Triple(line,TextField(),lab)
            }
        }
        drawn = true
    }
    private fun deleteGraph(){
        drawArea.children.removeAll(nodes)
        for(i in 1..nodes.size){
            for (j in (i+1)..nodes.size){
                drawArea.children.remove(linesLabels[Pair(i,j)]!!.first)
                drawArea.children.remove(linesLabels[Pair(i,j)]!!.third)
            }
        }
        linesLabels = HashMap()
        nodes.removeAll(nodes)
        e1.visibleProperty().set(false)
        e2.visibleProperty().set(false)
        e3.visibleProperty().set(false)
        e4.visibleProperty().set(false)
        h1.visibleProperty().set(false)
        h2.visibleProperty().set(false)
        first = 1
        startpoint.items.removeAll(startpoint.items)

    }
    fun calcCenter(rank: Int,order: Int,r: Double):Pair<Double,Double>{
        return Pair(r* cos(2*3.14/order*rank),r* sin(2*3.14/order*rank))
    }

    fun arcClicked(n1: Int,n2:Int){
        println(n1)
        println(n2)
    }

    fun addArcVal(node: GraphNode) {
        if (isSelectedNode) {
         //   graph.setArcVal(Pair(nodes.indexOf(selectedNode) + 1, nodes.indexOf(node) + 1))
            var inp = TextInputDialog()
            inp.title = "Evaluer l'arc"
            inp.contentText = "Valeur"
            inp.headerText=""
            inp.graphic = null
            var result = inp.showAndWait()
            while(!result.isPresent);
            graph.setArcVal(Pair(selectedNode.rk,node.rk),result.get().toInt())
           // graph.setArcVal(Pair(node.rk,selectedNode.rk),result.get().toInt())
            linesLabels[Pair(selectedNode.rk,node.rk)]!!.third.text = result.get()
            isSelectedNode=false
            nodeNum.text = ""
        }else{
            isSelectedNode = true
            selectedNode = node
            nodeNum.text = node.rk.toString()
        }
    }
    fun solve(){

        //exact solution4444
        var pt = ArrayList<Int>()
        //pt.add(first)
        var p = graph.exact(graph.order,pt)
       // println(p)
        var cost = costOf(p)
        coutExacte.text = cost.toString()
        pathExacte1.text = p.toString()
        pathExacte2.text = ""
        texec.text = graph.order.toString()
        //approximative
        var p1 = Path(graph,first)
        p1.greedy()
        var cost1 = costOf(p1.path())
        approximation.text = cost1.toString()
        pathapproxim1.text = p1.path().toString()
        pathapproxim2.text = ""
        texec2.text = "1 ms"
        //show solution
        e1.visibleProperty().set(true)
        e2.visibleProperty().set(true)
        e3.visibleProperty().set(true)
        e4.visibleProperty().set(true)
        h1.visibleProperty().set(true)
        h2.visibleProperty().set(true)

    }
    private fun costOf(list: ArrayList<Int>):Int{
        var fst = list.first()
        var cost = 0
        for(i in 1..(list.size-1)){
            cost += graph.getArcVal(Pair(fst,list[i]))
            fst = list[i]
        }
        cost += graph.getArcVal(Pair(list.last(),list.first()))
        return cost
    }
/*
    private fun handleNode(node: JFXButton){
        if(selected && node!== selectedButton) {
            var line = Line()
            var st = intersection(selectedButton.layoutX,selectedButton.layoutY,node.layoutX,node.layoutY)
            var ed = intersection(node.layoutX,node.layoutY,selectedButton.layoutX, selectedButton.layoutY)

            //  line.startX = selectedButton.layoutX + 0.5* nodeSize
            line.startX = st.first+ nodeSize/2
            //line.endX = node.layoutX + 0.5* nodeSize
                line.endX = ed.first + nodeSize/2
            //line.startY = selectedButton.layoutY + 0.5* nodeSize
                line.startY= st.second +nodeSize/2
            //line.endY = node.layoutY + 0.5* nodeSize
                line.endY = ed.second + nodeSize/2
            // println(line.style)
            var nodeColor=""
            for(e in nodes){
                //println("ec"+e.second.text)
                if(e.second=== selectedButton) {
                    nodeColor=e.third
                   // println("c" + e.second.text)
                    break
                }
            }
            line.style = "-fx-stroke: $nodeColor;"


            selectedBtn.text=""
            var node1: GraphNode<Int> = GraphNode(1, ArrayList())
            var node2: GraphNode<Int> = GraphNode(1, ArrayList())
            for(e in nodes){
                if(e.second===node) {
                    node2=e.first
                  //  println(e.second.text)
                }
                else if(e.second=== selectedButton){
                    node1=e.first
                  //  println(e.second.text)
                }
            }
            if(arcs[node1.container()]==null) arcs[node1.container()]=ArrayList()
            arcs[node1.container()]!!.add(line)

            drawingArea.children.add(line)
            selected = false
            addEdge(node1,node2,nodeColor)
        } else if(selected && node === selectedButton) {
            selected = false
            selectedBtn.text = ""
        }
        else {
            selected = true
            selectedButton = node
            selectedBtn.text=node.text
        }
    }
    private fun handleAdd(){
        addNode=!addNode
    }

    private fun handleDraw(x: Double, y:Double){
        if (addNode){
            addNode(x,y)
            selected = false
        }else if(selected) {
            selected =false
            selectedBtn.text=""
        }
    }

    private fun drawNode(x:Double,y: Double):JFXButton{
        var node = JFXButton()
        node.style = "-fx-background-color: black"  +
                "; -fx-background-radius: 10em;-fx-text-fill: white;" +
                "-fx-font-size:15;-fx-font-family:Arial;"
        node.cursor= Cursor.HAND
        var test = false
        for (i in 0..(colors.size-1)){
            if(!colors[(i+ colorsIndex)% colors.size].second){
                node.style = "-fx-background-color: " + colors[(i+ colorsIndex)% colors.size].first +
                        "; -fx-background-radius: 10em;-fx-text-fill: white;" +
                        "-fx-font-size:15;-fx-font-family: Arial;"
                colors[(i+ colorsIndex)% colors.size]= Pair(colors[(i+ colorsIndex)% colors.size].first,true)
                lastColor = (i+ colorsIndex)%colors.size
                colorsIndex= (i+ colorsIndex)% colors.size
                println(colorsIndex)
                test= true
                break
            }
        }
        if(!test)  colorsIndex=15
           /* if (colorsIndex< colors.size) {
                node.style = "-fx-background-color: " + colors[colorsIndex].first +
                        "; -fx-background-radius: 10em;-fx-text-fill: white;"

                colors[colorsIndex]= Pair(colors[colorsIndex].first,true)
                colorsIndex++
            }*/
        //node.style = addButton.style
        //println(node.style)

        node.text = count.toString()
        node.layoutX = x
        node.layoutY = y
        node.prefHeight = nodeSize
        node.prefWidth = nodeSize
        node.onAction = EventHandler {
            _ -> handleNode(node)
        }
        drawingArea.children.add(node)
        count++
        return node
    }

    private fun addNode(x:Double,y: Double){
        var element = GraphNode(0,ArrayList<GraphNode<Int>>())
        nodes.add(Triple(element,drawNode(x,y), colors[colorsIndex].first))
        graph.addNode(element.container())
        element.container.list().add(element)
        lastButton = nodes.last().second
        lastNode = element
    }


    private fun addEdge(node1: GraphNode<Int>,node2:GraphNode<Int>,nodeColor:String){
        node1.adjacents().add(node2)
        node2.adjacents().add(node1)
        if(node1.container()!==node2.container()){
            if(arcs[node2.container]!=null) arcs[node1.container]!!.addAll(arcs[node2.container]!!.toList())
            val cnx = node2.container()
            cnx.list().forEach(
                    {
                        e->e.container = node1.container
                        node1.container.list().add(e)
                        nodes.forEach {
                            f->if(f.first===e){
                            if(colors.indexOf(Pair(f.third,true))!=-1) colors[colors.indexOf(Pair(f.third,true))]=Pair(f.third,false)
                            nodes[nodes.indexOf(f)]= Triple(f.first,f.second,nodeColor)
                            f.second.style="-fx-background-color: $nodeColor"+
                                    "; -fx-background-radius: 10em;-fx-text-fill:white;" +
                                    "-fx-font-size:17;-fx-font-family: Arial;"
                        }
                        }
                    }
            )
            if(arcs[node2.container]!=null)
            arcs[node2.container]!!.forEach {
              e->e.style = "-fx-stroke: $nodeColor;"
            }
        }
        /*
        if(node1.container.isArticulationPoint(node1)){
            addArtiNode(node1)
        }else deleteArtiNode(node1)
        */
        if(node1.artic){
            if(!node1.container.isArticulationPoint(node1)) deleteArtiNode(node1)
        }else{
            if(node1.container.isArticulationPoint(node1)) addArtiNode(node1)
        }

        if(node2.artic){
            if(!node2.container.isArticulationPoint(node2)) deleteArtiNode(node2)
        }else{
            if(node2.container.isArticulationPoint(node2)) addArtiNode(node2)
        }

       /* if(node2.container.isArticulationPoint(node2)){
            addArtiNode(node2)
        }else deleteArtiNode(node2)*/

        val forDeletion : ArrayList<Triple<GraphNode<Int>,JFXButton,String>> = ArrayList()
        var cont:HBox
        articNodes.forEach {
            e->
            run {
                //println("traiter art" + e.second.text)
                if (!e.first.container.isArticulationPoint(e.first)) {

                    for (hb in articulationSet.children) {
                        cont = hb as HBox
                        cont.children.remove(e.second)

                    }
                    forDeletion.add(e)
                }
            }
        }
        forDeletion.forEach { e-> articNodes.remove(e)}
        lastButton = null
        lastNode = null
    }
    fun addArtiNode(point: GraphNode<Int>){
        val art = JFXButton()
        art.prefWidth= nodeSize
        art.prefHeight = nodeSize
        art.style = "-fx-background-color: green"  +
                "; -fx-background-radius: 10em;-fx-text-fill: white;" +
                "-fx-font-size:15;-fx-font-family:Arial;"
        for(e in nodes){
            if(e.first===point){
                art.text = e.second.text
                break
            }
        }
        articNodes.add(Triple(point,art,""))
        if(hboxmod==3){
            var hb = HBox()
            hb.spacing = 10.0
            articulationSet.children.add(hb)
            hboxmod=0
        }
       (articulationSet.children.last() as HBox).children.add(art)
        hboxmod++

                //add(art)
    }
    fun deleteArtiNode(point: GraphNode<Int>){
        var cont : HBox
        for(e in articNodes){
            if(e.first===point){
                //articulationSet.children.remove(e.second)
                for(hb in articulationSet.children){
                    cont = hb as HBox
                    cont.children.remove(e.second)
                }
                articNodes.remove(e)
                break
            }
        }
      //  hboxmod = (articulationSet.children.last() as HBox).children.size
    }
    fun saveAsPng(){
        var image: WritableImage
        image = drawingArea.snapshot(SnapshotParameters(),null)
        var file = File("snapshot.png")
        write(SwingFXUtils.fromFXImage(image, null), "png", file)
    }
    fun save(){
      /*  var fil = FileOutputStream("save.ser")
        var obj = ObjectOutputStream(fil)
        obj.writeObject()
        obj.close()
        fil.close()*/
    }
    fun drawColoredVertex(xs: Double,ys:Double,xf:Double,yf:Double,color: String){
        var line = Line()
        line.startX = xs + 0.5* nodeSize
        line.endX = xf + 0.5* nodeSize
        line.startY = ys + 0.5* nodeSize
        line.endY = yf + 0.5* nodeSize
        line.style = "-fx-stroke: $color;"
        drawingArea.children.add(line)
    }
    fun init(){
        graph  = Graph()
        addNode  = false
        addEdge  = false
        count  = 1
        selected  = false
        selectedButton  = JFXButton()
        nodeSize = 45.0
        colors = ArrayList<Pair<String,Boolean>>()
        colorsIndex = 0
        nodes  = ArrayList()
        articNodes = ArrayList()
        hboxmod = 3
        arcs = HashMap()
        lastButton =null
        lastNode = null

        drawingArea.children.removeAll(drawingArea.children)
        articulationSet.children.removeAll(articulationSet.children)
        selectedBtn.text=""
        colors.add(Pair("green",false))
        colors.add(Pair("blue",false))
        colors.add(Pair("orange",false))
        colors.add(Pair("yellow",false))
        colors.add(Pair("magenta",false))
        colors.add(Pair("Chartreuse",false))
        colors.add(Pair("Chocolate",false))
        colors.add(Pair("red",false))
        colors.add(Pair("Gray",false))
        colors.add(Pair("Indigo",false))
        colors.add(Pair("OrangeRed",false))
        colors.add(Pair("SpringGreen",false))
        colors.add(Pair("YellowGreen",false))
        colors.add(Pair("YellowGreen",false))
        colors.add(Pair("Aqua",false))
        colors.add(Pair("black",false))
    }
    fun markArticNodes(){
       if(!marked) {
           nodes.forEach { e ->
               run {
                   if(e.first.artic) {
                       var a = e.second.style.split(";")

                       var st ="-fx-background-color: OrangeRed;-fx-background-radius: 1em;" + a[2] + ";" + a[3] + ";" + a[4] + ";"
                       e.second.style = st
                   }
               }
           }
           marked = true
       }else{
           nodes.forEach { e ->
               run {
                   if(e.first.artic) {
                       var a = e.second.style.split(";")
                       var st ="-fx-background-color: ${e.third};-fx-background-radius: 10em;" + a[2] + ";" + a[3] + ";" + a[4] + ";"
                       e.second.style = st
                   }
               }
           }
           marked  = false
       }
    }
    fun intersection(xs: Double,ys:Double,xf:Double,yf:Double):Pair<Double,Double>{
        var a  = if((ys-yf) != 0.0) (ys-yf)/(xs-xf)
            else 0.0
        var b  = ys - (xs * a)
        var r = nodeSize/2
        var A = a*a +1.0
        var B = 2*a*(b-ys)-2*xs
        var C = (b-ys)*(b-ys)+xs*xs-r*r
        var delta = B*B -4 * A * C
        var x1 = (-B - sqrt(delta))/(2*A)
        var x2 = (-B + sqrt(delta))/(2*A)
        var y1 = a*x1 +b
        var y2 = a*x2 +b
        if(xs<xf){
            if(x1>x2) return Pair(x1,y1)
            else return Pair(x2,y2)
        }
        else{
            if(x1<x2) return Pair(x1,y1)
            else return Pair(x2,y2)
        }
    }
    fun undo(){
        if(lastButton!=null){
            drawingArea.children.remove(lastButton)
            nodes.remove(nodes.last())
            colors[lastColor]=Pair(colors[lastColor].first,false)
            graph.removeCx(lastNode!!.container)
            count--
        }
    }
*/
}