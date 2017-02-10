package ru.spbau.mit.atum;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import static android.provider.CalendarContract.ACCOUNT_TYPE_LOCAL;

public class CalendarsWorker {

    private ContentResolver contentResolver;

    private static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
            CalendarContract.Calendars.ACCOUNT_TYPE                   // 4
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_ACCOUNT_TYPE_INDEX = 4;


    public CalendarsWorker(Context context) {
        contentResolver = context.getContentResolver();
    }

    public long findCalendarID(String accountName, String name) {
        Cursor cur;
        try {
            cur = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, EVENT_PROJECTION, "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND (" +
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?)", new String[]{accountName, name}, null);
        } catch (SecurityException e) {
            throw e;
        }
        long calID = -1;
        while (cur.moveToNext()) {
            calID = cur.getLong(PROJECTION_ID_INDEX);
        }
        cur.close();

        return calID;
    }

    public void deleteCalendarByName(String accountName, String name) {
        Uri hzUri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI, accountName,  ACCOUNT_TYPE_LOCAL);
        contentResolver.delete(hzUri, "(" + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?)", new String[]{name});
    }

    public void printAllCalendars() {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        Cursor cur;
        try {
            cur = contentResolver.query(uri, EVENT_PROJECTION, "", new String[]{}, null);
        } catch (SecurityException e) {
            throw e;
        }

        while (cur.moveToNext()) {
            long calID = cur.getLong(PROJECTION_ID_INDEX);
            String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            String accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            String ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            String accountType = cur.getString(PROJECTION_ACCOUNT_TYPE_INDEX);

            Log.i("myLog", ((Long)calID).toString() + " displayName = " + displayName
                    + "; accountName = " + accountName + "; ownerName = " + ownerName + "; accountType = " + accountType);
        }

        cur.close();

    }

    public void addNewCalendarIfNotExist(String accountName, String displayName) {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        Cursor cur;
        try {
            cur = contentResolver.query(uri, EVENT_PROJECTION, "(" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND (" +
                            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " = ?)",
                    new String[]{accountName, displayName}, null);
        } catch (SecurityException e) {
            throw e;
        }

        if (cur.getCount() > 0) {
            Log.i("myLog", "I have a calendar!!!");
        } else {
            addNewCalendar(uri, accountName, displayName);
        }

        cur.close();
    }

    private Uri addNewCalendar(Uri uri, String accountName, String displayName) {
        Uri hzUri = asSyncAdapter(uri, accountName, ACCOUNT_TYPE_LOCAL);

        ContentValues mNewValues = new ContentValues();
        mNewValues.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        mNewValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, displayName);
        mNewValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE_LOCAL);

        return contentResolver.insert(hzUri, mNewValues);
    }

    private static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }

}
