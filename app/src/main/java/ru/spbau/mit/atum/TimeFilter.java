package ru.spbau.mit.atum;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public List<DualInterval> intervalRepresentation(Calendar initialMoment, Calendar finalMoment) {
        if (!checkOrderOfMoments(initialMoment, finalMoment)) {
            throw new IllegalArgumentException(FINAL_LESS_THAN_INIT_MSG);
        }

        return intervalRepresentationImpl(initialMoment,
                new Interval(0, convertToPointRelative(initialMoment, finalMoment)));
    }

    /**
     * Вспомогательный метод, который должны перегружать производные классы, в то время как
     * intervalRepresentation выполняет общую работу и вызывает этот метод.
     *
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param globalInterval целочисленный интервал всего доступного времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр.
     */
    protected abstract List<DualInterval> intervalRepresentationImpl(Calendar initialMoment,
                                                                     Interval globalInterval);

    /**
     * Вспомогательный метод, который проверяет, что переданные моменты времени идут в правильном
     * порядке: сначала первый, затем второй.
     *
     * @param initialMoment первый момент времени.
     * @param finalMoment второй момент времени.
     * @return true, если проверка успешна.
     */
    protected static boolean checkOrderOfMoments(Calendar initialMoment, Calendar finalMoment) {
        return initialMoment.compareTo(finalMoment) <= 0;
    }

    /**
     * Вспомогательный метод, который переводит момент времени в число, которое является сдвигом
     * в минутах относительно другого момента времени. Сдвиг высчитывается, отбрасывая более точные
     * единицы измерения.
     *
     * @param initialMoment момент времени, относительно которого считается сдвиг.
     * @param moment момент времени, который нужно перевести в число.
     * @return численное представление момента времени.
     */
    protected static int convertToPointRelative(Calendar initialMoment, Calendar moment) {
        return (int)(TimeUnit.MILLISECONDS.toMinutes(moment.getTimeInMillis()) -
                     TimeUnit.MILLISECONDS.toMinutes(initialMoment.getTimeInMillis()));
    }
}
