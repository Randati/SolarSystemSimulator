package util

import simulation.Object
import scala.io.Source

object SolarSystemReader {
	def loadFile(filename: String): Vector[Object] = {
		val file = Source.fromFile(filename)
		file.getLines.flatMap(readLine(_)).toVector
	}
	
	def readLine(line: String): Option[Object] = {
		val parts = line.split("#", 2).head.split("\\s+").filter(_.nonEmpty)
		
		if (parts.length != 9 && parts.length != 10)
			return None
		
		try {
			val name     = parts(0)
			val mass     = parts(1).toDouble
			val radius   = parts(2).toDouble * 1000
			val position = Vec(parts(3).toDouble * 1000, parts(4).toDouble * 1000, parts(5).toDouble * 1000)
			val velocity = Vec(parts(6).toDouble * 1000, parts(7).toDouble * 1000, parts(8).toDouble * 1000)
			val color    = if (parts.length == 10) Integer.parseInt(parts(9), 16) else 0xFF00FF
		
			Some(Object(
				name,
				mass,
				radius,
				position,
				velocity,
				color))
		}
		catch {
			case _: java.lang.NumberFormatException => None
		}
	}

}