package ru.spbau.mit.atum;

import java.util.Date;
import java.util.List;

/**
 * Временной фильтр задает некоторым специфичным образом временные промежутки. Фильтр может быть
 * обычным, а может быть "исключающим". Исключающий фильтр, в отличие от обычного, исключает
 * промежутки времени, которые он задает.
 */
public abstract class TimeFilter {
    protected static final String FINAL_LESS_THAN_INIT_MSG = "Final moment is less than initial.";

    private boolean exclusiveFlag;

    /**
     * Создает новый фильтр.
     *
     * @param exclusiveFlag если true, то фильтр будет исключающим.
     */
    public TimeFilter(boolean exclusiveFlag) {
        this.exclusiveFlag = exclusiveFlag;
    }

    /**
     * @return true, если фильтр исключающий.
     */
    public boolean isExclusive() {
        return exclusiveFlag;
    }

    /**
     * Конвертирует временные промежутки в набор непересекающихся интервалов на прямой. Интервалы
     * целочисленные, единицей дискретизации является минута.
     * Точкой отсчета считается первый аргумент, все временные промежутки до нее обрезаются или
     * отбрасываются. Второй аргумент задает правую границу, все временные промежутки после нее
     * обрезаются или отбрасываются. Таким образом бесконечное число интервалов получится не может.
     * Тип возвращаемых интервалов совпадает с типом фильтра.
     *
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param finalMoment верхняя граница времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр.
     */
    public abstract List<DualInterval> intervalRepresentation(Date initialMoment, Date finalMoment);
}
