package gui

import swing._
import java.awt.Graphics2D
import java.awt.BorderLayout
import simulation.Simulation
import simulation.Object
import util.Vec
import util.SolarSystemReader



object GUI extends SimpleSwingApplication {
	var simulationPaused = false
	var simSecPerSec = 60 * 60 * 24 * 0.5
	var ticksPerSec = 30.0
	
	private var currentSimulation = new Simulation(Vector())
	private var loadedObjects = Vector[Object]()
	private var collision = false
	
	private val simulationPanel = new SimulationPanel
	private val sidePanel = new SidePanel(simulationPanel)
	
	
	def simulation = currentSimulation
	
	def resetSimulation() = {
		currentSimulation = new Simulation(loadedObjects)
		simulationPanel.trails.clear()
		sidePanel.updateObjectList()
	}
	
	
	// Build GUI
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
			loadedObjects = SolarSystemReader.loadFile("solar-system.ss")
			resetSimulation()
			
			var tickTime = 0.0
			while (true) {
				val dt = simSecPerSec / ticksPerSec
				val simTimeInSec = ticksPerSec * tickTime
				val sleepTimeInSec = 1.0 - simTimeInSec
				val sleepThisTick = Math.max(sleepTimeInSec / ticksPerSec, 0.0)
				
				
				if (!simulationPaused && !collision) {
					val startTime = System.nanoTime()
					val collided = simulation.simulate(dt)
					
					if (collided)
						collision = true
					
					for (obj <- simulation.getObjects)
						simulationPanel.trails.push((obj.position, new Color(255, 255, 255)))
					
					tickTime = (System.nanoTime() - startTime) / 1e9
				}
				Thread.sleep((sleepThisTick * 1000).toLong)
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