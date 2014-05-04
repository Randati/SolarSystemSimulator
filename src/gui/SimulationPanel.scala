package gui

import scala.math._
import swing._
import swing.event._
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import simulation.Simulation
import util.Matrix
import util.Vec
import util.RingBuffer

class SimulationPanel extends Panel {
	val trails = new RingBuffer[(Vec, java.awt.Color)](300 * 11)
	var freeCamera = false
	var centerObject = -1
	var objectSize = 1.0
	var objectScale = 1.0
	
	private var offsetX: Double = -size.width / 2
	private var offsetY: Double = -size.height / 2
	private var zoom: Double = 1
	private var buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB)
	private var dragLast: Option[java.awt.Point] = None
	private var angleX = 0.0
	private var angleY = Pi / 2
	private var rotationMatrix = Matrix.rotationX(angleY) * Matrix.rotationZ(angleX)
	
	ignoreRepaint = true
	
	listenTo(mouse.clicks)
	listenTo(mouse.moves)
	listenTo(mouse.wheel)
	
	reactions += {
		case e: MousePressed =>
			dragLast = Some(e.point)
			
		case e: MouseDragged =>
			for (last <- dragLast) {
				updateCamera(e.point.x - last.x, e.point.y - last.y)
				dragLast = Some(e.point)
			}
			
		case e: MouseWheelMoved =>
			zoom = max(zoom + e.rotation * 0.06 * zoom, 0.0000000001)
	}
	
	def updateCamera(deltaX: Int = 0, deltaY: Int = 0) = {
		if (freeCamera) {
			rotationMatrix =
				Matrix.rotationX(-deltaY / 100.0) *
				Matrix.rotationY( deltaX / 100.0) *
				rotationMatrix
		}
		else {
			angleX -= deltaX / 100.0
			angleY = max(min(angleY - deltaY / 100.0, Pi), 0.0)
			
			rotationMatrix = Matrix.rotationX(angleY) * Matrix.rotationZ(angleX)
		}
	}
	
	
	case class Drawable(pos: Vec, radius: Double, color: java.awt.Color)
	
	override def paint(screenG: Graphics2D) {
		if (buffer.getWidth != size.width || buffer.getHeight != size.height)
			buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
		
		
		val g = buffer.getGraphics.asInstanceOf[Graphics2D]
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		
		
		// Draw background
		g.setColor(new Color(0, 0, 0))
		g.fillRect(0, 0, size.width, size.height)


		
		offsetX = size.width / 2
		offsetY = size.height / 2
		
		val objs = GUI.simulation.getObjects
		val rMin = if(objs.nonEmpty) objs.minBy(_.radius).radius else 0
		val rMax = if(objs.nonEmpty) objs.maxBy(_.radius).radius else 1
		val posZoom = 1e9 * zoom
		
		val center =
			if (centerObject >= 0 && centerObject < objs.length)
				rotationMatrix * objs(centerObject).position
			else
				Vec()
		
		
		def screenCoordinates(pos: Vec, offset: Double = 0): Vec = {
			val p = rotationMatrix * pos
			Vec(
				(p.x - center.x) / posZoom + offsetX - offset,
				(p.y - center.y) / posZoom + offsetY - offset,
				p.z)
		}
		
		// Draw grid
		val gridWidth = 1e12
		val gridPos   = -1e11
		val gridCells = 20
		for (row <- 0 to gridCells) {
			val v11 = row.toDouble / gridCells * 2 - 1
			val p1 = screenCoordinates(Vec(-gridWidth, gridWidth * v11, gridPos))
			val p2 = screenCoordinates(Vec( gridWidth, gridWidth * v11, gridPos))
			
			g.setColor(new Color(255, 255, 255, 40))
			g.drawLine(p1.x.toInt, p1.y.toInt, p2.x.toInt, p2.y.toInt)
		}
		for (row <- 0 to gridCells) {
			val v11 = row.toDouble / gridCells * 2 - 1
			val p1 = screenCoordinates(Vec(gridWidth * v11, -gridWidth, gridPos))
			val p2 = screenCoordinates(Vec(gridWidth * v11,  gridWidth, gridPos))
			
			g.setColor(new Color(255, 255, 255, 40))
			g.drawLine(p1.x.toInt, p1.y.toInt, p2.x.toInt, p2.y.toInt)
		}

		
		val selectedObject = if (GUI.selectedObject < objs.length) objs(GUI.selectedObject) else null
		var selectedDrawable: Option[Drawable] = None
		
		// Transform objects and trails to drawable circles
		// and sort them from farthest to closest
		val drawObjects =
			(objs.map { obj =>
				val r01        = (obj.radius - rMin) / (rMax - rMin)
				val r          = math.pow(r01, objectScale) * objectSize * (rMax - rMin) + rMin
				val drawRadius = math.max(r / posZoom, 0.5)
				
				val drawPos    = screenCoordinates(obj.position, drawRadius)
				
				val drawable = Drawable(drawPos, drawRadius, new Color(obj.color))
				
				if (obj == selectedObject)
					selectedDrawable = Some(drawable)
				
				drawable
			}
			++
			trails.getData.map { p =>
				val r = 0.5
				val pos = screenCoordinates(p._1, r)
				Drawable(pos, r, p._2)
			})
			.sortBy(_.pos.z)
		
		
		// Draw objects and trails
		for (d <- drawObjects) {
			g.setColor(d.color)
			g.fillOval(d.pos.x.toInt, d.pos.y.toInt, (d.radius * 2).toInt, (d.radius * 2).toInt)
		}
		
		
		// Draw velocity and acceleration vectors
		for (obj <- objs) {
			val origin = screenCoordinates(obj.position)
			val velVec = screenCoordinates(obj.position + obj.velocity * 10e5)
			val accVec = screenCoordinates(obj.position + obj.acceleration * 10e11)
			
			g.setColor(new Color(0x00FF00))
			g.drawLine(origin.x.toInt, origin.y.toInt, velVec.x.toInt, velVec.y.toInt)
			
			g.setColor(new Color(0x00FFFF))
			g.drawLine(origin.x.toInt, origin.y.toInt, accVec.x.toInt, accVec.y.toInt)
		}
		
		
		// Draw selection circle
		selectedDrawable.foreach { d =>
			val e = 5
			val r = d.radius + e
			val x = d.pos.x - e
			val y = d.pos.y - e
			g.setColor(new Color(255, 255, 255))
			g.drawOval(x.toInt, y.toInt, (r * 2).toInt, (r * 2).toInt)
		}
		

		screenG.drawImage(buffer, 0, 0, null)
	}
}