package gui

import swing._
import simulation.Simulation

class SidePanel(val simulation: Simulation) extends BoxPanel(Orientation.Vertical) {
	val simulationTime = new Label("00:00:00")
	
	this.minimumSize.width = 300
	
	contents += new Button("Foo")
	contents += new Button("Bar")
	contents += new Label("Time simulated:")
	contents += simulationTime
	
	private def secondsToString(time: Double): String = {
		val t = time.toLong
		val sec = t % 60
		val min = (t / 60) % 60
		val hrs = (t / 60 / 60) % 24
		val day = (t / 60 / 60 / 24) % 365
		val yrs = (t / 60 / 60 / 24 / 365)
		
		s"$yrs years, $day days\n$hrs:$min:$sec"
	} 
	
	def update() = {
		simulationTime.text = secondsToString(simulation.simulatedTime)
	}
}