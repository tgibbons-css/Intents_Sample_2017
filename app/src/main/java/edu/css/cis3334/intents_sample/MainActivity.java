package edu.css.cis3334.intents_sample;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity {

    private Spinner spinner;
    private ImageView imageView;
    private TextView textView;
    // constants for tracking results from intent calls
    static final int CIS3334_IMAGE_CAPTURE = 1001;
    static final int CIS3334_SELECT_CONTACT = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
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
                // ======= open a web page in a browser
                Uri webpage = Uri.parse("http://www.css.edu");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
                break;
            case 2:
                // ======= call someone
                // need to add permission the manifests/AndroidManifest.xml file
                //<uses-permission android:name="android.permission.CALL_PHONE" />
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + "(218)7236294"));
                if (phoneIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(phoneIntent);
                }
                break;
            case 3:
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:2183911863"));
                sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
                startActivity(sendIntent);
                break;
            case 4:
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                // specify the latitude and longitude, plus the zoom level
                mapIntent.setData(Uri.parse("geo:46.907005,-92.201879?z=19"));
                // specify the address
                //mapIntent.setData(Uri.parse("geo:0,0?q=1200+Kenwood+Ave+Duluth+MN"));
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                break;
            case 5:
                Intent mapIntent2 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=query"));
                if (mapIntent2.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent2);
                }
                break;
            case 6:
                // ======= Take a photo with the camera
                // need to add permission the manifests/AndroidManifest.xml file
                //    <uses-feature android:name="android.hardware.camera"  android:required="true" />
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // optionally set the where to store the file
                // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.withAppendedPath(mLocationForPhotos, targetFilename));
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CIS3334_IMAGE_CAPTURE);                 // CIS3334_IMAGE_CAPTURE declared as a constant above
                }
                break;
            case 7:
                Intent contactIntent = new Intent(Intent.ACTION_PICK);
                contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (contactIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(contactIntent, CIS3334_SELECT_CONTACT);
                }
                break;
            case 8:
                // Set a calendar event
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, "SAL Colloquium 'The Robot Next Door'");
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Tower Hall 4119");
                GregorianCalendar begDate = new GregorianCalendar(2017, 2, 24, 15, 40);
                GregorianCalendar endDate = new GregorianCalendar(2017, 2, 24, 16, 40);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begDate.getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate.getTimeInMillis());
                if (calIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(calIntent);
                }
                break;
            case 9:
                //alarm clock
                Intent intent9 = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent9.putExtra(AlarmClock.EXTRA_MESSAGE, "Go to class");
                intent9.putExtra(AlarmClock.EXTRA_HOUR, 10);
                intent9.putExtra(AlarmClock.EXTRA_MINUTES, 30);
                if (intent9.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent9);
                }
                break;
        }

    }

    // process any data returned from the intents
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get photo returned from camera
        if (requestCode == CIS3334_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bitmap thumbnail = data.getParcelable("data");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        // Get the contact info returned
        if (requestCode == CIS3334_SELECT_CONTACT && resultCode == RESULT_OK) {
            //Bitmap thumbnail = data.getParcelable("data");
            Uri uriContact = data.getData();
            textView.setText("Contact Name: " + retrieveContactName(uriContact));
        }

    }


    private String retrieveContactName(Uri uriContact) {
        String contactName = null;
        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return contactName;

    }

}
