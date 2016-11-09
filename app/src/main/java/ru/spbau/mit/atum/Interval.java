package ru.spbau.mit.atum;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Полуинтервал на прямой (левая граница включена), который представляется двумя числами -
 * началом и концом. Начало всегда не превосходит конца.
 */
public class Interval {
    private static final String RIGHT_LESS_THAN_LEFT =
            "Right right of the Interval is less than left right.";

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
    public EndPoint leftEndPoint() {
        return new EndPoint(left, false);
    }

    /**
     * @return объект класса EndPoint, представляющий правый конец интервала.
     */
    public EndPoint rightEndPoint() {
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
    public Interval intersection(Interval other) {
        int begin = Math.max(this.left, other.left);
        int end = Math.min(this.right, other.right);

        if (end < begin) {
            end = begin;
        }

        return new Interval(begin, end);
    }

    /**
     * Приводит набор интервалов в "нормальный" вид. Под нормальным видом подразумевается некоторый
     * другой набор интервалов, которые не пересекаются, идут в отсортированном порядке и задают то
     * же самое множество точек.
     * Итоговый нормальный вид складывается в переданную коллекцию.
     *
     * @param intervalsSet список интервалов, которые нужно привести в нормальный вид.
     * @return результирующий нормальный вид.
     */
    public static List<Interval> normalize(List<? extends Interval> intervalsSet) {
        throw new UnsupportedOperationException();
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
         * Естественный порядок отношения на точках. Обычное сравнение координат.
         *
         * @param other концевая точка, с которой нужно сравниться.
         * @return результат сравнения.
         */
        @Override
        public int compareTo(@NotNull EndPoint other) {
            return Integer.compare(coordinate, other.coordinate);
        }
    }
}
