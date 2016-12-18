package ru.spbau.mit.atum;

import android.content.Context;

public class CalendarExporter {

    private final String defaultAccountName = "ATUM";
    private final String defaultDisplayName = "ATUM";
    private CalendarsWorker calendarsWorker;
    private EventsWorker eventsWorker;

    public CalendarExporter(Context context) {
        calendarsWorker = new CalendarsWorker(context);

        calendarsWorker.addNewCalendarIfNotExist(defaultAccountName, defaultDisplayName);

        eventsWorker = new EventsWorker(context,
                calendarsWorker.findCalendarID(defaultAccountName, defaultDisplayName));

    }

    public void clearCalendar() {
        eventsWorker.deleteAll();
    }

    public void addTaskToCalendar(UserDefinedTask task) {
        eventsWorker.addTask(task);
    }

}
