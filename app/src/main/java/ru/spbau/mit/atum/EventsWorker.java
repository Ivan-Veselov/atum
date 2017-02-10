package ru.spbau.mit.atum;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

public class EventsWorker {

    private ContentResolver contentResolver;

    private long calID;

    private static final int PURPLE = 0xAB47BC;
    private static final int RED = 0xF44336;
    private static final int BLUE = 0x1976D2;

    private static final String[] EVENT_PROJECTION = new String[] {
            Events._ID,                           // 0
            Events.TITLE                          // 1
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_TITLE_INDEX = 0;

    public EventsWorker(Context context, long calID) {
        contentResolver = context.getContentResolver();
        this.calID = calID;
    }

    public long findEventIfByTitle(String title) {
        Cursor cur;
        try {
            cur = contentResolver.query(Events.CONTENT_URI, EVENT_PROJECTION, "(" + Events.TITLE + " = ?)",
                    new String[]{title}, null);
        } catch (SecurityException e) {
            throw e;
        }
        long eventID = -1;
        while (cur.moveToNext()) {
            eventID = cur.getLong(PROJECTION_ID_INDEX);
        }

        cur.close();

        return eventID;

    }

    public void addTask(UserDefinedTask task) {
        if (task.getScheduledTime() == null) {
            return;
        }
        long startMillis = task.getScheduledTime().getMillis();
        long endMillis = task.getScheduledTime().toDateTime().plusMinutes((int) task.getDuration().getStandardMinutes()).getMillis();

        ContentValues values = new ContentValues();
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, task.getName());
        values.put(Events.DESCRIPTION, task.getDescription());
        values.put(Events.EVENT_TIMEZONE, "UTC+03:00");

        if (task.getPlace() != null) {
            values.put(Events.EVENT_LOCATION, task.getPlace().getAddress().toString());
        }

        if (task.getType() == UserDefinedTask.Type.GENERAL) {
            values.put(Events.EVENT_COLOR, PURPLE);
        } else if (task.getType() == UserDefinedTask.Type.FIXED) {
            values.put(Events.EVENT_COLOR, BLUE);
        } else {
            values.put(Events.EVENT_COLOR, RED);
        }

        Uri uri;

        try {
            uri = contentResolver.insert(Events.CONTENT_URI, values);
        } catch (SecurityException e) {
            Log.i("myLog", e.toString());
            throw e;
        }

        addReminder(Long.parseLong(uri.getLastPathSegment()));

    }

    public void addReminder(long eventId) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.MINUTES, 15);
        values.put(CalendarContract.Reminders.EVENT_ID, eventId);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        try {
            contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values);
        } catch (SecurityException e) {
            throw e;
        }
    }

    public void deleteEventById(long eventID) {
        contentResolver.delete(ContentUris.withAppendedId(Events.CONTENT_URI, eventID), null, null);
    }

    public void deleteEventByTitle(String title) {
        try {
            contentResolver.delete(Events.CONTENT_URI, "(" + Events.TITLE + " = ?)", new String[]{title});
        } catch (SecurityException e) {
            throw e;
        }
    }

    public void deleteAll() {
        try {
            contentResolver.delete(Events.CONTENT_URI, "(" + Events.CALENDAR_ID + " = ?)",
                    new String[]{((Long)calID).toString()});
        } catch (SecurityException e) {
            throw e;
        }
    }

}
