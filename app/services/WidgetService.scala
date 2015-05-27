package services

import model.Widget

class WidgetService {

  def buildWidget: Widget = {
    new Widget(2, "Red")
  }

}

object WidgetService extends WidgetService
