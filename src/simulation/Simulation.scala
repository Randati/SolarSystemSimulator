package simulation

import scala.collection.mutable.ListBuffer
import util.Vec


class Simulation {
	var simulatedTime: Double = 0.0
	
	private var objects = new ListBuffer[Object]()
	private val G = 6.67259e-11 // (N*m^2) / (kg^2)
	
	case class State(val objects: Vector[Object])
	case class StateDeriv(val objects: Vector[ObjectDeriv])
	
	
	def addObject(obj: Object) = {
		objects += obj
	}
	
	def getObjects() = objects.toVector
	
	private def acceleration(obj: Object, state: State, time: Double): Vec = {
		var forceSum = new Vec
			
		for (obj2 <- state.objects) {
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
		a
	}
	
		
	private def evaluate(state: State, t: Double, dt: Double, deriv: StateDeriv): (State, StateDeriv) = {
		simulatedTime = t
		
		val newState = Vector.tabulate(state.objects.length)(i => state.objects(i).copy())
		val newDeriv = Vector.tabulate(state.objects.length)(_ => new ObjectDeriv)
		
		var i = 0
		for (obj <- state.objects) {
			newState(i).position = obj.position + deriv.objects(i).position * dt
			newState(i).velocity = obj.velocity + deriv.objects(i).velocity * dt
			
			newDeriv(i).position = newState(i).velocity
			newDeriv(i).velocity = acceleration(obj, state, t + dt)
			
			i += 1
		}
		
		(State(newState), StateDeriv(newDeriv))
	}
	
	
	def simulate(t: Double, dt: Double) = {
		val origState = State(objects.toVector)
		val derivState = StateDeriv(Vector.fill(origState.objects.length)(new ObjectDeriv))
		
		val (sa, a) = evaluate(origState, t, dt * 0.0, derivState)
		val (sb, b) = evaluate(origState, t, dt * 0.5, a)
		val (sc, c) = evaluate(origState, t, dt * 0.5, b)
		val (sd, d) = evaluate(origState, t, dt * 1.0, c)
		
		
		var i = 0
		for (obj <- objects) {
			val dxdt = (a.objects(i).position + (b.objects(i).position + c.objects(i).position) * 2.0 + d.objects(i).position) * (1.0 / 6.0)
			val dvdt = (a.objects(i).velocity + (b.objects(i).velocity + c.objects(i).velocity) * 2.0 + d.objects(i).velocity) * (1.0 / 6.0)
			
			obj.position += dxdt * dt
			obj.velocity += dvdt * dt
			
			i += 1
		}
	}
	
	
	
	
	
	def simulate(time: Double) = {
		simulatedTime += time
		
		for (obj <- objects) {
			obj.velocity += acceleration(obj, State(getObjects), time) * time
		}
		
		for (obj <- objects) {
			obj.position += obj.velocity * time
		}
	}
	
	
}