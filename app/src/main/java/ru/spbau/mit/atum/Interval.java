package ru.spbau.mit.atum;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.min;

/**
 * Полуинтервал на прямой (левая граница включена), который представляется двумя числами -
 * началом и концом. Начало всегда не превосходит конца.
 */
public final class Interval {
    private final int left;

    private final int right;

    public static Interval empty = new Interval(0, 0);

    /**
     * Создает новый интервал с заданными концами. В случае некорректных аргументов выбрасывается
     * исключение.
     *
     * @param left левый конец интервала.
     * @param right правый конец интервала.
     */
    public Interval(int left, int right) {
        if (right < left) {
            throw new IllegalArgumentException("Right end of the Interval is less than left end.");
        }

        this.left = left;
        this.right = right;
    }

    /**
     * @return начало интервала.
     */
    public int left() {
        return left;
    }

    /**
     * @return конец интервала.
     */
    public int right() {
        return right;
    }

    /**
     * Проверяет является ли интервал пустым.
     *
     * @return true, если интервал пустой.
     */
    public boolean isEmpty() {
        return left == right;
    }

    /**
     * @return объект класса EndPoint, представляющий левый конец интервала.
     */
    public @NonNull EndPoint leftEndPoint() {
        return new EndPoint(left, EndPoint.Type.LEFT);
    }

    /**
     * @return объект класса EndPoint, представляющий правый конец интервала.
     */
    public @NonNull EndPoint rightEndPoint() {
        return new EndPoint(right, EndPoint.Type.RIGHT);
    }

    /**
     * Возвращает новый интервал, который является пересечением текущего и переданного в качестве
     * аргумента. Если пересечение пустое, то результатом будет интервал вида [a, a), где a -
     * наибольший из левых концов пересекаемых интервалов.
     *
     * @param other интервал, с которым нужно найти пересечение.
     * @return новый интервал, который является пересечением текущего интервала и аргумента.
     */
    public @NonNull Interval intersection(@NonNull Interval other) {
        int begin = Math.max(this.left, other.left);
        int end = min(this.right, other.right);

        if (end < begin) {
            end = begin;
        }

        return new Interval(begin, end);
    }

    public boolean isIntersectionWithListOfIntervals(@NonNull List<Interval> others) {
        for (Interval other: others) {
            Interval intersection = intersection(other);
            if (intersection.left < intersection.right) {
                return true;
            }
        }
        return false;
    }

    public int length() {
        return right - left;
    }

    public Interval getMaxIntersectionWithListOfIntervals(@NonNull List<Interval> others) {
        Interval ans = empty;
        for (Interval other: others) {
            Interval intersection = intersection(other);
            if (intersection.length() > ans.length()) {
                ans = intersection;
            }
        }
        return ans;
    }

    public Interval withDuration(int duration) {
        return new Interval(left, min(right, left + duration));
    }

    public static Interval getMaxInterval(List<Interval> intervals) {
        Interval ans = empty;
        for (Interval interval: intervals) {
            if (interval.length() > ans.length()) {
                ans = interval;
            }
        }
        return ans;
    }

    /**
     * Преобразует набор интервалов в набор концов этих интервалов.
     *
     * @param intervalsSet список интервалов.
     * @return список концов каждого интервала из исходного набора.
     */
    public static @NonNull List<EndPoint> endPoints(@NonNull List<Interval> intervalsSet) {
        List<EndPoint> result = new ArrayList<>(2 * intervalsSet.size());

        for (Interval interval : intervalsSet) {
            result.add(interval.leftEndPoint());
            result.add(interval.rightEndPoint());
        }

        return result;
    }

    /**
     * Приводит набор интервалов в "нормализованный" вид. Под нормализованным видом подразумевается
     * некоторый другой набор интервалов, которые не пересекаются, идут в отсортированном порядке и
     * задают то же самое множество точек.
     *
     * @param intervalsSet список интервалов, которые нужно привести в нормализованный вид.
     * @return результирующий нормальный вид.
     */
    public static @NonNull List<Interval> normalize(@NonNull List<Interval> intervalsSet) {
        List<EndPoint> endPoints = endPoints(intervalsSet);
        Collections.sort(endPoints);

        List<Interval> result = new ArrayList<>();
        int openedIntervalsNumber = 0;

        // Начальное значение не будет использовано
        int leastCoveredPoint = 0;

        for (EndPoint point : endPoints) {
            if (point.getType() == EndPoint.Type.LEFT) {
                openedIntervalsNumber++;
                if (openedIntervalsNumber == 1) {
                    leastCoveredPoint = point.getCoordinate();
                }
            } else {
                openedIntervalsNumber--;
                if (openedIntervalsNumber == 0) {
                    result.add(new Interval(leastCoveredPoint, point.getCoordinate()));
                }
            }
        }

        return result;
    }

