package simulation

import util.Vec


case class Object(
	var name: String,
	var mass: Double,
	var radius: Double,
	var position: Vec,
	var velocity: Vec)

case class ObjectDeriv(
	var position: Vec = Vec(),
	var velocity: Vec = Vec())