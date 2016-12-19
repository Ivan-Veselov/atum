package ru.spbau.mit.atum;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.ReadableDateTime;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Временной фильтр задает некоторым специфичным образом временные промежутки. Фильтр может быть
 * обычным, а может быть "исключающим". Исключающий фильтр, в отличие от обычного, исключает
 * промежутки времени, которые он задает.
 */
public abstract class TimeFilter implements Serializable, Parcelable {
    private final String description;

    private final ExclusionType exclusionType;

    public static final Parcelable.Creator<TimeFilter> CREATOR
            = new Parcelable.Creator<TimeFilter>() {
        public TimeFilter createFromParcel(Parcel in) {
            return (TimeFilter) in.readSerializable();
        }

        public TimeFilter[] newArray(int size) {
            return new TimeFilter[size];
        }
    };

    /**
     * Создает новый фильтр.
     *
     * @param description описание фильтра.
     * @param exclusionType обозначает является ли фильтр исключающим.
     */
    protected TimeFilter(@NonNull String description, ExclusionType exclusionType) {
        this.description = description;
        this.exclusionType = exclusionType;
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
        return exclusionType == ExclusionType.EXCLUSIONARY;
    }

    /**
     * Возвращает тип фильтра (исключающий или нет).
     */
    public @NonNull ExclusionType exclusionType() {
        return exclusionType;
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
    public @NonNull List<Interval> intervalRepresentation(@NonNull ReadableDateTime initialMoment,
                                                          @NonNull ReadableDateTime finalMoment) {
        if (!checkOrderOfMoments(initialMoment, finalMoment)) {
            throw new IllegalArgumentException(
              "In arguments of intervalRepresentation method: final moment is less than initial.");
        }

        return intervalRepresentationImpl(initialMoment,
                new Interval(0, convertToPointRelative(initialMoment, finalMoment)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(this);
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
    protected static boolean checkOrderOfMoments(@NonNull ReadableDateTime initialMoment,
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
    protected static int convertToPointRelative(@NonNull ReadableDateTime initialMoment,
                                                @NonNull ReadableDateTime moment) {
        return (int) (TimeUnit.MILLISECONDS.toMinutes(moment.getMillis())
                      - TimeUnit.MILLISECONDS.toMinutes(initialMoment.getMillis()));
    }

    public enum ExclusionType { COMMON, EXCLUSIONARY }
}
