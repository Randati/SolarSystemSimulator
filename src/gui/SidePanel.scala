package gui

import swing._
import javax.swing.border._
import simulation.Simulation

class SidePanel(val simulation: Simulation) extends BoxPanel(Orientation.Vertical) {
	preferredSize = new Dimension(200, 0)
	
	class RLabel(s: String = "") extends Label(s) { horizontalAlignment = Alignment.Right }
	class LLabel(s: String = "") extends Label(s) { horizontalAlignment = Alignment.Left }
	
	
	val pauseButton = new Button("Pause")
	val resetButton = new Button("Reset")
	
	val yearsLabel   = new RLabel
	val daysLabel    = new RLabel
	val hoursLabel   = new RLabel
	val minutesLabel = new RLabel
	
	val speedSlider    = new Slider()
	val accuracySlider = new Slider()
	val cameraCheckbox = new CheckBox("Free camera")
	
	
	
	contents += new FlowPanel() {
		contents += pauseButton
		contents += resetButton
	}
	
	contents += new FlowPanel { contents += new Label("Simulated time") }
	contents += new GridPanel(4, 2) {
		hGap = 10
		contents += yearsLabel
		contents += new LLabel("years")
		contents += daysLabel
		contents += new LLabel("days")
		contents += hoursLabel
		contents += new LLabel("hours")
		contents += minutesLabel
		contents += new LLabel("minutes")
	}
	
	contents += Swing.VStrut(20)
	
	contents += new GridPanel(2, 2) {
		contents += new Label("Speed")
		contents += speedSlider
		contents += new Label("Accuracy")
		contents += accuracySlider
	}
	
	contents += new FlowPanel { contents += cameraCheckbox }
	
	
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
		val t = simulation.simulatedTime.toLong
		yearsLabel.text   = (t / 60 / 60 / 24 / 365).toString
		daysLabel.text    = (t / 60 / 60 / 24 % 365).toString
		hoursLabel.text   = (t / 60 / 60 % 24).toString
		minutesLabel.text = (t / 60 % 60).toString
	}
	
}