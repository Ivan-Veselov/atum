package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Полуинтервал на прямой (левая граница включена), который представляется двумя числами -
 * началом и концом. Начало всегда не превосходит конца.
 */
public class Interval {
    private static final String RIGHT_LESS_THAN_LEFT =
            "Right end of the Interval is less than left end.";

    private final int left;

    private final int right;

    /**
     * Создает новый интервал с заданными концами. В случае некорректных аргументов выбрасывается
     * исключение.
     *
     * @param left левый конец интервала.
     * @param right правый конец интервала.
     */
    public Interval(int left, int right) {
        if (right < left) {
            throw new IllegalArgumentException(RIGHT_LESS_THAN_LEFT);
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
    public @NotNull EndPoint leftEndPoint() {
        return new EndPoint(left, false);
    }

    /**
     * @return объект класса EndPoint, представляющий правый конец интервала.
     */
    public @NotNull EndPoint rightEndPoint() {
        return new EndPoint(right, true);
    }

    /**
     * Возвращает новый интервал, который является пересечением текущего и переданного в качестве
     * аргумента. Если пересечение пустое, то результатом будет интервал вида [a, a), где a -
     * наибольший из левых концов пересекаемых интервалов.
     *
     * @param other интервал, с которым нужно найти пересечение.
     * @return новый интервал, который является пересечением текущего интервала и аргумента.
     */
    public @NotNull Interval intersection(@NotNull Interval other) {
        int begin = Math.max(this.left, other.left);
        int end = Math.min(this.right, other.right);

        if (end < begin) {
            end = begin;
        }

        return new Interval(begin, end);
    }

    /**
     * Преобразует набор интервалов в набор концов этих интервалов.
     *
     * @param intervalsSet список интервалов.
     * @return список концов каждого интервала из исходного набора.
     */
    public static @NotNull List<EndPoint> endPoints(
                                            @NotNull List<? extends Interval> intervalsSet) {
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
    public static @NotNull List<Interval> normalize(
                                            @NotNull List<? extends Interval> intervalsSet) {
        List<EndPoint> endPoints = endPoints(intervalsSet);
        Collections.sort(endPoints);

        List<Interval> result = new ArrayList<>();
        int openedIntervalsNumber = 0;

        // Начальное значение не будет использовано
        int leastCoveredPoint = 0;

        for (EndPoint point : endPoints) {
            if (!point.isRight()) {
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
    public static @NotNull List<Interval> difference(
                                                @NotNull List<? extends Interval> minuend,
                                                @NotNull List<? extends Interval> subtrahend) {
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

        private final boolean rightFlag;

        /**
         * Создает новую концевую точку.
         *
         * @param coordinate координата точки.
         * @param rightFlag если true, то точка является правой границей.
         */
        public EndPoint(int coordinate, boolean rightFlag) {
            this.coordinate = coordinate;
            this.rightFlag = rightFlag;
        }

        /**
         * @return координата точки.
         */
        public int getCoordinate() {
            return coordinate;
        }

        /**
         * @return true, если точка является правой границей интервала.
         */
        public boolean isRight() {
            return rightFlag;
        }

        /**
         * Естественный порядок отношения на точках. Обычное сравнение координат. В случае равенства
         * координат левые границы считаются меньше правых.
         *
         * @param other концевая точка, с которой нужно сравниться.
         * @return результат сравнения.
         */
        @Override
        public int compareTo(@NotNull EndPoint other) {
            int comparisonResult = Integer.compare(coordinate, other.coordinate);

            if (comparisonResult != 0) {
                return comparisonResult;
            }

            if (rightFlag == other.rightFlag) {
                return 0;
            }

            if (rightFlag) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
