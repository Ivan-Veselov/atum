package ru.spbau.mit.atum;

import java.util.List;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTimeConstants;
import org.joda.time.ReadableDateTime;

/**
 * Фильтр, который задает непрерывный промежуток времени для каждого из заданных дней недели.
 * Промежуток может заканчиваться на следующий день, но его длительность не должна превышать суток.
 *
 * <p>TODO: ограничение на длину промежутка можно убрать.
 * Для этого потребуется усложнить алгоритм преобразования в список отрезков.
 */
public class WeekFilter extends TimeFilter {
    private static final String INVALID_INTERVAL_MSG = "Invalid time interval for week filter.";

    private int firstMinute;

    private int duration;

    private WeekMask mask;

    /**
     * Создает новый фильтр.
     *
     * @param firstMinute первая минута дня, которую задает фильтр.
     * @param duration длина одного промежутка. Не должна превышать 24 часа.
     * @param mask маска, представляющая те дни недели, в которых находятся временные промежутки,
     *             которые задает фильтр. Неделя начинается с понедельника.
     * @param exclusiveFlag если true, то фильтр будет исключающим.
     */
    public WeekFilter(int firstMinute, int duration, @NotNull WeekMask mask,
                      boolean exclusiveFlag) {
        super(exclusiveFlag);

        if (firstMinute < 0 || firstMinute >= DateTimeConstants.MINUTES_PER_DAY
                || duration < 0 || duration > DateTimeConstants.MINUTES_PER_DAY) {
            throw new IllegalArgumentException(INVALID_INTERVAL_MSG);
        }

        this.firstMinute = firstMinute;
        this.duration = duration;
        this.mask = mask;
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
     * @return маска дней недели, которые задает фильтр.
     */
    public @NotNull WeekMask getWeekMask() {
        return mask;
    }

    /**
     * @param initialMoment точка отсчета и нижняя граница времени.
     * @param globalInterval целочисленный интервал всего доступного времени.
     * @return набор непересекающихся интервалов, представляющий временные промежутки, которые
     *         задает фильтр.
     */
    @Override
    protected List<DualInterval> intervalRepresentationImpl(@NotNull ReadableDateTime initialMoment,
                                                            @NotNull Interval globalInterval) {
        int minuteOffset = initialMoment.getHourOfDay() * DateTimeConstants.MINUTES_PER_HOUR
                         + initialMoment.getMinuteOfHour();

        // У понедельника номер 1
        int dayOfWeek = initialMoment.getDayOfWeek() - DateTimeConstants.MONDAY;

        int currentIntervalBeginning = firstMinute - minuteOffset;

        List<DualInterval> intervalList = new ArrayList<>();

        while (currentIntervalBeginning < globalInterval.end()) {
            if (mask.isSet(dayOfWeek)) {
                DualInterval interval = new DualInterval(currentIntervalBeginning,
                        currentIntervalBeginning + duration,
                        isExclusive());

                interval.intersectWith(globalInterval);
                if (!interval.isEmpty()) {
                    intervalList.add(interval);
                }
            }

            dayOfWeek = (dayOfWeek + 1) % DateTimeConstants.DAYS_PER_WEEK;
            currentIntervalBeginning += DateTimeConstants.MINUTES_PER_DAY;
        }

        return intervalList;
    }
}
