package gui

import swing._
import javax.swing.border._
import simulation.Simulation

class SidePanel(val simulation: Simulation) extends BoxPanel(Orientation.Vertical) {
	preferredSize = new Dimension(200, 600)
	
	contents += new BoxPanel(Orientation.Horizontal) {
		contents += new Button("Pause")
		contents += new Button("Reset")
	}
	
	
	contents += new Label("Simulated time")
	
	contents += new GridPanel(4, 2) {
		hGap = 10
		
		val t = simulation.simulatedTime.toLong
		
		class RLabel(s: String) extends Label(s) { horizontalAlignment = Alignment.Right }
		class LLabel(s: String) extends Label(s) { horizontalAlignment = Alignment.Left }
		
		contents += new RLabel((t / 60 / 60 / 24 / 365).toString)
		contents += new LLabel("years")
		
		contents += new RLabel(((t / 60 / 60 / 24) % 365).toString)
		contents += new LLabel("days")
		
		contents += new RLabel(((t / 60 / 60) % 24).toString)
		contents += new LLabel("hours")
		
		contents += new RLabel(((t / 60) % 60).toString)
		contents += new LLabel("minutes")
	}
	
	contents += new BoxPanel(Orientation.Horizontal) {
		contents += new Label("Speed")
		contents += new Slider()
	}
	
	contents += new BoxPanel(Orientation.Horizontal) {
		contents += new Label("Accuracy")
		contents += new Slider()
	}
	
	contents += new CheckBox("Free camera")
	
	contents += Swing.VStrut(20)
	
	
	contents += new BoxPanel(Orientation.Vertical) {
		border = new TitledBorder("Object")
		
		contents += new ComboBox(Array("Foo", "Bar"))
		
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new Label("Mass")
			contents += new TextField()
		}
		
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new Label("Radius")
			contents += new TextField()
		}
		
		contents += new Label("Position")
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new TextField()
			contents += new TextField()
			contents += new TextField()
		}
		
		contents += new Label("Velocity")
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new TextField()
			contents += new TextField()
			contents += new TextField()
		}
		
		contents += new Separator(Orientation.Vertical)
	}
	
	def update() = {
		
	}
}