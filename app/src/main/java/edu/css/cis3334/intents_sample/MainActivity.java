package edu.css.cis3334.intents_sample;

import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity {

    private Spinner spinner;
    // constants for tracking results from intent calls
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);                                 // link the spinner with the layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,                    // set up a adapter to link the intents array in strings.xml
                R.array.intents, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);                                                    // link the adapter to the spinner

        // set up the listener for when the selection from the spinner is made
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                callIntent(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing here
            }

        });

    }

    private void callIntent(Integer position) {
        switch (position) {
            case 1:
                Uri mylink = Uri.parse("http://www.css.edu");
                intent = new Intent(Intent.ACTION_VIEW, mylink);
                break;
            case 2:
                intent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:(218)7236294"));
                break;
            case 3:
                intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:(218)7236294"));
                break;
            case 4:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:46.907005,-92.201879?z=19"));
                break;
            case 5:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=query"));
                break;
            case 6:
                // Take a photo with the camera and return it
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.withAppendedPath(mLocationForPhotos, targetFilename));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);      // REQUEST_IMAGE_CAPTURE declared as a constant above
                break;
            case 7:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("content://contacts/people/"));
                break;
            case 8:
                intent = new Intent(Intent.ACTION_EDIT,
                        Uri.parse("content://contacts/people/1"));
                break;
            case 9:
                //alarm clock
                Intent intent9 = new Intent(AlarmClock.ACTION_SET_ALARM)
                        .putExtra(AlarmClock.EXTRA_MESSAGE, "Go to class")
                        .putExtra(AlarmClock.EXTRA_HOUR, 10)
                        .putExtra(AlarmClock.EXTRA_MINUTES, 30);
//                Intent intent9 = new Intent(AlarmClock.ACTION_SET_ALARM);
//                intent9.putExtra(AlarmClock.EXTRA_MESSAGE, "Go to class");
//                intent9.putExtra(AlarmClock.EXTRA_HOUR, 10);
//                intent9.putExtra(AlarmClock.EXTRA_MINUTES, 30);
                if (intent9.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent9);
                }

                intent = new Intent(Intent.ACTION_EDIT,
                        Uri.parse("content://contacts/people/1"));
                break;
        }

    }

}
