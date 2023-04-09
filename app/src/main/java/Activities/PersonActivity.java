package Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zebfamilymap.R;

import java.util.ArrayList;

import BackendLogic.DataCache;
import model.Event;
import model.Person;



public class PersonActivity extends AppCompatActivity {

    public static DataCache dataCache = DataCache.getInstance();

    private static final String EXTRA_EVENT_ID = "com.example.zebfamilymap.event_id";

    private static Event mEvent;

    public static void start(Context context, Event event) {
        mEvent = event;
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, event.getEventID());
        context.startActivity(intent);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);

        Person personToDisplay = dataCache.getPersonByID(mEvent.getPersonID());

        //set the TextView personName to the person's name
        TextView personName = findViewById(R.id.personName);


        String personGender;
        if(personToDisplay.getGender().equalsIgnoreCase("m")){
             personGender = "Male";
        }else{
              personGender ="Female";
        }

//        personName.setText("First Name: "+personToDisplay.getFirstName() + "\n"
//                +"Last Name: " +personToDisplay.getLastName() + "\n"
//
//                +"Gender: "+ personGender);

        TextView first_Name = findViewById(R.id.first_nameID);

        first_Name.setText("First Name: "+personToDisplay.getFirstName());

        TextView last_Name = findViewById(R.id.last_nameID);

        last_Name.setText("Last Name: "+personToDisplay.getLastName());

        TextView gender = findViewById(R.id.person_gender);

        gender.setText("Gender: "+personGender);

        ExpandableListView eventInfo = findViewById(R.id.personActivity);

        ArrayList<Event> userEvents = dataCache.getEventArrayList();






        // Get the event ID from the intent extras
//        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
//
//        // Find the corresponding event from the data cache
//        //mEvent = DataCache.getInstance().getEventArrayList().get(eventId);
//
//        // Set up the UI elements with the event information
//        @SuppressLint("CutPasteId") TextView titleTextView = findViewById(R.id.personName);
//        titleTextView.setText(mEvent.getEventType() + ": " + mEvent.getCity() + ", " + mEvent.getCountry() + " (" + mEvent.getYear() + ")");
//
//        @SuppressLint("CutPasteId") TextView personNameTextView = findViewById(R.id.personName);
//        Person person = DataCache.getInstance().getPersonByPersonID(mEvent.getPersonID());
//        String personName = person.getFirstName() + " " + person.getLastName();
//        personNameTextView.setText(personName);
    }
}
