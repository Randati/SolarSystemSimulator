package gui

import swing._
import simulation.Simulation

class SimulationPanel(val simulation: Simulation) extends Panel {
	private var offsetX: Double = -size.width / 2
	private var offsetY: Double = -size.height / 2
	private var zoom: Double = 152098232.0 * 2

	
	private val timer = new javax.swing.Timer(1000 / 60, new java.awt.event.ActionListener() {
		override def actionPerformed(e: java.awt.event.ActionEvent)  = {
			repaint()
		}
	})
	
	timer.start()
	
	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		g.setColor(new Color(0, 0, 0))
		g.fillRect(0, 0, size.width, size.height)
		
		
		g.setColor(new Color(255, 255, 255))
		
		offsetX = size.width / 2
		offsetY = size.height / 2
		
		simulation.simulate(60 * 60)
		val objs = simulation.getObjects
		
		for (obj <- objs) {
			println((obj.position.x / zoom - obj.radius + offsetX).toInt, (obj.position.y / zoom - obj.radius + offsetY).toInt)
			g.fillOval(
				(obj.position.x / zoom - obj.radius + offsetX).toInt,
				(obj.position.y / zoom - obj.radius + offsetY).toInt,
				(obj.radius * 2).toInt,
				(obj.radius * 2).toInt)
		}
		
	}
}