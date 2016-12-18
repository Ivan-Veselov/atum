package ru.spbau.mit.atum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CalendarExporter extends AppCompatActivity {

    private final String defaultAccountName = "ATUM";
    private final String defaultDisplayName = "ATUM";
    private CalendarsWorker calendarsWorker;
    private EventsWorker eventsWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_exporter);

        calendarsWorker = new CalendarsWorker(getApplicationContext());

        calendarsWorker.addNewCalendarIfNotExist(defaultAccountName, defaultDisplayName);

        eventsWorker = new EventsWorker(getApplicationContext(),
                calendarsWorker.findCalendarID(defaultAccountName, defaultDisplayName));

        doSomeMagic();
    }

    public void doSomeMagic() {

        calendarsWorker.printAllCalendars();

        //eventsWorker.deleteEventByTitle("Jazzercise");
        //long eventId = eventsWorker.addExampleEvent();
        //Log.i("myLog", ((Long)eventId).toString());
        //eventsWorker.deleteEventById(eventId);

    }

}
