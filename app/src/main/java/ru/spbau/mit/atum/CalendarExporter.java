package ru.spbau.mit.atum;

import android.content.Context;

import java.util.List;

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

    public void addTasks(List<UserDefinedTask> tasks) {
        eventsWorker.deleteAll();
        for (UserDefinedTask task: tasks) {
            eventsWorker.addTask(task);
        }
    }

}
