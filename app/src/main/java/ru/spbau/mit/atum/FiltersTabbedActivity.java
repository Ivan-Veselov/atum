package ru.spbau.mit.atum;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class FiltersTabbedActivity extends TabActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Interval");
        tabSpec.setContent(new Intent(this, IntervalFilterEditorActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Week");
        tabSpec.setContent(new Intent(this, WeekFilterEditorActivity.class));
        tabHost.addTab(tabSpec);

    }
}
