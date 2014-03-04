package gui

import swing._
import java.awt.image.BufferedImage
import simulation.Simulation

class SimulationPanel(val simulation: Simulation) extends Panel {
	private var offsetX: Double = -size.width / 2
	private var offsetY: Double = -size.height / 2
	private var zoom: Double = 152098232.0 * 2
	private var buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB)
	ignoreRepaint = true

	private val timer = new javax.swing.Timer(1000 / 120, new java.awt.event.ActionListener() {
		override def actionPerformed(e: java.awt.event.ActionEvent) = {
			repaint()
		}
	})
	timer.start()

	override def paint(screenG: Graphics2D) {
		if (buffer.getWidth != size.width || buffer.getHeight != size.height) {
			buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
		}
		
		val g = buffer.getGraphics
		
		g.setColor(new Color(0, 0, 0))
		g.fillRect(0, 0, size.width, size.height)

		g.setColor(new Color(255, 255, 255))

		offsetX = size.width / 2
		offsetY = size.height / 2

		simulation.simulate(60 * 60)
		val objs = simulation.getObjects

		for (obj <- objs) {
			g.fillOval(
				(obj.position.x / zoom - obj.radius + offsetX).toInt,
				(obj.position.y / zoom - obj.radius + offsetY).toInt,
				(obj.radius * 2).toInt,
				(obj.radius * 2).toInt)
		}
		
		screenG.drawImage(buffer, 0, 0, null)
	}
}