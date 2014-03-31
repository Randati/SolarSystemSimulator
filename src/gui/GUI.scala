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
	simulation.addObject(new Object("Sun",     1.9891e30,    1, Vec(              0, 0, 0), Vec(0,       0 ,0)))
	simulation.addObject(new Object("Mercury", 3.3022e23,    2.5 * 0.4, Vec(  46001200000.0, 0, 0), Vec(0, 47870.0 ,0)))
	simulation.addObject(new Object("Venus",   4.8676e24,    2.5 * 0.9, Vec( 107477000000.0, 0, 0), Vec(0, 35020.0 ,0)))
	simulation.addObject(new Object("Earth",   5.97219e24,   2.5 * 1.0, Vec( 147098290000.0, 0, 0), Vec(0, 29780.0 ,0)))
	simulation.addObject(new Object("Mars",    6.4185e23,    2.5 * 0.5, Vec( 206669000000.0, 0, 0), Vec(0, 24077.0 ,0)))
	simulation.addObject(new Object("Jupiter", 1.8986e27,    2.5 * 10.5, Vec( 740573600000.0, 0, 0), Vec(0, 13070.0 ,0)))
	simulation.addObject(new Object("Saturn",  5.6846e26,    2.5 * 9.0, Vec(1353572956000.0, 0, 0), Vec(0,  9690.0 ,0)))
	simulation.addObject(new Object("Uranus",  8.6810e25,    2.5 * 3.9, Vec(2748938461000.0, 0, 0), Vec(0,  6810.0 ,0)))
	simulation.addObject(new Object("Neptune", 1.0243e26,    2.5 * 3.8, Vec(4452940833000.0, 0, 0), Vec(0,  5430.0 ,0)))
//	simulation.addObject(new Object("Jupiter", 1.8986e27,    2.5 * 10.5, Vec( 740573600000.0, 0, 0), Vec(0, 0 ,0)))
//	simulation.addObject(new Object("Jupiter", 1.8986e27,    2.5 * 10.5, Vec( -740573600000.0, 0, 0), Vec(0, 0 ,0)))

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