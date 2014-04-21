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
	private var offsetX: Double = -size.width / 2
	private var offsetY: Double = -size.height / 2
	private var zoom: Double = 1
	private var buffer = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB)
	private var dragLast: Option[java.awt.Point] = None
	private var angle = 0.0
	private val trails = new RingBuffer[(Vec, java.awt.Color)](300 * 11)
	private var rotationMatrix = Matrix(Vector.tabulate(3, 3) { (y, x) =>
		if (x == y) 1.0 else 0.0
	})
	
	ignoreRepaint = true
	
	listenTo(mouse.clicks)
	listenTo(mouse.moves)
	listenTo(mouse.wheel)
	
	reactions += {
    	case e: MousePressed =>
    		dragLast = Some(e.point)
    		
    	case e: MouseDragged =>
    		if (dragLast.isDefined) {
    			val last = dragLast.get
    			val delta = (e.point.x - last.x, e.point.y - last.y)
    			dragLast = Some(e.point)
    			
    			rotationMatrix *= rotMatZ(-delta._1 / 100.0)
    			rotationMatrix *= rotMatX(-delta._2 / 100.0)
    		}
    		
    	case e: MouseWheelMoved =>
    		zoom = max(zoom + e.rotation * 0.1, 0.0001)
	}
	
	def rotMatX(a: Double) = Matrix(Vector(
		Vector(1.0,    0.0,     0.0),
		Vector(0.0, cos(a), -sin(a)),
		Vector(0.0, sin(a),  cos(a))))
	
	def rotMatY(a: Double) = Matrix(Vector(
		Vector( cos(a), 0.0, sin(a)),
		Vector(    0.0, 1.0,    0.0),
		Vector(-sin(a), 0.0, cos(a))))
		
	def rotMatZ(a: Double) = Matrix(Vector(
		Vector(cos(a), -sin(a), 0.0),
		Vector(sin(a),  cos(a), 0.0),
		Vector(   0.0,     0.0, 1.0)))
	

	
	override def paint(screenG: Graphics2D) {
		if (buffer.getWidth != size.width || buffer.getHeight != size.height) {
			buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)
		}
		
		
		val g = buffer.getGraphics.asInstanceOf[Graphics2D]
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
		
		g.setColor(new Color(0, 0, 0))
		g.fillRect(0, 0, size.width, size.height)

		g.setColor(new Color(255, 255, 255))

		offsetX = size.width / 2
		offsetY = size.height / 2
		
		val objs = simulation.getObjects
		val rMin = objs.minBy(_.radius).radius
		val center = Vec()
		
		for (p <- trails.getData) {
			drawPoint(p._1, 1, p._2)
		}
		
		for (obj <- objs) {
			val col = obj.name match {
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
			
			trails.push((obj.position, col))
			drawPoint(obj.position, obj.radius, col)
		}
		
		
		def drawPoint(pos: Vec, radius: Double, color: Color) = {
			val drawPos = rotationMatrix * pos
			
			val rZoom = 3.5 / zoom
			val posZoom = 1e9 * zoom
			val drawRadius = math.max((math.log(radius) - math.log(rMin)) * rZoom, 0.5) 
			val x = (drawPos.x - center.x) / posZoom + offsetX - drawRadius
			val y = (drawPos.y - center.y) / posZoom + offsetY - drawRadius
			
			g.setColor(color)
			g.fillOval(x.round.toInt, y.round.toInt, (drawRadius * 2).round.toInt, (drawRadius * 2).round.toInt)
		}
		
		
		screenG.drawImage(buffer, 0, 0, null)
	}
}