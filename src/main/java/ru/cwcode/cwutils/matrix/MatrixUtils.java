package ru.cwcode.cwutils.matrix;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class MatrixUtils {
  /**
   * @param array массив [y][x][z]
   *              <br>где первый элемент (y) - ось вращения
   * @param angle угол поворота, кратный 90
   * @return повернутый массив
   */
  @SuppressWarnings("unchecked")
  public <T> T[][][] rotate(T[][][] array, int angle) {
    int ySize = array.length;
    int xSize = array[0].length;
    int zSize = array[0][0].length;
    
    T[][][] rotated = array;
    
    switch (angle) {
      case 90 -> {
        rotated = (T[][][]) Array.newInstance(array.getClass().getComponentType().getComponentType().getComponentType(), ySize, zSize, xSize);
        for (int y = 0; y < ySize; y++) {
          for (int x = 0; x < xSize; x++) {
            for (int z = 0; z < zSize; z++) {
              rotated[y][z][xSize - 1 - x] = array[y][x][z];
            }
          }
        }
      }
      case 180 -> {
        rotated = (T[][][]) Array.newInstance(array.getClass().getComponentType().getComponentType().getComponentType(), ySize, xSize, zSize);
        for (int y = 0; y < ySize; y++) {
          for (int x = 0; x < xSize; x++) {
            for (int z = 0; z < zSize; z++) {
              rotated[y][xSize - 1 - x][zSize - 1 - z] = array[y][x][z];
            }
          }
        }
      }
      case 270 -> {
        rotated = (T[][][]) Array.newInstance(array.getClass().getComponentType().getComponentType().getComponentType(), ySize, zSize, xSize);
        for (int y = 0; y < ySize; y++) {
          for (int x = 0; x < xSize; x++) {
            for (int z = 0; z < zSize; z++) {
              rotated[y][zSize - 1 - z][x] = array[y][x][z];
            }
          }
        }
      }
    }
    
    return rotated;
  }
  
  /**
   * @param array массив[y][x][z]
   * @param element искомый элемент
   * @return массив [y][x][z] - координаты элемента
   */
  public Optional<Integer[]> findFirstPosition(Object[][][] array, Object element) {
    List<Integer[]> positions = findAllPositions(array, element);
    if (positions.isEmpty()) return Optional.empty();
    
    return Optional.of(positions.get(0));
  }
  
  /**
   * @param array массив[y][x][z]
   * @param element искомый элемент
   * @return массив [y][x][z] - координаты элемента
   */
  public List<Integer[]> findAllPositions(Object[][][] array, Object element) {
    int ySize = array.length;
    int xSize = array[0].length;
    int zSize = array[0][0].length;
    
    List<Integer[]> positions = new ArrayList<>();
    
    for (int y = 0; y < ySize; y++) {
      for (int x = 0; x < xSize; x++) {
        for (int z = 0; z < zSize; z++) {
          if (!array[y][x][z].equals(element)) continue;
          positions.add(new Integer[]{y, x, z});
        }
      }
    }
    
    return positions;
  }
}
