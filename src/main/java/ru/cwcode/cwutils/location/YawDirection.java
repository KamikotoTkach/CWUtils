package ru.cwcode.cwutils.location;

public enum YawDirection {
  NORTH,
  NORTH_EAST(0, 2),
  EAST,
  SOUTH_EAST(2, 4),
  SOUTH,
  SOUTH_WEST(4, 6),
  WEST,
  NORTH_WEST(6, 0);
  
  public static final float SEGMENT_SIZE = 22.5f;
  final float from;
  final float to;
  int[] primary = new int[2];
  float middle;
  private boolean isPrimary = true;
  
  YawDirection(int prima1, int prima2) {
    this();
    isPrimary = false;
    primary[0] = prima1;
    primary[1] = prima2;
  }
  
  YawDirection() {
    this.from = normalize(-SEGMENT_SIZE + (ordinal() * 2) * SEGMENT_SIZE);
    this.to = normalize(from + SEGMENT_SIZE * 2);
    this.middle = from + SEGMENT_SIZE;
  }
  
  public static float normalize(float yaw) {
    yaw %= 360;
    
    if (yaw < 0) yaw = 360 + yaw;
    return yaw;
  }
  
  public static YawDirection parseToPrimaries(float yaw) {
    YawDirection direction = parse(yaw);
    if (direction.isPrimary) return direction;
    
    YawDirection best = null;
    float bestDif = -1;
    for (int i : direction.primary) {
      float dif = Math.abs(YawDirection.values()[i].middle - yaw);
      if (bestDif == -1 || dif < bestDif) {
        bestDif = dif;
        best = YawDirection.values()[i];
      }
    }
    return best;
  }
  
  public static YawDirection parse(float yaw) {
    yaw = normalize(yaw);
    for (YawDirection direction : values()) {
      if (Math.abs(direction.middle - yaw) <= SEGMENT_SIZE || direction.middle == 360 && yaw <= SEGMENT_SIZE) {
        return direction;
      }
    }
    return null;
  }
}
