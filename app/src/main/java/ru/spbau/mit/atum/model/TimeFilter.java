package ru.spbau.mit.atum.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

import org.joda.time.ReadableDateTime;

/**
 * Временной фильтр задает некоторым специфичным образом временные промежутки. Фильтр может быть
 * обычным, а может быть "исключающим". Исключающий фильтр, в отличие от обычного, исключает
 * промежутки времени, которые он задает.
 */
public abstract class TimeFilter implements Parcelable {
    private final String description;

    private final ExclusionType exclusionType;

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
        if (!TimeIntervalUtils.checkOrderOfMoments(initialMoment, finalMoment)) {
            throw new IllegalArgumentException(
              "In arguments of intervalRepresentation method: final moment is less than initial.");
        }

        return intervalRepresentationImpl(initialMoment,
                new Interval(0,
                             TimeIntervalUtils.convertToPointRelative(initialMoment, finalMoment)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(description);
        out.writeSerializable(exclusionType);
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

    protected TimeFilter(Parcel in) {
        description = in.readString();
        exclusionType = (ExclusionType) in.readSerializable();
    }

    public enum ExclusionType { COMMON, EXCLUSIONARY }
}
