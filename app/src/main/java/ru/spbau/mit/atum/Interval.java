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
}
