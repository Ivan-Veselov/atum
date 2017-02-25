package ru.spbau.mit.atum.ui;

import android.content.Intent;

import java.util.Map;

import ru.spbau.mit.atum.R;
import ru.spbau.mit.atum.model.AbstractFiltersHolder;
import ru.spbau.mit.atum.model.UserDefinedTask;
import ru.spbau.mit.atum.model.UserSynchronisableData;

public class TaskListActivity extends AbstractFiltersHolderListActivity<UserDefinedTask> {
    @Override
    protected void setTitleText() {
        title.setText(R.string.task_list);
    }

    @Override
    protected void initializeHolder() {
        filtersHolders = UserSynchronisableData.getInstance().getTasks();
    }

    @Override
    protected String getScheduleStatus(AbstractFiltersHolder task) {
        UserDefinedTask userDefinedTask = (UserDefinedTask)task;
        if (userDefinedTask.getScheduledTime() == null) {
            return "not scheduled";
        } else {
            return "OK";
        }
    }

    @Override
    protected String getTaskType(AbstractFiltersHolder task) {
        UserDefinedTask userDefinedTask = (UserDefinedTask)task;
        if (userDefinedTask.getType() == UserDefinedTask.Type.GENERAL) {
            return "GENERAL TASK";
        } else if (userDefinedTask.getType() == UserDefinedTask.Type.FIXED) {
            return "FIXED TASK";
        } else {
            return "ADDITIONAL TASK";
        }
    }

    @Override
    protected boolean getScheduleStatusBool(AbstractFiltersHolder task) {
        return ((UserDefinedTask)task).getScheduledTime() != null;
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
