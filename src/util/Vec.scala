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
	
	def length: Double = {
		Math.sqrt(
			this.x * this.x +
			this.y * this.y +
			this.z * this.z)
	}
	
	def normalize: Vec = {
		this / length
	}
	
	def distancePow2(that: Vec): Double = {
		val dx = that.x - this.x
		val dy = that.y - this.y
		val dz = that.z - this.z
		
		dx * dx + dy * dy + dz * dz
	}
	
	def distance(that: Vec): Double = {
		Math.sqrt(this distancePow2 that)
	}
}