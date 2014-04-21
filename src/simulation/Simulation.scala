package simulation

import scala.collection.mutable.ListBuffer
import util.Vec

// TODO: Test lagrangian points

class Simulation {
	private var time  = 0.0
	private var state = SystemState(Vector())
	private var objectsToAdd = List[Object]()
	
	def simulatedTime = time
	
	def addObject(obj: Object) = {
		objectsToAdd ::= obj 
	}
	
	def getObjects() = state.objects
	
	def simulate(dt: Double) = {
		state = SystemState(state.objects ++ objectsToAdd)
		objectsToAdd = Nil
		
		val (newT, newState) = RK4Integrator.nextState(time, state, dt)
		time = newT
		state = newState
	}
}


private case class SystemState(objects: Vector[Object]) extends State[SystemState] {
	private val G = 6.67259e-11 // (N*m^2) / (kg^2)
	
	def +(s: SystemState) = SystemState(objects.zip(s.objects).map(p => p._1 + p._2))
	def *(x: Double)      = SystemState(objects.map(_ * x))
	
	def calcDelta(dt: Double, t: Double, state: SystemState): SystemState = {
		SystemState(this.objects.map { obj =>
			val a = acceleration(obj, this.objects)
			obj.copy(
				velocity = a,
				position = obj.velocity + a * dt)
		})
	}
	
	private def acceleration(obj: Object, objects: Vector[Object]): Vec = {
		val forceSum = objects.foldLeft(new Vec) { (forceSum, obj2) =>
			if (obj == obj2)
				forceSum
			else {
				val a = obj.position
				val b = obj2.position
				
				val force = (obj.mass * obj2.mass) / (a distancePow2 b)
				val unitVec = (b - a).normalize
				forceSum + unitVec * force
			}
		}
		
		val F = forceSum * G
		val a = F / obj.mass
		a
	}

}