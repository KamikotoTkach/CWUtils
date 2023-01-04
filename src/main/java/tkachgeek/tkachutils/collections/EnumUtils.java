package tkachgeek.tkachutils.collections;

import java.util.Optional;
import java.util.stream.Stream;

public class EnumUtils {
    public static <T extends Enum> Optional<T> getEnumInstance(T[] values, String value) {
        return Stream.of(values)
                .filter(instance -> instance.name().equalsIgnoreCase(value))
                .findFirst();
    }
}
