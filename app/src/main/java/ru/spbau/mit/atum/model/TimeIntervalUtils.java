package ru.spbau.mit.atum.model;

import android.support.annotation.NonNull;

import org.joda.time.ReadableDateTime;

import java.util.concurrent.TimeUnit;

/**
 * Класс, содержащий вспомогательные статические методы для работы с временными интервалами.
 */
public final class TimeIntervalUtils {
    /**
     * Вспомогательный метод, который проверяет, что переданные моменты времени идут в правильном
     * порядке: сначала первый, затем второй.
     *
     * @param initialMoment первый момент времени.
     * @param finalMoment второй момент времени.
     * @return true, если проверка успешна.
     */
    public static boolean checkOrderOfMoments(@NonNull ReadableDateTime initialMoment,
                                              @NonNull ReadableDateTime finalMoment) {
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
    public static int convertToPointRelative(@NonNull ReadableDateTime initialMoment,
                                             @NonNull ReadableDateTime moment) {
        return (int) (TimeUnit.MILLISECONDS.toMinutes(moment.getMillis())
                - TimeUnit.MILLISECONDS.toMinutes(initialMoment.getMillis()));
    }
}
