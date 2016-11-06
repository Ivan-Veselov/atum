package ru.spbau.mit.atum;

/**
 * Подвид интервала, который разбивает свои экземпляры на два вида: обычные интервалы и
 * "исключающие". По смыслу исключающий интервал задает все точки прямой, кроме самих точек
 * интервала.
 */
public class DualInterval extends Interval {
    private boolean exclusiveFlag;

    /**
     * Создает новый интервал с заданными концами. В случае некорректных аргументов выбрасывается
     * исключение.
     *
     * @param begin начало интервала.
     * @param end конец интервала.
     * @param exclusiveFlag если true, то создастся исключающий интервал.
     */
    public DualInterval(int begin, int end, boolean exclusiveFlag) {
        super(begin, end);

        this.exclusiveFlag = exclusiveFlag;
    }

    /**
     * @return true, если интервал исключающий.
     */
    boolean isExclusive() {
        return exclusiveFlag;
    }
}
