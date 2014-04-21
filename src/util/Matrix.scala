package util

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