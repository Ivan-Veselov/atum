package ru.spbau.mit.atum;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;
import org.joda.time.ReadableDateTime;

/**
 * Временной фильтр задает некоторым специфичным образом временные промежутки. Фильтр может быть
 * обычным, а может быть "исключающим". Исключающий фильтр, в отличие от обычного, исключает
 * промежутки времени, которые он задает.
 */
public abstract class TimeFilter implements Serializable {
    protected static final String FINAL_LESS_THAN_INIT_MSG = "Final moment is less than initial.";

    private final String description;

    private final boolean exclusiveFlag;

    /**
     * Создает новый фильтр.
     *
     * @param exclusiveFlag если true, то фильтр будет исключающим.
     */
    public TimeFilter(boolean exclusiveFlag) {
        this.description = "Filter";
        this.exclusiveFlag = exclusiveFlag;
    }

    /**
     * Возвращает описание типа фильтра в заданном контексте.
     *
     * @param context контекст приложения.
     * @return описание типа фильтра.
     */
    public abstract @NonNull String getTypeDescription(Context context);

    /**
     * Возвращает описание фильтра.
     */
    public @NonNull String getDescription() {
        return description;
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
     * Интервалы расположены в отсортированном порядке.
     *
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param finalMoment верхняя граница времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр, в отсортированном порядке.
     */
    public @NotNull List<Interval> intervalRepresentation(@NotNull ReadableDateTime initialMoment,
                                                          @NotNull ReadableDateTime finalMoment) {
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
     *         задает фильтр, в отсортированном порядке.
     */
    protected abstract List<Interval> intervalRepresentationImpl(ReadableDateTime initialMoment,
                                                                 Interval globalInterval);

    /**
     * Вспомогательный метод, который проверяет, что переданные моменты времени идут в правильном
     * порядке: сначала первый, затем второй.
     *
     * @param initialMoment первый момент времени.
     * @param finalMoment второй момент времени.
     * @return true, если проверка успешна.
     */
    protected static boolean checkOrderOfMoments(@NotNull ReadableDateTime initialMoment,
                                                 @NotNull ReadableDateTime finalMoment) {
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
    protected static int convertToPointRelative(@NotNull ReadableDateTime initialMoment,
                                                @NotNull ReadableDateTime moment) {
        return (int) (TimeUnit.MILLISECONDS.toMinutes(moment.getMillis())
                      - TimeUnit.MILLISECONDS.toMinutes(initialMoment.getMillis()));
    }
}
