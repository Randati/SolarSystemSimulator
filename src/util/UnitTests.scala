package util

import org.junit.Test
import org.junit.Assert._
import simulation.Object

class UnitTests {

	@Test def testReadingLines() {
		assertEquals(None, SolarSystemReader.readLine(""))
		assertEquals(None, SolarSystemReader.readLine("#"))
		assertEquals(None, SolarSystemReader.readLine("foo   bar # baz"))
		assertEquals(None, SolarSystemReader.readLine("foo"))
		assertEquals(None, SolarSystemReader.readLine("foo bar baz  foo bar baz  foo bar baz"))
		
		val correctLine = " Foo \t  1E+20 1000  1.0 2.0 3.0  4.0 5.0 6.0 "
		val expected = Object("Foo", 1E+20, 1000*1000.0, Vec(1.0*1000.0, 2.0*1000.0, 3.0*1000.0), Vec(4.0*1000.0, 5.0*1000.0, 6.0*1000.0))
		
		assertEquals(Some(expected), SolarSystemReader.readLine(correctLine))
		assertEquals(Some(expected), SolarSystemReader.readLine(correctLine + " # Comment"))
		assertEquals(None,           SolarSystemReader.readLine("#" + correctLine))
	}
	
	
	@Test def testMatrixAddSub() {
		val a3x2 = Matrix(Vector(
			Vector(1.0, 2.0, 3.0),
			Vector(4.0, 5.0, 6.0)))
		
		val b3x2 = Matrix(Vector(
			Vector(7.0, 8.0, 9.0),
			Vector(10.0, 11.0, 12.0)))
		
		val r3x2 = Matrix(Vector(
			Vector(8.0, 10.0, 12.0),
			Vector(14.0, 16.0, 18.0)))
		
		assertEquals(r3x2, a3x2 + b3x2)
		assertEquals(b3x2, r3x2 - a3x2)
	}
	
	@Test def testMatrixMul() {
		val a3x2 = Matrix(Vector(
			Vector(2.0, 3.0, 4.0),
			Vector(1.0, 0.0, 0.0)))
		
		val b2x3 = Matrix(Vector(
			Vector(0.0, 1000.0),
			Vector(1.0,  100.0),
			Vector(0.0,   10.0)))
		
		val r2x2 = Matrix(Vector(
			Vector(3.0, 2340.0),
			Vector(0.0, 1000.0)))
		
		assertEquals(r2x2, a3x2 * b2x3)
		
		val a2x2 = Matrix(Vector(
			Vector(1.0, 2.0),
			Vector(3.0, 4.0)))
		
		val b2x2 = Matrix(Vector(
			Vector(0.0, 1.0),
			Vector(0.0, 0.0)))
		
		val ab = Matrix(Vector(
			Vector(0.0, 1.0),
			Vector(0.0, 3.0)))
			
		val ba = Matrix(Vector(
			Vector(3.0, 4.0),
			Vector(0.0, 0.0)))
		
		assertEquals(ab, a2x2 * b2x2)
		assertEquals(ba, b2x2 * a2x2)
	}
	
	
	@Test def testRingBuffer() {
		val ring = new RingBuffer[Int](3)
		assertEquals(Array().deep, ring.getData.deep)
		
		ring.push(1)
		assertEquals(Array(1).deep, ring.getData.deep)
		
		ring.push(2)
		assertEquals(Array(1, 2).deep, ring.getData.deep)
		
		ring.push(3)
		assertEquals(Array(1, 2, 3).deep, ring.getData.deep)
		
		ring.push(4)
		assertEquals(Array(4, 2, 3).deep, ring.getData.deep)
		
		ring.push(5)
		assertEquals(Array(4, 5, 3).deep, ring.getData.deep)
		
		ring.push(6)
		assertEquals(Array(4, 5, 6).deep, ring.getData.deep)
		
		ring.push(7)
		assertEquals(Array(7, 5, 6).deep, ring.getData.deep)
		
		
		ring.clear()
		assertEquals(Array().deep, ring.getData.deep)
		
		ring.push(1)
		assertEquals(Array(1).deep, ring.getData.deep)
		
		ring.push(2)
		assertEquals(Array(1, 2).deep, ring.getData.deep)
		
		ring.push(3)
		assertEquals(Array(1, 2, 3).deep, ring.getData.deep)
		
		ring.push(4)
		assertEquals(Array(4, 2, 3).deep, ring.getData.deep)
	}

}