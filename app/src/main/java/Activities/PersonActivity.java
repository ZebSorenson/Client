package Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zebfamilymap.R;

import java.util.ArrayList;
import java.util.List;

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


        //expandable list view code below

        ExpandableListView expandableListView = findViewById(R.id.personActivity); //double check this personActivity marker

        List<Event> eventList = dataCache.getEventArrayList();

        List<Event> sortedEventList = dataCache.getSortedEventsBasedOnPersonID(mEvent.getPersonID()); //this should be a sorted list of events for the person given

        List<Person>  personList = dataCache.getPersonArrayList();

        //event list and then personList

        expandableListView.setAdapter(new EventAndPersonAdapter(sortedEventList, personList));



        //expandable list view code above


        Person personToDisplay = dataCache.getPersonByID(mEvent.getPersonID()); // the current person we are working with

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


    private class EventAndPersonAdapter extends BaseExpandableListAdapter{

        private static final  int EVENT_GROUP_POSITION = 0;

        private static final int PERSON_GROUP_POSITION = 1;

        private final List<Event>  eventList;

        private final List<Person> personList;

        private EventAndPersonAdapter(List<Event> eventList, List<Person> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return eventList.size();
                case PERSON_GROUP_POSITION:
                    return personList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            //this will not be used
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            //this will not be used
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.event_single_item, parent, false); //DOUBLE CHECK THIS WHAT LAYOUT DOES THIS NEED TO BE?!!!!!!
            }

            TextView titleView = convertView.findViewById(R.id.eventDetailsXMLID); //DOUBLE CHECK THIS?? WHAT IS THE LAYOUT SUPPOSED TO BE????

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.listTitle); //WHAT THE HECK IS THIS???? (R.string.XXX)
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.personTitleString);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_list_item, parent, false); //dobule check this against what is in the example
                    initializePersonAndEventView(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_list_item, parent, false); //make this relatable to a person
                    initializePersonAndEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializePersonAndEventView(View skiResortItemView, final int childPosition) {
            TextView resortNameView = skiResortItemView.findViewById(R.id.eventDetailsXMLID);
            resortNameView.setText(eventList.get(childPosition).getEventType());


            //is this where we are going to set the images??

            TextView resortLocationView = skiResortItemView.findViewById(R.id.personGenderTextXML);
            resortLocationView.setText(personList.get(childPosition).getGender());

            skiResortItemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StringFormatInvalid")
                @Override
                public void onClick(View v) {
                    Toast.makeText(PersonActivity.this, getString(R.string.event_Person_Gender, personList.get(childPosition).getGender()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }







    //end of the class
}
