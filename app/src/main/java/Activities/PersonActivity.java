package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zebfamilymap.R;

import BackendLogic.DataCache;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT_ID = "com.example.zebfamilymap.event_id";

    private static Event mEvent;

    public static void start(Context context, Event event) {
        mEvent = event;
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, event.getEventID());
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);

        // Get the event ID from the intent extras
        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);

        // Find the corresponding event from the data cache
        //mEvent = DataCache.getInstance().getEventArrayList().get(eventId);

        // Set up the UI elements with the event information
        TextView titleTextView = findViewById(R.id.personName);
        titleTextView.setText(mEvent.getEventType() + ": " + mEvent.getCity() + ", " + mEvent.getCountry() + " (" + mEvent.getYear() + ")");

        TextView personNameTextView = findViewById(R.id.personName);
        Person person = DataCache.getInstance().getPersonByPersonID(mEvent.getPersonID());
        String personName = person.getFirstName() + " " + person.getLastName();
        personNameTextView.setText(personName);
    }
}
