package engine.utils;

public class Utils {

  public static int theta(float x, float y) {
    double mag = Math.sqrt(x * x + y * y);
    double dx = x / mag;
    double dy = y / mag;
    double theta;
    if (dx >= 0.5) {
      theta = Math.acos(dx);
      theta = Math.toDegrees(theta);
      if (dy < 0)
        theta = 360.0 - theta;
    } else if (dx <= -0.5) {
      theta = Math.acos(dx);
      theta = Math.toDegrees(theta);
      if (dy < 0)
        theta = 180.0 + (180.0 - theta);
    } else if (dy >= 0) {
      theta = Math.asin(dy);
      theta = Math.toDegrees(theta);
      if (dx < 0)
        theta = 90.0 + (90.0 - theta);
    } else {
      theta = Math.asin(dy);
      theta = Math.toDegrees(-theta);
      if (dx < 0)
        theta = 180.0 + theta;
      else
        theta = 360.0 - theta;
    }
    return (int) theta;
  }

}
