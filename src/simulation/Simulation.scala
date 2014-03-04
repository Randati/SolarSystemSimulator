package simulation

import scala.collection.mutable.ListBuffer
import util.Vec


class Simulation {
	private var objects = new ListBuffer[Object]()
	private val G = 6.67259e-11 // (N*m^2) / (kg^2)
	
	
	def addObject(obj: Object) = {
		objects += obj
	}
	
	def getObjects() = objects.toVector
	
	def simulate(time: Double) = {
		
		for (obj <- objects) {
			var forceSum = new Vec
			
			for (obj2 <- objects) {
				if (obj != obj2) {
					val a = obj.position
					val b = obj2.position
					
					val force = (obj.mass * obj2.mass) / (a distancePow2 b)
					val dist = a distance b
					val unitVec = (b - a) / dist
					forceSum += unitVec * force
				}
			}
			
			val F = forceSum * G
			val a = F / obj.mass
			println(F, a)
			obj.velocity += a * time
		}
		
		for (obj <- objects) {
			obj.position += obj.velocity * time
		}
	}
	
}