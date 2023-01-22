package tkachgeek.tkachutils.collections;

import java.util.Optional;

public class EnumUtils {
    public static <T extends Enum> Optional<T> getEnumInstance(T[] values, String value) {
        for (T instance : values) {
            if (instance.name().equalsIgnoreCase(value)) {
                return Optional.of(instance);
            }
        }
        return Optional.empty();
    }
}
