package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER;
import static ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION;

public class AdditionalTaskEditorActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_task_editor);

        name = (EditText) findViewById(R.id.additional_task_name);
        description = (EditText) findViewById(R.id.additional_task_descripion);

    }

    public void onButtonApplyClick(View view) {

        Intent intent = new Intent();

        UserDefinedTask additionalTask = UserDefinedTask.newQuickieTask(name.getText().toString(),
                description.getText().toString(), null);

        intent.putExtra(EXTRA_FILTER_HOLDER, additionalTask);

        int position = getIntent().getIntExtra(EXTRA_FILTER_HOLDER_POSITION, -1);
        if (position >= 0) {
            intent.putExtra(EXTRA_FILTER_HOLDER_POSITION, position);
        }

        setResult(RESULT_OK, intent);
        finish();

    }

}
