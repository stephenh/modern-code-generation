package example1.objects;

import java.util.ArrayList;
import java.util.List;

/** Utility methods primarily for dealing with the lists in the generated stub classes. */
public class ListUtils {

  static <T> List<T> add(final List<T> list, final T value) {
    final List<T> copy = copyPotentiallyNull(list);
    copy.add(value);
    return copy;
  }

  static <T> List<T> remove(final List<T> list, final T value) {
    final List<T> copy = copyPotentiallyNull(list);
    copy.remove(value);
    return copy;
  }

  private static <T> List<T> copyPotentiallyNull(final List<T> list) {
    if (list == null) {
      return new ArrayList<T>();
    } else {
      return new ArrayList<T>(list);
    }
  }

}
