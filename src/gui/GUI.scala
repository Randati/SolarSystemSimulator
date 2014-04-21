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
	var dt = 60 * 60 * 24 * 0.5
	
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
	}
	
	
	// Start simulation thread
	new Thread(new Runnable {
		def run() = {
			SolarSystemReader.loadFile("solar-system.ss", simulation)
			
			while (true) {
				simulation.simulate(dt)
				
				for (obj <- simulation.getObjects)
					simulationPanel.trails.push((obj.position, new Color(255, 255, 255)))
					
//				Thread.sleep(1000 / 60)
			}
		}
	}).start()
	
	
	// Start drawing timer
	new javax.swing.Timer(1000 / 60, new java.awt.event.ActionListener() {
		override def actionPerformed(e: java.awt.event.ActionEvent) = {
			simulationPanel.repaint()
			sidePanel.update()
		}
	}).start()
	
}