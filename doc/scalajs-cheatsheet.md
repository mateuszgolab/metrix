

Accessing a Scala object in JS
==============================

Add `()` after it as it is a function in JS.

@JSExport
object MyObject {
 def hello = println("Hello")
}

<script>
MyObject().hello()
</script>

Binding a Scala class to an existing JS class
=============================================

@JSName("js-name")

Building JSON object in Scala
=============================

js.JSON.parse("""{ foo: "bar" }""") << works but not really legible
js.Dynamic.literal(foo = "bar") << right

Conversion between js.Dynamic and T
===================================

val date: js.Date = new js.Date
val dateDyn: js.Dynamic = date.asInstanceOf[js.Dynamic]
val dateT: js.Date = dateDyn.asInstanceOf[js.Date]


Access Window in scalajs.dom
============================

`val window = dom.document.defaultView`