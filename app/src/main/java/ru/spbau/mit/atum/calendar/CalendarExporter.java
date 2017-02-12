package ru.spbau.mit.atum.calendar;

import android.content.Context;

import java.util.List;

import ru.spbau.mit.atum.model.UserDefinedTask;

public final class CalendarExporter {

    private EventsWorker eventsWorker;

    public CalendarExporter(Context context) {
        CalendarsWorker calendarsWorker = new CalendarsWorker(context);

        String defaultAccountName = "ATUM";
        String defaultDisplayName = "ATUM";
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
