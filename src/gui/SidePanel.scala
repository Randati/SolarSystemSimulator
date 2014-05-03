package gui

import swing._
import swing.event._
import javax.swing.border._
import javax.swing.JComboBox
import javax.swing.ComboBoxModel
import simulation.Simulation

class SidePanel(val simPanel: SimulationPanel)
		extends BoxPanel(Orientation.Vertical) {
	preferredSize = new Dimension(250, 0)
	
	private class RLabel(s: String = "") extends Label(s) { horizontalAlignment = Alignment.Right }
	private class LLabel(s: String = "") extends Label(s) { horizontalAlignment = Alignment.Left }
	
	private val loadButton  = new Button("Load")
	private val clearButton = new Button("Clear")
	
	private val pauseButton = new Button("Pause")
	private val resetButton = new Button("Reset")
	
	private val yearsLabel   = new RLabel
	private val daysLabel    = new RLabel
	private val hoursLabel   = new RLabel
	private val minutesLabel = new RLabel
	
	private val speedSlider    = new Slider { min = 0; max = 1000 }
	private val accuracySlider = new Slider { min = 1; max = 1000 }
	private val cameraCheckbox = new CheckBox("Free camera")
	
	private val objectList = new ComboBox(Array[String]())
	
	private val massField   = new TextField
	private val radiusField = new TextField
	private val posFields   = Vector(new TextField, new TextField, new TextField)
	private val velFields   = Vector(new TextField, new TextField, new TextField)
	
	
	
	contents += new FlowPanel() {
		contents += loadButton
		contents += clearButton
	}
	
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
		
		contents += objectList
		
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new Label("Mass")
			contents += massField
		}
		
		contents += new BoxPanel(Orientation.Horizontal) {
			contents += new Label("Radius")
			contents += radiusField
		}
		
		contents += new Label("Position")
		contents += new BoxPanel(Orientation.Horizontal) {
			posFields.foreach { contents += _ }
		}
		
		contents += new Label("Velocity")
		contents += new BoxPanel(Orientation.Horizontal) {
			velFields.foreach { contents += _ }
		}
		
		contents += new Separator(Orientation.Vertical)
	}
	
	
	
	listenTo(
		loadButton, clearButton,
		pauseButton, resetButton,
		speedSlider, accuracySlider, cameraCheckbox,
		objectList.selection)
		
	
	reactions += {
		case ButtonClicked(`loadButton`) =>
			val chooser = new FileChooser(new java.io.File("./"))
			if (chooser.showOpenDialog(this) == FileChooser.Result.Approve) {
				GUI.loadFile(chooser.selectedFile.getAbsolutePath)
			}
		
		case ButtonClicked(`clearButton`) =>
			GUI.clearObjects()
				
		case ButtonClicked(`pauseButton`) =>
			GUI.simulationPaused = !GUI.simulationPaused
			pauseButton.text = if (GUI.simulationPaused) "Run" else "Pause"
		
		case ButtonClicked(`resetButton`) =>
			GUI.resetSimulation()
			
		case ValueChanged(`speedSlider`) =>
			val v01 = speedSlider.value.toDouble / 1000
			val v = v01 * v01 * v01 * v01 * v01 * v01
			GUI.simSecPerSec = v * 60 * 60 * 24 * 365 * 100
		
		case ValueChanged(`accuracySlider`) =>
			val v01 = accuracySlider.value.toDouble / 1000
			val v = v01 * v01 * v01
			GUI.ticksPerSec = v * 10000
		
		case ButtonClicked(`cameraCheckbox`) =>
			simPanel.freeCamera = cameraCheckbox.selected
			simPanel.updateCamera()
		
		case SelectionChanged(`objectList`) =>
			GUI.selectedObject = objectList.selection.index
	}
	
	
	def update() = {
		// Simulation time
		val t = GUI.simulation.simulatedTime.toLong
		yearsLabel.text   = (t / 60 / 60 / 24 / 365).toString
		daysLabel.text    = (t / 60 / 60 / 24 % 365).toString
		hoursLabel.text   = (t / 60 / 60 % 24).toString
		minutesLabel.text = (t / 60 % 60).toString
		
		
		// Selected object
		val objects = GUI.simulation.getObjects
		val selected = GUI.selectedObject
		
		if (objects.length > selected) {
			val obj = objects(selected)
			
			massField.text    = obj.mass.toString
			radiusField.text  = (obj.radius/ 1000).toString
			posFields(0).text = (obj.position.x / 1000).toString
			posFields(1).text = (obj.position.y / 1000).toString
			posFields(2).text = (obj.position.z / 1000).toString
			velFields(0).text = (obj.velocity.x / 1000).toString
			velFields(1).text = (obj.velocity.y / 1000).toString
			velFields(2).text = (obj.velocity.z / 1000).toString
		}
	}
	
	def updateObjectList() = {
		val objects = GUI.simulation.getObjects
		val combobox = objectList.peer.asInstanceOf[JComboBox[String]]
		val listModel = ComboBox.newConstantModel(objects.map(_.name)).asInstanceOf[ComboBoxModel[String]]
		combobox.setModel(listModel)
	}
	
}
