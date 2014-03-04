package util

case class Vec(
	val x: Double = 0,
	val y: Double = 0,
	val z: Double = 0) {
	
	def +(that: Vec): Vec = {
		new Vec(
			this.x + that.x,
			this.y + that.y,
			this.z + that.z)
	}
	
	def -(that: Vec): Vec = {
		new Vec(
			this.x - that.x,
			this.y - that.y,
			this.z - that.z)
	}
	
	def *(that: Double): Vec = {
		new Vec(
			this.x * that,
			this.y * that,
			this.z * that)
	}
	
	def /(that: Double): Vec = {
		new Vec(
			this.x / that,
			this.y / that,
			this.z / that)
	}
	
	def distancePow2(that: Vec): Double = {
		Math.pow(that.x - this.x, 2) +
		Math.pow(that.y - this.y, 2) +
		Math.pow(that.z - this.z, 2)
	}
	
	def distance(that: Vec): Double = {
		Math.sqrt(distancePow2(that))
	}
}