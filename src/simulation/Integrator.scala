package simulation

trait Integrator {
	protected def stateIntegrator[S <: State[S]](t: Double, state: S, dt: Double): (Double, S)
	
	def nextState[S <: State[S]](t: Double, state: S, dt: Double, rounds: Int = 4): (Double, S) = {
		val sectionSize = 1.0 / rounds
		
		// Run integrator X times
		(0 until rounds).foldLeft((t, state)) { case ((t, state), _) =>
			stateIntegrator(t, state, dt * sectionSize)
		}
	}
}

final object EulerIntegrator extends Integrator {
	protected def stateIntegrator[S <: State[S]](t: Double, state: S, dt: Double): (Double, S) = {
		val newT = t + dt
		val newState = state + state.calcDelta(dt, t, state) * dt
		(newT, newState)
	}
}

final object RK4Integrator extends Integrator {
	protected def stateIntegrator[S <: State[S]](t: Double, state: S, dt: Double): (Double, S) = {
		val a = state.calcDelta(dt, t + dt * 0.0, state          ) * dt
		val b = state.calcDelta(dt, t + dt * 0.5, state + a * 0.5) * dt
		val c = state.calcDelta(dt, t + dt * 0.5, state + b * 0.5) * dt
		val d = state.calcDelta(dt, t + dt * 1.0, state + c * 1.0) * dt

		val newT = t + dt
		val newState = state + (a + (b + c) * 2.0 + d) * (1.0 / 6.0)

		(newT, newState)
	}
}

trait State[S] {
	def +(s: S): S
	def *(x: Double): S

	def calcDelta(dt: Double, t: Double, state: S): S
}

