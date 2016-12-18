package ru.spbau.mit.atum;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.joda.time.DateTime;

import static android.provider.CalendarContract.ACCOUNT_TYPE_LOCAL;

public class CalendarExporter extends AppCompatActivity {

    private final String defaultAccountName = "yukikomodo@gmail.com";
    private final String defaultDisplayName = "ATUM";

    public static final String[] EVENT_PROJECTION = new String[] {
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT,                 // 3
            Calendars.ACCOUNT_TYPE                   // 4
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_ACCOUNT_TYPE_INDEX = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_exporter);

        doSomeMagic();
    }

    public void doSomeMagic() {

        addNewCalendarIfNotExist(defaultAccountName, defaultDisplayName);

        printAllCalendars();

        addEvent();

    }

    public static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, accountType).build();
    }

    public void deleteCalendarByName(String accountName, String name) {
        Uri hzUri = asSyncAdapter(Calendars.CONTENT_URI, accountName,  ACCOUNT_TYPE_LOCAL);
        getContentResolver().delete(hzUri, "(" + Calendars.CALENDAR_DISPLAY_NAME + " = ?)", new String[]{name});
    }

    public void changeAccountName(String accountName, String name) {
    }

    public void changeDisplayName(String accountName, String name) {
    }

    public void printAllCalendars() {
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;

        Cursor cur;
        try {
            cur = cr.query(uri, EVENT_PROJECTION, "", new String[]{}, null);
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
                    + " accountName = " + accountName + " ownerName = " + ownerName + " accountType = " + accountType);
        }

    }

    public void addNewCalendarIfNotExist(String accountName, String displayName) {
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;

        Cursor cur;

        try {
            cur = cr.query(uri, EVENT_PROJECTION, "(" + Calendars.ACCOUNT_NAME + " = ?) AND (" +
                                                 Calendars.CALENDAR_DISPLAY_NAME + " = ?)",
                    new String[]{accountName, displayName}, null);
        } catch (SecurityException e) {
            throw e;
        }

        if (cur.getCount() > 0) {
            Log.i("myLog", "I have a calendar!!!");
        } else {
            addNewCalendar(uri, accountName, displayName);
        }
    }

    public Uri addNewCalendar(Uri uri, String accountName, String displayName) {
        Uri hzUri = asSyncAdapter(uri, accountName, ACCOUNT_TYPE_LOCAL);

        ContentValues mNewValues = new ContentValues();
        mNewValues.put(Calendars.ACCOUNT_NAME, accountName);
        mNewValues.put(Calendars.CALENDAR_DISPLAY_NAME, displayName);
        mNewValues.put(Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE_LOCAL);

        return getContentResolver().insert(hzUri, mNewValues);
    }

    public void addEvent() {
        ContentResolver cr = getContentResolver();

        Cursor cur = null;
        try {
            cur = cr.query(Calendars.CONTENT_URI, EVENT_PROJECTION, "(" + Calendars.ACCOUNT_NAME + " = ?)", new String[]{"ME@me.me"}, null);
        } catch (SecurityException e) {
            throw e;
        }

        long calID = -1;
        while (cur.moveToNext()) {
            calID = cur.getLong(PROJECTION_ID_INDEX);
        }

        Log.i("myLog", ((Long)calID).toString());

        if (calID == -1) {
            throw new RuntimeException();
        }

        long startMillis = new DateTime(2016, 12, 20, 7, 30).getMillis();
        long endMillis = new DateTime(2016, 12, 20, 8, 45).getMillis();

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, "Jazzercise");
        values.put(Events.DESCRIPTION, "Group workout");
        //values.put(Events.CALENDAR_ID, calID);
        values.put(Events.CALENDAR_ID, 7);
        values.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
        //values.put(Events.ACCOUNT_TYPE, ACCOUNT_TYPE_LOCAL);
        Uri uri;

        try {
            // uri = cr.insert(asSyncAdapter(Events.CONTENT_URI, "ME@me.me",  ACCOUNT_TYPE_LOCAL), values); // ?
            uri = cr.insert(Events.CONTENT_URI, values);
        } catch (SecurityException e) {
            Log.i("myLog", e.toString());
            throw e;
        } catch (Exception e) {
            Log.i("myLog", e.toString());
            throw e;
        }

        Log.i("myLog", "Yaaay!!!");

        long eventID = Long.parseLong(uri.getLastPathSegment());
    }

    public void deleteEvent() {}

    public void changeEvent() {}


}
