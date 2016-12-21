package ru.spbau.mit.atum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER;
import static ru.spbau.mit.atum.AbstractFiltersHolderEditorActivity.EXTRA_FILTER_HOLDER_POSITION;
import static ru.spbau.mit.atum.PlacesUtils.setBuilderPositionNearPlace;

public class AdditionalTaskEditorActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 0;

    private EditText name;
    private EditText description;
    private TextView placeTextView;

    private Place chosenPlace = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_task_editor);

        name = (EditText) findViewById(R.id.additional_task_name);
        description = (EditText) findViewById(R.id.additional_task_descripion);
        placeTextView = (TextView) findViewById(R.id.additional_task_location_text_view);
    }

    public void onButtonApplyClick(View view) {

        Intent intent = new Intent();

        UserDefinedTask additionalTask = UserDefinedTask.newQuickieTask(name.getText().toString(),
                description.getText().toString(), chosenPlace);

        intent.putExtra(EXTRA_FILTER_HOLDER, additionalTask);

        int position = getIntent().getIntExtra(EXTRA_FILTER_HOLDER_POSITION, -1);
        if (position >= 0) {
            intent.putExtra(EXTRA_FILTER_HOLDER_POSITION, position);
        }

        setResult(RESULT_OK, intent);
        finish();

    }

    public void onClickLocationButton(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        if (chosenPlace != null) {
            setBuilderPositionNearPlace(builder, chosenPlace);
        }

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException |
                GooglePlayServicesRepairableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    chosenPlace = PlacePicker.getPlace(this, data);
                    placeTextView.setText(chosenPlace.getAddress());
                }

                break;

            default:
                break;
        }
    }
}
