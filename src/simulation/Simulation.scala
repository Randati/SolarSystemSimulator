package simulation

import scala.collection.mutable.ListBuffer
import util.Vec

// TODO: Test lagrangian points

class Simulation(objects: Vector[Object]) {
	private var time  = 0.0
	private var state = SystemState(objects)

	def simulatedTime = time
	
	def getObjects() = state.objects
	
	def simulate(dt: Double): Boolean = {
		val (newT, newState) = RK4Integrator.nextState(time, state, dt)
		time = newT
		state = newState
		
		for (o1 <- state.objects; o2 <- state.objects if o1 != o2) {
			if (ballBallCollision(o1.position, o1.radius, o2.position, o2.radius))
				return true
		}
		
		false
	}
	
	private def ballBallCollision(aPos: Vec, aR: Double, bPos: Vec, bR: Double): Boolean = {
		// Same as:
		// (aPos distance bPos) < (aR + bR)
		val r2 = aR + bR
		(aPos distancePow2 bPos) < r2 * r2
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