package gui

import swing._

class SidePanel extends BoxPanel(Orientation.Vertical) {
	contents += new Button("Foo")
	contents += new Button("Bar")
}