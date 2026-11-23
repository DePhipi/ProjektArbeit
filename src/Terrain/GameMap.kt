package Terrain

import javafx.geometry.Point3D
import javafx.scene.*
import javafx.scene.paint.Color
import javafx.scene.transform.Rotate
import javafx.scene.transform.Translate
import javafx.stage.Stage

public class GameMap(
    val map: List<String>
) {

    val world = Group()

    fun startScene(stage: Stage) {

        stage.scene = Scene(world, 800.0, 600.0, true, SceneAntialiasing.BALANCED)
        stage.show()
        stage.scene.fill = Color.web("#a8d4f5")

        addLightning()
        addCamera(stage)
        generateMap()
    }

    fun addLightning() {
        world.children.add(AmbientLight(Color.web("#8a9bb5")))
        world.children.add(DirectionalLight(Color.web("#fff1d6")).apply {
            direction = Point3D(-0.6, 1.0, 0.4)
        })
    }

    fun addCamera(stage: Stage) {
        val center = map.size * BLOCK_WIDTH / 2.0
        val position = Translate(center, 0.0, center)
        val rotation = Rotate(0.0, Rotate.Y_AXIS)
        val pitchDown = Rotate(-35.0, Rotate.X_AXIS)
        val zoom  = Translate(0.0, 0.0, -150.0)

        val camera = PerspectiveCamera(true).apply {
            nearClip = 1.0
            farClip = 2000.0
            transforms.addAll(position, rotation, pitchDown, zoom)
        }

        //Cam zoom
        stage.scene.setOnScroll { e ->
            zoom.z = (zoom.z + e.deltaY * 0.5).coerceIn(-1000.0, -20.0)
        }

        //Cam turn
        var lastX = 0.0

        stage.scene.setOnMousePressed { e ->
            lastX = e.sceneX
        }

        stage.scene.setOnMouseDragged {e ->
            val dragX = lastX - e.sceneX
            lastX = e.sceneX

            rotation.angle -= dragX * 0.1
        }

        stage.scene.camera = camera
    }

    fun generateMap() {

        for (z in map.indices) {
            for (x in map[z].indices) {

                val blockType = when (map[z][x]) {
                    'A' -> Materials.GRASS
                    'B' -> Materials.WATER
                    'C' -> Materials.STONELVL1
                    'D' -> Materials.STONELVL2
                    'E' -> Materials.STONELVL3
                    'F' -> Materials.SNOW
                    'G' -> Materials.SAND
                    else -> Materials.GRASS
                }

                val block = Block().generateBlock(blockType).apply {
                    translateX = BLOCK_WIDTH * x
                    translateZ = BLOCK_WIDTH * z
                };


                //Todo randomise coin spawning
                if (block.material == Materials.GRASS.material) {
                    val coin = Block().generateCoin().apply {
                        translateX = BLOCK_WIDTH * x
                        translateZ = BLOCK_WIDTH * z
                    }
                    //world.children.add(coin)
                }

                if (block.material == Materials.GRASS.material) {
                    val tree = Block().generateTree().apply {
                        translateX = BLOCK_WIDTH * x
                        translateZ = BLOCK_WIDTH * z
                    }
                    //world.children.add(tree)
                }
                world.children.add(block)
            }
        }


    }
}