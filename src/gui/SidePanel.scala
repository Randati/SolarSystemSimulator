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
	private class TField extends TextField { minimumSize = new Dimension(1000, preferredSize.height + 4); editable = false }
	
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
	
	
	private val cameraCheckbox = new CheckBox("Free camera rotation")
	private val centerList     = new ComboBox(Array[String]("None"))
	private val sizeSlider     = new Slider { min = 1; max = 1000 }
	private val scaleSlider    = new Slider { min = 1; max = 1000 }
	private val trailSlider    = new Slider { min = 1; max = 1000 }
	private val vectorSlider   = new Slider { min = 1; max = 1000 }
	
	private val objectList = new ComboBox(Array[String]())
	private val massField   = new TField
	private val radiusField = new TField
	private val posFields   = Vector(new TField, new TField, new TField)
	private val velFields   = Vector(new TField, new TField, new TField)
	
	
	
	// Simulation
	contents += new BoxPanel(Orientation.Vertical) {
		border = new CompoundBorder() {
			outsideBorder = new TitledBorder("Simulation")
			insideBorder = new EmptyBorder(8, 8, 8, 8)
		}
		
		
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
	}
	contents += Swing.VStrut(20)
	
	
	// Apperance
	contents += new BoxPanel(Orientation.Vertical) {
		border = new CompoundBorder() {
			outsideBorder = new TitledBorder("Appearance")
			insideBorder = new EmptyBorder(8, 8, 8, 8)
		}
		
		contents += new FlowPanel { contents += cameraCheckbox }
		
		contents += new FlowPanel { contents += new Label("Camera center") }
		contents += centerList
		contents += Swing.VStrut(10)
		
		contents += new GridPanel(4, 2) {
			contents += new Label("Object size")
			contents += sizeSlider
			contents += new Label("Object scale")
			contents += scaleSlider
			contents += new Label("Trail length")
			contents += trailSlider
			contents += new Label("Vector length")
			contents += vectorSlider
		}
	}
	contents += Swing.VStrut(20)
	
	
	// Object
	contents += new BoxPanel(Orientation.Vertical) {
		border = new CompoundBorder() {
			outsideBorder = new TitledBorder("Object")
			insideBorder = new EmptyBorder(8, 8, 8, 8)
		}
		
		contents += objectList
		contents += Swing.VStrut(20)
		
		contents += new FlowPanel { contents += new Label("Mass, kg") }
		contents += massField
		contents += Swing.VStrut(10)
		
		contents += new FlowPanel { contents += new Label("Radius, km") }
		contents += radiusField
		contents += Swing.VStrut(20)
		
		contents += new FlowPanel { contents += new Label("Position, km") }
		contents += new BoxPanel(Orientation.Horizontal) { contents += new Label("X"); contents += posFields(0) }
		contents += new BoxPanel(Orientation.Horizontal) { contents += new Label("Y"); contents += posFields(1) }
		contents += new BoxPanel(Orientation.Horizontal) { contents += new Label("Z"); contents += posFields(2) }
		contents += Swing.VStrut(20)
		
		contents += new FlowPanel { contents += new Label("Velocity, km/s") }
		contents += new BoxPanel(Orientation.Horizontal) { contents += new Label("X"); contents += velFields(0) }
		contents += new BoxPanel(Orientation.Horizontal) { contents += new Label("Y"); contents += velFields(1) }
		contents += new BoxPanel(Orientation.Horizontal) { contents += new Label("Z"); contents += velFields(2) }
	}
	contents += Swing.VStrut(10000)
	
	
	
	
	
	listenTo(
		loadButton, clearButton,
		pauseButton, resetButton,
		speedSlider, accuracySlider,
		
		cameraCheckbox, centerList.selection,
		sizeSlider, scaleSlider, trailSlider, vectorSlider,
		
		objectList.selection)
		
	
	reactions += {
		case ButtonClicked(`loadButton`) =>
			val chooser = new FileChooser(new java.io.File("./"))
			if (chooser.showOpenDialog(this) == FileChooser.Result.Approve) {
				GUI.loadFile(chooser.selectedFile.getAbsolutePath)
			}
		
		case ButtonClicked(`clearButton`) =>
			GUI.simulationPaused = true
			GUI.clearObjects()
			GUI.resetSimulation()
			update()
			setPauseButtonState()
				
		case ButtonClicked(`pauseButton`) =>
			GUI.simulationPaused = !GUI.simulationPaused
			setPauseButtonState()
		
		case ButtonClicked(`resetButton`) =>
			GUI.resetSimulation()
			update()
			
		case ValueChanged(`speedSlider`) =>
			val v01 = speedSlider.value.toDouble / 1000
			val v = v01 * v01 * v01 * v01 * v01 * v01
			GUI.simSecPerSec = v * 60 * 60 * 24 * 365 * 100
		
		case ValueChanged(`accuracySlider`) =>
			val v01 = accuracySlider.value.toDouble / 1000
			val v = v01 * v01 * v01
			GUI.ticksPerSec = v * 10000
			println(GUI.ticksPerSec)
			
		
		case ButtonClicked(`cameraCheckbox`) =>
			simPanel.freeCamera = cameraCheckbox.selected
			simPanel.updateCamera()
		
		case SelectionChanged(`centerList`) =>
			simPanel.centerObject = centerList.selection.index - 1
		
		case ValueChanged(`sizeSlider`) =>
			simPanel.objectSize = sizeSlider.value / 1000.0 * 100
		
		case ValueChanged(`scaleSlider`) =>
			simPanel.objectScale = 1.0 - scaleSlider.value / 1000.0
		
		case ValueChanged(`trailSlider`) =>
			simPanel.trailLength((trailSlider.value / 1000.0 * 2000).toInt)
			
		case ValueChanged(`vectorSlider`) =>
			simPanel.vectorLength = vectorSlider.value / 1000.0 * 10
			
			
		case SelectionChanged(`objectList`) =>
			GUI.selectedObject = objectList.selection.index
			update()
	}
	
	speedSlider.value = 400
	accuracySlider.value = 300
	sizeSlider.value = 1
	scaleSlider.value = 1
	trailSlider.value = 100
	vectorSlider.value = 10
	
	
	
	
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
		
		val centerCombobox  = centerList.peer.asInstanceOf[JComboBox[String]]
		val objectsCombobox = objectList.peer.asInstanceOf[JComboBox[String]]
		
		val centerModel  = ComboBox.newConstantModel("None" +: objects.map(_.name)).asInstanceOf[ComboBoxModel[String]]
		val objectsModel = ComboBox.newConstantModel(objects.map(_.name)).asInstanceOf[ComboBoxModel[String]]
		
		centerCombobox.setModel(centerModel)
		objectsCombobox.setModel(objectsModel)
		
		centerList.selection.index = 0
		if (objects.nonEmpty)
			objectList.selection.index = 0
		
		trailSlider.value = trailSlider.value
	}
	
	def setPauseButtonState() = {
		pauseButton.text = if (GUI.simulationPaused) "Run" else "Pause"
		pauseButton.enabled = !GUI.hasCrashed
	}
	
}
