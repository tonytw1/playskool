package views

import org.joda.time.DateTime

trait WithHeader {

  implicit def header : Header = {
    new Header(DateTime.now())
  }

}
