package Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zebfamilymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import BackendLogic.DataCache;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    public static DataCache dataCache = DataCache.getInstance();

    private static final String EXTRA_EVENT_ID = "com.example.zebfamilymap.event_id";

    private static Event selectedEvent;

    public static void start(Context context, Event event) {

        selectedEvent = event;

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

        List<Event> sortedEventList = dataCache.getSortedEventsBasedOnPersonID(selectedEvent.getPersonID()); //this should be a sorted list of events for the person given

        List<Person> personList = dataCache.sortedFamilyMembers(selectedEvent.getPersonID()); //this will give us our family members in correct order


        expandableListView.setAdapter(new EventAndPersonAdapter(sortedEventList, personList));

        //expandable list view code above

        Person personToDisplay = dataCache.getPersonByID(selectedEvent.getPersonID()); // the current person we are working with

        //set the TextView personName to the person's name
        TextView personName = findViewById(R.id.personName);

        String personGender;

        if(personToDisplay.getGender().equalsIgnoreCase("m")){

            personGender = "Male";
        }else{

            personGender ="Female";
        }

        //set the items on the screen in relation to the person we are working with

        TextView first_Name = findViewById(R.id.first_nameID);

        first_Name.setText("First Name: "+personToDisplay.getFirstName());

        TextView last_Name = findViewById(R.id.last_nameID);

        last_Name.setText("Last Name: "+personToDisplay.getLastName());

        TextView gender = findViewById(R.id.person_gender);

        gender.setText("Gender: "+personGender);

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

            switch(groupPosition){
                case EVENT_GROUP_POSITION:
                    return eventList.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return personList.get(childPosition);
            }
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
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { //make sure your are using the correct layouts!

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.event_list_item, parent, false);
            }

            TextView title = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    title.setText(R.string.eventTitleString); //WHAT THE HECK IS THIS???? (R.string.XXX)
                    break;
                case PERSON_GROUP_POSITION:
                    title.setText(R.string.personTitleString);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            View singleItemView;

            switch(groupPosition) {
                case EVENT_GROUP_POSITION:

                    singleItemView = getLayoutInflater().inflate(R.layout.event_single_item, parent, false); //double check this against what is in the ski example

                    startEventView(singleItemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:

                    singleItemView = getLayoutInflater().inflate(R.layout.person_list_item, parent, false); //make this relatable to a person

                    startPersonView(singleItemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return singleItemView;
        }

        private void startEventView(View eventItemView, final int childPosition) {

            TextView eventTextView = eventItemView.findViewById(R.id.eventDetailsXMLID);

            ImageView eventImageView = eventItemView.findViewById(R.id.eventDetailsPhotoID);

            String eventInfo = eventList.get(childPosition).getEventType()+" "+ eventList.get(childPosition).getCity()+ " "+eventList.get(childPosition).getCountry()+" " + eventList.get(childPosition).getYear();

            eventTextView.setText(eventInfo);

            Drawable eventMarker = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.byu_blue).sizeDp(35);

            eventImageView.setImageDrawable(eventMarker);

            eventItemView.setOnClickListener(view ->{ //this is what you will use to go to a diff event

                Toast.makeText(PersonActivity.this, "text listener", Toast.LENGTH_SHORT).show();

            });
        }

        private void startPersonView(View eventItemView, final int childPosition) {

            TextView personName = eventItemView.findViewById(R.id.personGenderTextXML);

            ImageView personImageView = eventItemView.findViewById(R.id.personGenderImageXML);

            String relationshipToPerson = getRelationship(personList.get(childPosition).getPersonID());

            String personInfoString = personList.get(childPosition).getFirstName() + " " + personList.get(childPosition).getLastName() + ": " + relationshipToPerson ;

            personName.setText(personInfoString);



            if(personList.get(childPosition).getGender().equalsIgnoreCase("m")){

                Drawable maleIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.byu_blue).sizeDp(35);

                personImageView.setImageDrawable(maleIcon);
            }else if(personList.get(childPosition).getGender().equalsIgnoreCase("f")){

                Drawable femaleIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.pretty_pink).sizeDp(35);

                personImageView.setImageDrawable(femaleIcon);
            }


            eventItemView.setOnClickListener(view ->{ //this is what you will use to go to a diff event

                Toast.makeText(PersonActivity.this, "text listener", Toast.LENGTH_SHORT).show();

            });
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String getRelationship(String personID) { //with the person's ID we can check to see what kind of relationship we are looking at

        String relationship = "";

        Person person = dataCache.getPersonByID(selectedEvent.getPersonID());

        if (person.getFatherID() != null && person.getFatherID().equals(personID)) {

            relationship = "Father";
        } else if (person.getMotherID() != null && person.getMotherID().equals(personID)) {

            relationship = "Mother";
        } else if (person.getSpouseID() != null && person.getSpouseID().equals(personID)) {

            relationship = "Spouse";
        } else {
            relationship = "Child"; //anything that isn't Father, Mother, or Spouse is going to be a child
        }

        return relationship;
    }

    //end of the class
}