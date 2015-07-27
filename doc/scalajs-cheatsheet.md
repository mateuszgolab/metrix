

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

Including ScalaJS code in the page
==================================
not in <head> otherwise no sizes.

Not related to scalaJS per se but still useful to know

WebDriver test: executing some JS and returning a value
=======================================================
```
executeScript(s"(function() { return 2.0; })();")        // will return null!
executeScript(s"return (function() { return 2.0; })();") // will return 2.0 as a Double
executeScript(s"return 2.0;")                            // will also return 2.0
```

Pickling/Unpickling Double, Float, Short and Byte
=================================================

What you read is not necessarily what you write. For instance with
uPickle, if you write a double and then read from it, it's fine. But
if you read from js.Dynamic and try to cast into a Double it could
fail:
```
window.foo = write(2.0: Double)
read[Double](window.foo)       // OK
window.foo.asInstanceOf[Double] // KO: you get an Int: 2
```

You need to use java.lang.Number and then `doubleValue()` to get back
what you want:
```
window.foo.asInstanceOf[java.lang.Number].doubleValue() // OK!
```