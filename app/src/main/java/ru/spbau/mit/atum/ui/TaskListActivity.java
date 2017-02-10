package ru.spbau.mit.atum.ui;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.model.UserDefinedTask;
import ru.spbau.mit.atum.model.UserSynchronisableData;

public class TaskListActivity extends AbstractFiltersHolderListActivity {
    @Override
    protected void setTitleText() {
        title.setText(R.string.task_list);
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
        return new Intent(this, TabbedActivity.class);
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
        if (task.getType() == UserDefinedTask.Type.QUICKIE) {
            return new Intent(this, AdditionalTaskEditorActivity.class);
        }
        return null;
    }
}
