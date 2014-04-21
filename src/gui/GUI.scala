package gui

import swing._
import java.awt.Graphics2D
import java.awt.BorderLayout
import simulation.Simulation
import simulation.Object
import util.Vec
import util.SolarSystemReader

object GUI extends SimpleSwingApplication {
	val simulation = new Simulation
	
	private val simulationPanel = new SimulationPanel(simulation)
	private val sidePanel = new SidePanel(simulation)
	
	def top = new MainFrame {
		title = "Solar System Simulator"
		minimumSize = new Dimension(400, 300)
		this.centerOnScreen()
		this.maximize()
		
		contents = new BorderPanel {
			layout(simulationPanel) = BorderPanel.Position.Center
			layout(sidePanel) = BorderPanel.Position.East
		}
		
		SolarSystemReader.loadFile("solar-system.ss", simulation)
	}
	
	
	// TODO: Move physics to another thread
	private val timer = new javax.swing.Timer(1000 / 60, new java.awt.event.ActionListener() {
		override def actionPerformed(e: java.awt.event.ActionEvent) = {
			
			val dt: Double = 60 * 60 * 24 * 0.5 * 1
			simulation.simulate(dt)
			simulationPanel.repaint()
			sidePanel.update()
		}
	})
	timer.start()

}