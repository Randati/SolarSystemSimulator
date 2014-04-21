package util
import scala.math.{sin, cos}

case class Matrix(data: Vector[Vector[Double]]) {
	val width = data(0).size
	val height = data.size
	
	def apply(x: Int, y: Int): Double = data(y)(x)
		
	def +(that: Matrix): Matrix = {
		require(this.width == that.width)
		require(this.height == that.height)
		
		Matrix(Vector.tabulate(height, width) { (y, x) =>
			this(x, y) + that(x, y)
		})
	} 
	
	def -(that: Matrix): Matrix = {
		require(this.width == that.width)
		require(this.height == that.height)
		
		Matrix(Vector.tabulate(height, width) { (y, x) =>
			this(x, y) - that(x, y)
		})
	}
	
	
	def *(that: Matrix): Matrix = {
		require(this.width == that.height)
		
		val width = that.width
		val height = this.height
		
		Matrix(Vector.tabulate(height, width) { (y, x) =>
			var i = 0
			var result = 0.0
			while (i < this.width) {
				result += this(i, y) * that(x, i)
				i += 1
			}
			result
		})
	}
	
	def *(that: Vec): Vec = {
		require(this.width == 3)
		require(this.height == 3)
		
		Vec(
			this(0, 0) * that.x + this(1, 0) * that.y + this(2, 0) * that.z,
			this(0, 1) * that.x + this(1, 1) * that.y + this(2, 1) * that.z,
			this(0, 2) * that.x + this(1, 2) * that.y + this(2, 2) * that.z)
	}
}


object Matrix {
	def rotationX(a: Double) = Matrix(Vector(
		Vector(1.0,    0.0,     0.0),
		Vector(0.0, cos(a), -sin(a)),
		Vector(0.0, sin(a),  cos(a))))
	
	def rotationY(a: Double) = Matrix(Vector(
		Vector( cos(a), 0.0, sin(a)),
		Vector(    0.0, 1.0,    0.0),
		Vector(-sin(a), 0.0, cos(a))))
		
	def rotationZ(a: Double) = Matrix(Vector(
		Vector(cos(a), -sin(a), 0.0),
		Vector(sin(a),  cos(a), 0.0),
		Vector(   0.0,     0.0, 1.0)))
}
