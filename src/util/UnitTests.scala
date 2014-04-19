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
		val expected = Object("Foo", 1E+20, 1000, Vec(1.0, 2.0, 3.0), Vec(4.0, 5.0, 6.0))
		
		assertEquals(Some(expected), SolarSystemReader.readLine(correctLine))
		assertEquals(Some(expected), SolarSystemReader.readLine(correctLine + " # Comment"))
		assertEquals(None,           SolarSystemReader.readLine("#" + correctLine))
	}

}