package simulation

import scala.collection.mutable.ListBuffer

class Simulation {
	private var objects = new ListBuffer[Object]()
	
	
	def addObject(obj: Object) = {
		objects += obj
	}
	
	def getObjects() = objects.toVector
}