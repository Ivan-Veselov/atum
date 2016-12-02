package ru.spbau.mit.atum;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Фильтр, который задает непрерывный промежуток времени для каждого из заданных дней недели.
 * Промежуток может заканчиваться на следующий день, но его длительность не должна превышать суток.
 *
 * <p>TODO: ограничение на длину промежутка можно убрать.
 * Для этого потребуется усложнить алгоритм преобразования в список отрезков.
 */
public class WeekFilter extends TimeFilter {
    private final int firstMinute;

    private final int duration;

    private final EnumSet<DaysOfWeek> mask;

    /**
     * Создает новый фильтр.
     *
     * @param firstMinute первая минута дня, которую задает фильтр.
     * @param duration длина одного промежутка. Не должна превышать 24 часа.
     * @param mask маска, представляющая те дни недели, в которых находятся временные промежутки,
     *             которые задает фильтр. Неделя начинается с понедельника.
     * @param exclusiveFlag если true, то фильтр будет исключающим.
     */
    @Deprecated
    public WeekFilter(int firstMinute, int duration, @NonNull EnumSet<DaysOfWeek> mask,
                      boolean exclusiveFlag) {
        super("Filter", exclusiveFlag ? ExclusionType.EXCLUSIONARY : ExclusionType.COMMON);

        if (firstMinute < 0 || firstMinute >= DateTimeConstants.MINUTES_PER_DAY
                || duration < 0 || duration > DateTimeConstants.MINUTES_PER_DAY) {
            throw new IllegalArgumentException("Invalid time interval for week filter.");
        }

        this.firstMinute = firstMinute;
        this.duration = duration;
        this.mask = mask;
    }

    /**
     * Создает новый фильтр.
     *
     * @param description описание фильтра.
     * @param firstMinute первая минута дня, которую задает фильтр.
     * @param duration длина одного промежутка. Не должна превышать 24 часа.
     * @param mask маска, представляющая те дни недели, в которых находятся временные промежутки,
     *             которые задает фильтр. Неделя начинается с понедельника.
     * @param exclusionType тип фильтра (исключающий или нет).
     */
    public WeekFilter(@NonNull String description,
                      int firstMinute,
                      int duration,
                      @NonNull EnumSet<DaysOfWeek> mask,
                      @NonNull ExclusionType exclusionType) {
        super(description, exclusionType);

        if (firstMinute < 0 || firstMinute >= DateTimeConstants.MINUTES_PER_DAY
                || duration < 0 || duration > DateTimeConstants.MINUTES_PER_DAY) {
            throw new IllegalArgumentException("Invalid time interval for week filter.");
        }

        this.firstMinute = firstMinute;
        this.duration = duration;
        this.mask = mask;
    }

    /**
     * Создает новый фильтр. Вместо длины промедутка принимает минуту, которая идет после последней.
     *
     * @param description описание фильтра.
     * @param firstMinute первая минута дня, которую задает фильтр.
     * @param minuteAfterLast минута, которая идет после последней минуты промежутка.
     * @param mask маска, представляющая те дни недели, в которых находятся временные промежутки,
     *             которые задает фильтр. Неделя начинается с понедельника.
     * @param exclusionType тип фильтра (исключающий или нет).
     * @return новый фильтр.
     */
    public static WeekFilter newWeekFilterFromMinutesInterval(@NonNull String description,
                                                              int firstMinute,
                                                              int minuteAfterLast,
                                                              @NonNull EnumSet<DaysOfWeek> mask,
                                                              @NonNull ExclusionType exclusionType) {
        int duration = minuteAfterLast - firstMinute;
        if (duration <= 0) {
            duration += DateTimeConstants.MINUTES_PER_DAY;
        }

        return new WeekFilter(description, firstMinute, duration, mask, exclusionType);
    }

    /**
     * Возвращает описание типа фильтра в заданном контексте.
     *
     * @param context контекст приложения.
     * @return описание типа фильтра.
     */
    public @NonNull String getTypeDescription(Context context) {
        return context.getString(R.string.week_filter);
    }

    /**
     * @return первая минута дня, которую задает фильтр.
     */
    public int getFirstMinute() {
        return firstMinute;
    }

    /**
     * @return длина одного промежутка.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Возвращает минуту, котороя идет после последней минуты промежутка дня, который задает фильтр.
     * Возможно, что эта минута принадлежит уже следующему дню.
     */
    public int getMinuteAfterLast() {
        return (firstMinute + duration) % DateTimeConstants.MINUTES_PER_DAY;
    }

    /**
     * @return маска дней недели, которые задает фильтр.
     */
    public @NonNull EnumSet<DaysOfWeek> getWeekMask() {
        return mask;
    }

    /**
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param globalInterval целочисленный интервал всего доступного времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр, в отсортированном порядке.
     */
    @Override
    protected List<Interval> intervalRepresentationImpl(@NonNull ReadableDateTime initialMoment,
                                                        @NonNull Interval globalInterval) {
        int minuteOffset = initialMoment.getHourOfDay() * DateTimeConstants.MINUTES_PER_HOUR
                         + initialMoment.getMinuteOfHour();

        // У понедельника номер 1
        int dayOfWeek = initialMoment.getDayOfWeek() - DateTimeConstants.MONDAY;

        int currentIntervalBeginning = firstMinute - minuteOffset;

        List<Interval> intervalList = new ArrayList<>();

        while (currentIntervalBeginning < globalInterval.right()) {
            if (mask.contains(DaysOfWeek.values()[dayOfWeek])) {
                Interval interval = new Interval(currentIntervalBeginning,
                                                 currentIntervalBeginning + duration);

                interval = interval.intersection(globalInterval);
                if (!interval.isEmpty()) {
                    intervalList.add(interval);
                }
            }

            dayOfWeek = (dayOfWeek + 1) % DateTimeConstants.DAYS_PER_WEEK;
            currentIntervalBeginning += DateTimeConstants.MINUTES_PER_DAY;
        }

        return intervalList;
    }

    public enum DaysOfWeek {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};
}
