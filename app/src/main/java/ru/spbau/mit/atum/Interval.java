package ru.spbau.mit.atum;

/**
 * Полуинтервал на прямой (левая граница включена), который представляется двумя числами -
 * началом и концом. Начало всегда не превосходит конца.
 */
public class Interval {
    private static final String END_LESS_THAN_BEG = "End of the Interval is less than beginning.";

    private int begin;

    private int end;

    /**
     * Создает новый интервал с заданными концами. В случае некорректных аргументов выбрасывается
     * исключение.
     *
     * @param begin начало интервала.
     * @param end конец интервала.
     */
    public Interval(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException(END_LESS_THAN_BEG);
        }

        this.begin = begin;
        this.end = end;
    }

    /**
     * @return начало интервала.
     */
    public int begin() {
        return begin;
    }

    /**
     * @return конец интервала.
     */
    public int end() {
        return end;
    }

    /**
     * Проверяет является ли интервал пустым.
     *
     * @return true, если интервал пустой.
     */
    public boolean isEmpty() {
        return begin == end;
    }

    /**
     * Пересекает этот интервал с переданным в качестве аргумента, сохраняя резуьтат в текущем
     * интервале. Если пересечение пустое, то результатом будет интервал вида [a, a), где a -
     * наибольший из левых концов пересекаемых интервалов.
     *
     * @param interval интервал, с которым нужно пересечь.
     */
    public void intersectWith(Interval interval) {
        begin = Math.max(begin, interval.begin);
        end = Math.min(end, interval.end);

        if (end < begin) {
            end = begin;
        }
    }
}
