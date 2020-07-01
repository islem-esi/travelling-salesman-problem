import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Created by HPPC on 10/19/2018.
 */
class Entry:Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //FXMLLoader.setDefaultClassLoader(javaClass.classLoader)
        val fxmlLoader = FXMLLoader( Entry::class.java.getResource("/gui/createGraph.fxml"))
        val create = fxmlLoader.load<Parent>()
        val scene = Scene(create)
        primaryStage.scene = scene
        primaryStage.show()
    }
    companion object {
        @JvmStatic
        fun main(args:Array<String>){
            launch(Entry::class.java)
        }

}
}