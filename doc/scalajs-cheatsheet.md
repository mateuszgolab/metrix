

Accessing a Scala object in JS
==============================

Add `()` after it as it is a function in JS.

Binding a Scala class to an existing JS class
=============================================

@JSName("js-name")

Building JSON object in Scala
=============================

js.JSON.parse("""{ foo: "bar" }""") << works but not really legible
js.Dynamic.literal(foo = "bar") << right