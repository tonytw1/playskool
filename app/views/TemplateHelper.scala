package views

class TemplateHelper {

  def meh(value: String): String = {
    "Meh: " + value.toUpperCase + " " + this.toString
  }

}

object TemplateHelper extends TemplateHelper