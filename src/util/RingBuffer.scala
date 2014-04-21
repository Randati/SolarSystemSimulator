package util
import scala.reflect.ClassTag

class RingBuffer[T : ClassTag](size: Int) {
	private val data = Array.ofDim[T](size)
	private var pointer = 0
	private var full = false
	
	def push(x: T) = {
		data(pointer) = x
		
		pointer += 1
		if (pointer == data.size) {
			full = true
			pointer = 0
		}
	}
	
	def getData: Array[T] = {
		if (full)
			data
		else
			data.take(pointer)
	}
}