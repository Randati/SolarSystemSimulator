package gui

import swing._
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import simulation.Simulation

class SimulationPanel(val simulation: Simulation) extends Panel {
	private var offsetX: Double = -size.width / 2
	private var offsetY: Double = -size.height / 2
	private var zoom: Double = 9.361466974610922E+08 * 3
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
			val radius = math.max(obj.radius / 50000000, 3.0)
			val x = (obj.position.x - obj.radius) / zoom + offsetX
			val y = (obj.position.y - obj.radius) / zoom + offsetY
			
			g.fillOval(x.toInt, y.toInt, radius.toInt, radius.toInt)
		}
		
		
		screenG.drawImage(buffer, 0, 0, null)
	}
}