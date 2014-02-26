package gui

import swing._
import simulation.Simulation

class SimulationPanel(val simulation: Simulation) extends Panel {
	
	override def paintComponent(g: Graphics2D) {
		super.paintComponent(g)
		g.setColor(new Color(0, 0, 0))
		g.fillRect(0, 0, size.width, size.height)
		
		
		g.setColor(new Color(255, 255, 255))
		
		val objs = simulation.getObjects
		
		for (obj <- objs) {
			g.fillOval(
				(obj.position._1 - obj.radius).toInt,
				(obj.position._2 - obj.radius).toInt,
				(obj.radius * 2).toInt,
				(obj.radius * 2).toInt)
		}
		
	}
}