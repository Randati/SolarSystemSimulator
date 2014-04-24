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

class SimulationPanel(val simulation: Simulation) extends Panel {
	val trails = new RingBuffer[(Vec, java.awt.Color)](300 * 11)
	
	private var offsetX: Double = -size.width / 2
	private var offsetY: Double = -size.height / 2
	private var zoom: Double = 1
	private var buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB)
	private var dragLast: Option[java.awt.Point] = None
	private var angleX = 0.0
	private var angleY = Pi / 2
	private var rotationMatrix = Matrix.rotationX(angleY) * Matrix.rotationZ(angleX)
	private var freeCamera = false
	
	ignoreRepaint = true
	
	listenTo(mouse.clicks)
	listenTo(mouse.moves)
	listenTo(mouse.wheel)
	
	reactions += {
		case e: MousePressed =>
			dragLast = Some(e.point)
			
		case e: MouseDragged =>
			for (last <- dragLast) {
				val deltaX = e.point.x - last.x
				val deltaY = e.point.y - last.y
				
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
					
				dragLast = Some(e.point)
			}
			
		case e: MouseWheelMoved =>
			zoom = max(zoom + e.rotation * 0.06 * zoom, 0.0001)
	}
	
	// TODO: Move colors to the settings file
	def planetColor(name: String) = name match {
		case "10-Sun"      => java.awt.Color.yellow
		case "199-Mercury" => new Color(200, 200, 200)
		case "299-Venus"   => new Color(255, 168, 18)
		case "399-Earth"   => new Color(94, 255, 77)
		case "499-Mars"    => new Color(255, 81, 13)
		case "599-Jupiter" => new Color(194, 209, 180)
		case "699-Saturn"  => new Color(229, 222, 138)
		case "799-Uranus"  => new Color(119, 217, 229)
		case "899-Neptune" => new Color(91, 130, 229)
		case "999-Pluto"   => new Color(186, 96, 69)
		case "301-Moon"    => new Color(255, 255, 255)
		case _             => new Color(255, 0, 255)
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
		
		val objs = simulation.getObjects
		val rMin = objs.minBy(_.radius).radius
		val rZoom = 3.5 / zoom
		val posZoom = 1e9 * zoom
		val center = Vec()
		
		
		// TODO: Add velocity and acceleration vectors
		
		// Transform objects and trails to drawable circles
		// and sort them from farthest to closest
		val drawObjects =
			(objs.map { obj =>
				val drawPos    = rotationMatrix * obj.position
				val drawRadius = math.max((math.log(obj.radius) - math.log(rMin)) * rZoom, 0.5)
				val color      = planetColor(obj.name)
				
				Drawable(drawPos, drawRadius, color)
			}
			++
			trails.getData.map { p =>
				Drawable(rotationMatrix * p._1, 0.5, p._2)
			})
			.sortBy(_.pos.z)
		
		
		// Draw objects and trails
		for (d <- drawObjects) {
			val x = (d.pos.x - center.x) / posZoom + offsetX - d.radius
			val y = (d.pos.y - center.y) / posZoom + offsetY - d.radius
			
			g.setColor(d.color)
			g.fillOval(x.round.toInt, y.round.toInt, (d.radius * 2).round.toInt, (d.radius * 2).round.toInt)
		}
		

		screenG.drawImage(buffer, 0, 0, null)
	}
}