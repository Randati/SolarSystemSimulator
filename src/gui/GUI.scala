package gui

import swing._
import java.awt.Graphics2D
import java.awt.BorderLayout
import simulation.Simulation
import simulation.Object
import util.Vec

object GUI extends SimpleSwingApplication {
	val simulation = new Simulation
	
	// Testing
	simulation.addObject(new Object("Sun", 1.9891e30, 100, Vec(0, 0, 0), Vec(0, 0 ,0)))
	simulation.addObject(new Object("Earth", 5.97219e24, 10, Vec(147098290000.0, 0, 0), Vec(0, 29780.0 ,0)))
//	simulation.addObject(new Object("Moon", 7.3477e22, 1, Vec(147098290000.0 + 363295000.0, 0, 0), Vec(0, 29780.0 + 1022.0 ,0)))
//	simulation.addObject(new Object("Earth", 5.97219e24, 30, Vec(0, 0, 0), Vec(0, 0 ,0)))
//	simulation.addObject(new Object("Moon", 7.3477e22, 10, Vec(363295000.0, 0, 0), Vec(0, 1022.0 ,0)))
	
	
	def top = new MainFrame {
		title = "Solar System Simulator"
		minimumSize = new Dimension(400, 300)
		this.centerOnScreen()
		this.maximize()
		
		contents = new BorderPanel {
			layout(new SimulationPanel(simulation)) = BorderPanel.Position.Center
			layout(new SidePanel) = BorderPanel.Position.East
		}
	}
}