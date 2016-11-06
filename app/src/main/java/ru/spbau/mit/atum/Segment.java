package ru.spbau.mit.atum;

/**
 * Ненаправленный отрезок на прямой, который представляется двумя числами - координатами начала и
 * конца. Координата начала всегда не превосходит координаты конца.
 */
public class Segment {
    private static final String END_LESS_THAN_BEG =
            "Coordinate of the end of the Segment is less than coordinate of the beginning.";

    private int begin;

    private int end;

    /**
     * Создает новый отрезок с заданными координатами концов. В случае некорректных аргументов
     * выбрасывается исключение.
     *
     * @param begin координата начала отрезка.
     * @param end координата конца отрезка.
     */
    public Segment(int begin, int end) {
        if (end < begin) {
            throw new IllegalArgumentException(END_LESS_THAN_BEG);
        }

        this.begin = begin;
        this.end = end;
    }

    /**
     * @return координата начала отрезка.
     */
    public int begin() {
        return begin;
    }

    /**
     * @return координата конца отрезка.
     */
    public int end() {
        return end;
    }
}
