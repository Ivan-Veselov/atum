package ru.spbau.mit.atum;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends AbstractFiltersHolderListActivity {
    @Override
    protected void setTitleText() {
        title.setText("TASK LIST");
    }

    @Override
    protected void initializeHolder() {
        filtersHolders = (ArrayList<AbstractFiltersHolder>) (List) UserSynchronisableData.getInstance().getTasks();
    }

    @Override
    protected void addSchedule(Map<String, Object> m, AbstractFiltersHolder task) {
        UserDefinedTask userDefinedTask = (UserDefinedTask)task;
        if (userDefinedTask.getScheduledTime() == null) {
            m.put(IS_SCHEDULED, "not scheduled");
        } else {
            m.put(IS_SCHEDULED, "OK");
        }
    }

    @Override
    protected Intent initializeIntent() {
        return new Intent(this, TaskSwitchActivity.class);
    }

    @Override
    protected Intent initializeEditIntent(AbstractFiltersHolder holder) {
        UserDefinedTask task = (UserDefinedTask)holder;
        if (task.getType() == UserDefinedTask.Type.FIXED) {
            return new Intent(this, FixedTaskEditorActivity.class);
        }
        if (task.getType() == UserDefinedTask.Type.GENERAL) {
            return new Intent(this, TaskEditorActivity.class);
        }
        return null;
    }
}
