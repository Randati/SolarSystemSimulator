package gui

import swing._
import java.awt.Graphics2D
import java.awt.BorderLayout
import simulation.Simulation
import simulation.Object

object GUI extends SimpleSwingApplication {
	val simulation = new Simulation
	
	// Testing
	simulation.addObject(new Object("Sun", 100, 100, (0, 0, 0), (0, 0 ,0)))
	
	def top = new MainFrame {
		title = "Solar System Simulator"
		minimumSize = new Dimension(400, 300)
		
		contents = new BorderPanel {
			layout(new SimulationPanel(simulation)) = BorderPanel.Position.Center
			layout(new SidePanel) = BorderPanel.Position.East
		}
	}
}