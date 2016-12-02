package ru.spbau.mit.atum;

import org.joda.time.DateTimeConstants;

import java.io.Serializable;

/**
 * Класс представляющий произвольный набор дней недели.
 */
public class WeekMask implements Serializable {
    private final int mask;

    /**
     * Конструирует набор на основе списка чисел. Если номер дня недели встречается в списке хотя бы
     * один раз, то этот день недели попадет в сконструированный набор. Если одно из чисел не
     * принадлежит промежутку от 0 до 6, то будет выброшено исключение.
     *
     * @param daysList список номеров дней недели.
     */
    public WeekMask(int... daysList) {
        int maskBuilder = 0;
        for (int dayNumber : daysList) {
            if (!checkDayNumber(dayNumber)) {
                throw new IllegalArgumentException("Day of week number is out of range [0, 6].");
            }

            maskBuilder |= 1 << dayNumber;
        }

        mask = maskBuilder;
    }

    /**
     * Проверяет по номеру дня недели, лежит ли данный день в наборе. Если номер не попадает в
     * промежуток от 0 до 6, то будет выброшено исключение.
     *
     * @param dayNumber номер дня недели.
     * @return true, если день содержится в наборе.
     */
    public boolean isSet(int dayNumber) {
        if (!checkDayNumber(dayNumber)) {
            throw new IllegalArgumentException("Day of week number is out of range [0, 6].");
        }

        return (mask & (1 << dayNumber)) != 0;
    }

    /**
     * Вспомогательный метод, который проверяет, что заданное число представляет из себя номер дня
     * недели.
     *
     * @param dayNumber число, которое нужно проверить.
     * @return true, если число является номером дня недели.
     */
    private boolean checkDayNumber(int dayNumber) {
        return 0 <= dayNumber && dayNumber < DateTimeConstants.DAYS_PER_WEEK;
    }
}
