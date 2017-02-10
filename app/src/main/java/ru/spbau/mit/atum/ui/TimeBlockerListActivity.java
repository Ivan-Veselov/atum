package ru.spbau.mit.atum.ui;


import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.model.UserDefinedTimeBlocker;
import ru.spbau.mit.atum.model.UserSynchronisableData;

public class TimeBlockerListActivity extends AbstractFiltersHolderListActivity<UserDefinedTimeBlocker> {

    @Override
    protected void setTitleText() {
        title.setText(R.string.blocker_list);
    }

    @Override
    protected void initializeHolder() {
        filtersHolders = UserSynchronisableData.getInstance().getBlockers();
    }

    @Override
    protected Intent initializeIntent() {
        return new Intent(this, TimeBlockerEditorActivity.class);
    }

    @Override
    protected Intent initializeEditIntent(AbstractFiltersHolder holder) {
        return initializeIntent();
    }

    @Override
    protected void addSchedule(Map<String, Object> m, AbstractFiltersHolder u) {}

}