    /**
     * Рассматривая два набора интервалов на прямой как два множества, возвращает разность этих
     * множеств в виде набора интервалов в нормализованном виде.
     *
     * @param minuend уменьшаемое. Множество из которого вычитают.
     * @param subtrahend вычитаемое. Множество, которое вычитают.
     * @return результат операции в виде набора интервалов в нормализованном виде.
     */
    public static @NonNull List<Interval> difference(
                                                @NonNull List<Interval> minuend,
                                                @NonNull List<Interval> subtrahend) {
        minuend = normalize(minuend);
        subtrahend = normalize(subtrahend);

        List<Interval> result = new ArrayList<>();
        Iterator<? extends Interval> subtrahendIterator = subtrahend.iterator();
        Interval subtrahendInterval = null;

        if (subtrahendIterator.hasNext()) {
            subtrahendInterval = subtrahendIterator.next();
        }

        withNewInterval:
        for (Interval interval : minuend) {
            withCurrentInterval:
            while (true) {
                while (subtrahendInterval != null
                                            && subtrahendInterval.right() <= interval.left()) {
                    if (subtrahendIterator.hasNext()) {
                        subtrahendInterval = subtrahendIterator.next();
                    } else {
                        subtrahendInterval = null;
                    }
                }

                if (subtrahendInterval == null) {
                    result.add(interval);
                    continue withNewInterval;
                }

                if (subtrahendInterval.left() <= interval.left()) {
                    if (subtrahendInterval.right() < interval.right()) {
                        interval = new Interval(subtrahendInterval.right(), interval.right());
                        continue withCurrentInterval;
                    } else {
                        continue withNewInterval;
                    }
                } else {
                    if (subtrahendInterval.left() < interval.right()) {
                        result.add(new Interval(interval.left(), subtrahendInterval.left()));
                        interval = new Interval(subtrahendInterval.left(), interval.right());
                        continue withCurrentInterval;
                    } else {
                        result.add(interval);
                        continue withNewInterval;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Класс представляющий собой концевую точку интервала.
     */
    public static class EndPoint implements Comparable<EndPoint> {
        private final int coordinate;

        private final Type type;

        /**
         * Создает новую концевую точку.
         *
         * @param coordinate координата точки.
         * @param type тип концевой точки.
         */
        public EndPoint(int coordinate, @NonNull Type type) {
            this.coordinate = coordinate;
            this.type = type;
        }

        /**
         * @return координата точки.
         */
        public int getCoordinate() {
            return coordinate;
        }

        /**
         * Возвращает тип концевой точки.
         */
        public Type getType() {
            return type;
        }

        /**
         * Сравнивает данный конец интервала с другим объектом. Если объект не является концом
         * интервала, то есть представителем этого класса, то результат отрицательный. В противном
         * случае концевые точки считаются равными, если у них совпадют координаты и тип (левый или
         * правый конец).
         *
         * @param object объект, с которым нужно сравниться.
         * @return результат сравнения.
         */
        @Override
        public boolean equals(@Nullable Object object) {
            if (object == null || !(object instanceof EndPoint)) {
                return false;
            }

            EndPoint other = (EndPoint) object;

            return coordinate == other.coordinate && type == other.type;
        }

        /**
         * Возвращает хеш объекта.
         */
        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[] {coordinate, type});
        }

        /**
         * Естественный порядок отношения на точках. Обычное сравнение координат. В случае равенства
         * координат левые границы считаются меньше правых.
         *
         * @param other концевая точка, с которой нужно сравниться.
         * @return результат сравнения.
         */
        @Override
        public int compareTo(@NonNull EndPoint other) {
            int comparisonResult = Integer.compare(coordinate, other.coordinate);

            if (comparisonResult != 0) {
                return comparisonResult;
            }

            if (type == other.type) {
                return 0;
            }

            if (type == Type.RIGHT) {
                return 1;
            } else {
                return -1;
            }
        }

        public enum Type {LEFT, RIGHT};
    }
}
