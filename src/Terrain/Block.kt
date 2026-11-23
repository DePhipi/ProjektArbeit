package Terrain;

import javafx.scene.Group
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.Box
import javafx.scene.shape.Cylinder
import javafx.scene.shape.Sphere
import javafx.scene.transform.Rotate

const val BLOCK_WIDTH = 10.0;
const val BLOCK_HEIGT = 5.0;

enum class Materials(val color: Color, val height: Double) {
    WATER(Color.web("#3d7fd6"), 0.6),
    SAND(Color.web("#e8d9a0"), 1.0),
    GRASS(Color.web("#6cc25a"), 1.4),
    STONELVL1(Color.web("#9a958e"), 2.4),
    STONELVL2(Color.web("#86817a"), 3.4),
    STONELVL3(Color.web("#726d67"), 4.4),
    SNOW(Color.web("#f4f6f8"), 5.4);

    val variants = listOf(0.9, 0.96, 1.0, 1.05).map { b ->
        PhongMaterial(color.deriveColor(0.0, 1.0, b, 1.0)).apply {
            specularColor = Color.BLACK
        }
    }

    //Color variants
    fun randomMaterial() = variants.random()

    //Default colors
    val material = PhongMaterial(color).apply {
        specularColor = Color.BLACK
    }
}

public class Block {

    public fun generateBlock(type: Materials): Box {
        //Todo block height dosent work yet. Adjust so the height goes all the way down
        val block = Box(BLOCK_WIDTH, BLOCK_HEIGT * type.height, BLOCK_WIDTH).apply {
            translateY -= type.height * BLOCK_HEIGT
        }
        block.material = type.randomMaterial();

        return block
    }

    private val goldMaterial = PhongMaterial(Color.GOLD).apply {
        specularColor = Color.WHITE
    }


    //Todo maybe add coin spin?
    fun generateCoin(): Cylinder {
        return Cylinder(BLOCK_HEIGT / 2, 0.5, 24).apply {
            translateY -= 3 * BLOCK_HEIGT
            material = goldMaterial
            rotationAxis = Rotate.X_AXIS
            rotate = 90.0
        }
    }

    //Todo Make trees pretier look like shit
    fun generateTree(): Group {
        val tree = Group()

        val trunk = Cylinder(BLOCK_WIDTH / 10, BLOCK_HEIGT / 2).apply {
            translateY -= (Materials.GRASS.height + 1) * BLOCK_HEIGT
        }
        val leaves = Sphere(BLOCK_WIDTH / 5).apply {
            translateY -= (Materials.GRASS.height + 1) * BLOCK_HEIGT + BLOCK_WIDTH / 2
        }

        tree.children.add(trunk)
        tree.children.add(leaves)

        return tree
    }
}
