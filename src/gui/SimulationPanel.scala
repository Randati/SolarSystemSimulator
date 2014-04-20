package gui

import swing._
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import simulation.Simulation

class SimulationPanel(val simulation: Simulation) extends Panel {
	private var offsetX: Double = -size.width / 2
	private var offsetY: Double = -size.height / 2
	private var zoom: Double = 9.361466974610922E+08 * 0.005
	private var buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB)
	ignoreRepaint = true

	
	override def paint(screenG: Graphics2D) {
		if (buffer.getWidth != size.width || buffer.getHeight != size.height) {
			buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
		}
		
		
		val g = buffer.getGraphics.asInstanceOf[Graphics2D]
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		
		g.setColor(new Color(0, 0, 0))
		g.fillRect(0, 0, size.width, size.height)

		g.setColor(new Color(255, 255, 255))

		offsetX = size.width / 2
		offsetY = size.height / 2
		
		val objs = simulation.getObjects

		for (obj <- objs) {
			val radius = math.max(obj.radius / 400000, 1.0)
			val x = (obj.position.x - objs(3).position.x) / zoom + offsetX - radius
			val y = (obj.position.z - objs(3).position.z) / zoom + offsetY - radius
			val col = if (obj.position.y < 0) 100 else 255
			
			g.setColor(new Color(col, col, col))
			g.fillOval(x.toInt, y.toInt, (radius * 2).toInt, (radius * 2).toInt)
		}
		
		
		screenG.drawImage(buffer, 0, 0, null)
	}
}