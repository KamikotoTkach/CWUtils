package ru.cwcode.cwutils.vector;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class VectorUtils {
  public static Vector getClosestPointOnLine(Vector point, Vector startPoint, Vector endPoint) {
    Vector segmentDir = endPoint.clone().subtract(startPoint);
    Vector pointDir = point.clone().subtract(startPoint);
    double projectedLength = pointDir.dot(segmentDir) / segmentDir.length();
    if (projectedLength < 0 || projectedLength > segmentDir.length()) return null;
    
    return startPoint.clone().add(segmentDir.clone().normalize().multiply(projectedLength));
  }
  
  public static Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
    rotateAroundAxisX(v, angleX);
    rotateAroundAxisY(v, angleY);
    rotateAroundAxisZ(v, angleZ);
    return v;
  }
  
  public static Vector rotateAroundAxisX(Vector v, double angle) {
    double y, z, cos, sin;
    cos = Math.cos(angle);
    sin = Math.sin(angle);
    y = v.getY() * cos - v.getZ() * sin;
    z = v.getY() * sin + v.getZ() * cos;
    return v.setY(y).setZ(z);
  }
  
  public static Vector rotateAroundAxisY(Vector v, double angle) {
    double x, z, cos, sin;
    cos = Math.cos(angle);
    sin = Math.sin(angle);
    x = v.getX() * cos + v.getZ() * sin;
    z = v.getX() * -sin + v.getZ() * cos;
    return v.setX(x).setZ(z);
  }
  
  public static Vector rotateAroundAxisZ(Vector v, double angle) {
    double x, y, cos, sin;
    cos = Math.cos(angle);
    sin = Math.sin(angle);
    x = v.getX() * cos - v.getY() * sin;
    y = v.getX() * sin + v.getY() * cos;
    return v.setX(x).setY(y);
  }
  
  /**
   * Rotate a vector about a location using that location's direction
   */
  public static Vector rotateVector(Vector v, Location location) {
    return rotateVector(v, location.getYaw(), location.getPitch());
  }
  
  public static Vector rotateVector(Vector v, float yawDegrees, float pitchDegrees) {
    double yaw = Math.toRadians(-1 * (yawDegrees + 90));
    double pitch = Math.toRadians(-pitchDegrees);
    
    double cosYaw = Math.cos(yaw);
    double cosPitch = Math.cos(pitch);
    double sinYaw = Math.sin(yaw);
    double sinPitch = Math.sin(pitch);
    
    double initialX, initialY, initialZ;
    double x, y, z;
    
    // Z_Axis rotation (Pitch)
    initialX = v.getX();
    initialY = v.getY();
    x = initialX * cosPitch - initialY * sinPitch;
    y = initialX * sinPitch + initialY * cosPitch;
    
    // Y_Axis rotation (Yaw)
    initialZ = v.getZ();
    initialX = x;
    z = initialZ * cosYaw - initialX * sinYaw;
    x = initialZ * sinYaw + initialX * cosYaw;
    
    return new Vector(x, y, z);
  }
  
  public static double angleToXAxis(Vector vector) {
    return Math.atan2(vector.getX(), vector.getY());
  }
  
  public static Vector directional(float pitch, float yaw) {
    Vector vector = new Vector();
    
    vector.setY(-Math.sin(Math.toRadians(pitch)));
    
    double xz = Math.cos(Math.toRadians(pitch));
    
    vector.setX(-xz * Math.sin(Math.toRadians(yaw)));
    vector.setZ(xz * Math.cos(Math.toRadians(yaw)));
    
    return vector;
  }
  
  public static void rotateLocationToFace(Location center, BlockFace blockFace) {
    switch (blockFace) {
      case NORTH:
        center.setYaw(180);
        center.setPitch(0);
        break;
      case EAST:
        center.setYaw(-90);
        center.setPitch(0);
        break;
      case SOUTH:
        center.setYaw(0);
        center.setPitch(0);
        break;
      case WEST:
        center.setYaw(90);
        center.setPitch(0);
        break;
      case UP:
        center.setPitch(-90);
        center.setYaw(180);
        break;
      case DOWN:
        center.setYaw(180);
        center.setPitch(90);
        break;
    }
  }
}
