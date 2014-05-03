package cli

import collection.mutable.Queue
import simulation.Object
import simulation.Simulation
import util.SolarSystemReader
import util.Vec

object CLI {
	def main(args: Array[String]): Unit = {
		println("Solar System Simulator")
		println("----------------------")
		
		printHelp()
		
		
		var simulation = new Simulation(Vector())
		var objects = Vector[Object]()
		
		while (true) {
			val cmd = parseInput(readLine("> "))
			
			cmd match {
				case Command.Load(filename) =>
					SolarSystemReader.loadFile(filename) match {
						case Some(objs) =>
							objects ++= objs
							
							println("Read " + objs.length + " objects.")
							if (objects.length != objs.length)
								println(objects.length + " objects altogether")
								
						case None =>
							println("Could not load file '" + filename + "'!")
					}
					
				case Command.Object(name, mass, radius, x, y, z, vx, vy, vz) =>
					objects +:= Object(name, mass, radius * 1000, Vec(x, y, z) * 1000, Vec(vx, vy, vz) * 1000)
					
				case Command.Simulate(duration, stepDuration, output) =>
					// TODO handle output
					simulation = new Simulation(objects)
					
					println(stepDuration)
					println
					
					for (o <- simulation.getObjects)
						println(o.name + " " + o.mass + " " + (o.radius / 1000))
					
					var collision = false
					while (simulation.simulatedTime <= duration && !collision) {
						val collided = simulation.simulate(stepDuration)
						if (collided)
							collision = true
						
						println
						for (o <- simulation.getObjects) {
							val p = o.position / 1000
							val v = o.velocity / 1000
							println(s"${p.x} ${p.y} ${p.z} ${v.z} ${v.z} ${v.z}")
						}
					}
					
					
				case Command.Status() =>
					println("name mass radius (x y z) (vx vy vz)")
					for (o <- objects) {
						val r = o.radius / 1000
						val p = o.position / 1000
						val v = o.velocity / 1000
						println(s"${o.name} ${o.mass} ${r} (${p.x} ${p.y} ${p.z}) (${v.x} ${v.y} ${v.z})")
					}
				
				case Command.Clear() =>
					objects = Vector()
					
				case Command.Help() =>
					printHelp()
					
				case Command.Exit() =>
					return
				
				case Command.Invalid(err) => err match {
					case Command.UnknownCommand(cmd) =>
						println("Unknown command '" + cmd + "'!")
					
					case Command.TooManyArguments() =>
						println("Too many arguments!")
					
					case Command.TooFewArguments() =>
						println("Too few arguments!")
					
					case Command.InvalidNumber() =>
						println("Parsing number failed!")
				}
					
			}
		}
	}

	
	private def printHelp() = println(
"""
  Command     Arguments
  -------     ---------

  load        filename
    Load an input file.

  object      name mass radius x y z vx vy vz
    Add an object. Mass as kg, radius and position as km (relative to the
    center), velocity as km/s.

  simulate    duration step_duration [outputfile]
    Run simulation until duration has elapsed (in simulation time) or some
    objects have collided.

  status
    Print all objects.

  clear
    Clear all objects.

  help
    Print this help.

  exit
    Exit this program.""")
		
	
	def parseInput(line: String): Command.Command = {
		val allParts = line.split("\\s+").filter(_.nonEmpty)
		val parts = new Queue[String]
		parts ++= allParts
		
		val cmd = try {
			parts.dequeue match {
				case "load" =>
					Command.Load(parts.dequeue)
						
				case "object" =>
					Command.Object(
						parts.dequeue,
						parts.dequeue.toDouble,
						parts.dequeue.toDouble,
						parts.dequeue.toDouble,
						parts.dequeue.toDouble,
						parts.dequeue.toDouble,
						parts.dequeue.toDouble,
						parts.dequeue.toDouble,
						parts.dequeue.toDouble)
					
				case "simulate" =>
					Command.Simulate(
						parts.dequeue.toDouble,
						parts.dequeue.toDouble,
						if (parts.nonEmpty) Some(parts.dequeue) else None)
						
				case "status" => Command.Status()
				case "clear"  => Command.Clear()
				case "help"   => Command.Help()
				case "exit"   => Command.Exit()
					
				case _ =>
					return Command.Invalid(Command.UnknownCommand(allParts(0)))
			}
		}
		catch {
			case _: java.lang.NumberFormatException =>
				return Command.Invalid(Command.InvalidNumber())
				
			case _: java.util.NoSuchElementException =>
				return Command.Invalid(Command.TooFewArguments())
		}
		
		if (parts.nonEmpty)
			return Command.Invalid(Command.TooManyArguments())
		
		cmd
	}
}

object Command {
	abstract class Command
	case class Load(filename: String) extends Command
	case class Object(name: String, mass: Double, radius: Double, x: Double, y: Double, z: Double, vx: Double, vy: Double, vz: Double) extends Command
	case class Simulate(duration: Double, durationStep: Double, output: Option[String]) extends Command
	case class Status extends Command
	case class Clear extends Command
	case class Help extends Command
	case class Exit extends Command
	case class Invalid(err: Error) extends Command
	
	abstract class Error
	case class UnknownCommand(cmd: String) extends Error
	case class TooManyArguments extends Error
	case class TooFewArguments extends Error
	case class InvalidNumber extends Error
}