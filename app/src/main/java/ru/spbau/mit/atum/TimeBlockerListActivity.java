package ru.spbau.mit.atum;


import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeBlockerListActivity extends AbstractFiltersHolderListActivity {

    @Override
    protected void setTitleText() {
        title.setText("BLOCKER LIST");
    }

    @Override
    protected void initializeHolder() {
        filtersHolders = (ArrayList<AbstractFiltersHolder>) (List) UserSynchronisableData.getInstance().getBlockers();
    }

    @Override
    protected Intent initializeIntent() {
        return new Intent(this, TimeBlockerEditorActivity.class);
    }

    @Override
    protected void addSchedule(Map<String, Object> m, AbstractFiltersHolder u) {}

}
