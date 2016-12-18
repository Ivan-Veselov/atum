package ru.spbau.mit.atum;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.util.Log;

import org.joda.time.DateTime;

public class EventsWorker {

    private ContentResolver contentResolver;
    private long calID;

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

    public long addExampleEvent() {
        long startMillis = new DateTime(2016, 12, 20, 7, 30).getMillis();
        long endMillis = new DateTime(2016, 12, 20, 8, 45).getMillis();

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, "Jazzercise");
        values.put(Events.DESCRIPTION, "Group workout");
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri;

        try {
            uri = contentResolver.insert(Events.CONTENT_URI, values);
        } catch (SecurityException e) {
            Log.i("myLog", e.toString());
            throw e;
        } catch (Exception e) {
            Log.i("myLog", e.toString());
            throw e;
        }

        Log.i("myLog", "Yaaay!!!");

        return Long.parseLong(uri.getLastPathSegment());
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

        return eventID;

    }

    public long addTask(UserDefinedTask task) {
        if (task.getScheduledTime() == null) {
            return -1;
        }
        long startMillis = task.getScheduledTime().getMillis();
        long endMillis = task.getScheduledTime().toDateTime().plusMinutes(task.getDuration()).getMillis();

        ContentValues values = new ContentValues();
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, task.getName());
        values.put(Events.DESCRIPTION, task.getDescription());
        values.put(Events.EVENT_TIMEZONE, "UTC+03:00");
        Uri uri;

        try {
            uri = contentResolver.insert(Events.CONTENT_URI, values);
        } catch (SecurityException e) {
            Log.i("myLog", e.toString());
            throw e;
        }

        Log.i("myLog", "Yaaay!!!");

        return Long.parseLong(uri.getLastPathSegment());

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

    public void changeEvent() {}

}