import scala.scalajs.js

package object metrix {
  // Note: We extends AnyVal to prevent runtime instantiation.  See
  // value class guide for more info.
  implicit class JsonHelper(val sc: StringContext) extends AnyVal {
    def json(args: Any*): js.Dynamic = {
      val strings = sc.parts.iterator
      val expressions = args.iterator
      var buffer = new StringBuffer(strings.next)
      while(strings.hasNext) {
        buffer append expressions.next
        buffer append strings.next
      }
      js.JSON.parse(buffer.toString())
    }
  }
}