package simulation

import util.Vec


case class Object(
	val name: String,
	val mass: Double,
	val radius: Double,
	val position: Vec,
	val velocity: Vec,
	val color: Int = 0xFFFFFF,
	val acceleration: Vec = Vec()) {
	
	def +(o: Object) = copy(position = position + o.position, velocity = velocity + o.velocity)
	def *(x: Double) = copy(position = position * x,          velocity = velocity * x)
}
