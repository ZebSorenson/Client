package BackendLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import RequestResult.EventResult;
import RequestResult.PersonIDResult;
import RequestResult.PersonResult;
import model.Event;
import model.Person;

public class DataCache {

    PersonIDResult firstChildPerson = null;

    public PersonIDResult getFirstChildPerson() {
        return firstChildPerson;
    }

    public void setFirstChildPerson(PersonIDResult firstChildPerson) {
        this.firstChildPerson = firstChildPerson;
    }

    ArrayList<Person> personArrayList = new ArrayList<>();

    ArrayList<Event> eventArrayList = new ArrayList<>();

    public ArrayList<Person> getPersonArrayList() {
        return personArrayList;
    }

    public ArrayList<Event> getEventArrayList() {
        return eventArrayList;
    }

    private static DataCache instance;

    public static DataCache getInstance(){

        if(instance==null){

            instance = new DataCache();
        }
        return instance;
    }

    private DataCache(){

    }

    public ArrayList<Person> getRelatedPeople(String personID) {
        ArrayList<Person> relatedPeople = new ArrayList<>();
        Person currentPerson = getPersonByID(personID);

        if (currentPerson != null) {
            // Add father if exists
            if (currentPerson.getFatherID() != null) {
                relatedPeople.add(getPersonByID(currentPerson.getFatherID()));
            }
            // Add mother if exists
            if (currentPerson.getMotherID() != null) {
                relatedPeople.add(getPersonByID(currentPerson.getMotherID()));
            }
            // Add spouse if exists
            if (currentPerson.getSpouseID() != null) {
                relatedPeople.add(getPersonByID(currentPerson.getSpouseID()));
            }
            // Add children
            for (Person person : personArrayList) {
                if (person.getFatherID() != null && person.getFatherID().equalsIgnoreCase(personID) ||
                        person.getMotherID() != null && person.getMotherID().equalsIgnoreCase(personID)) {
                    relatedPeople.add(person);
                }
            }
        }

        return relatedPeople;
    }


    public ArrayList<Event> getSortedEventsBasedOnPersonID(String personID) { // this will give us an ArrayList of Events in chronological order

        ArrayList<Event> eventsOfPerson = new ArrayList<>();

        for (Event event : eventArrayList) {

            if (event.getPersonID().equalsIgnoreCase(personID)) {

                eventsOfPerson.add(event);
            }
        }

        // Sort the eventsOfPerson based on each event's year so that the events are in chronological order
        Collections.sort(eventsOfPerson, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {

                return Integer.compare(event1.getYear(), event2.getYear());
            }
        });

        return eventsOfPerson;
    }


    public void addFamilyMembers(PersonResult result){ //adds family members to a simple array since our server uses that. We then add it to our arrayList

        Person[] personArray = null;

        personArray = (Person[])result.getData(); //every person in the database, whether or not related.

        personArrayList.addAll(Arrays.asList(personArray)); //add the people to an ArrayList because they are nicer :)

        personArrayList.size();

        System.out.println(personArrayList.size() + " person were added to the datacache"); //used to make sure we're adding the right number of people

    }

    public Person getPersonByID(String personID){ //returns a person object based on ID (which we can get out of an Event object)

        for(Person person: personArrayList){

            if(person.getPersonID().equals(personID)){

                return person;
            }
        }

        return null;
    }

    public void addEventsCache(EventResult result){ //same as the add Persons function, just for events

        Event[] eventArray = null;

        eventArray = (Event[]) result.getData();

        eventArrayList.addAll(Arrays.asList(eventArray));

        System.out.println(eventArray.length+" Events were added to the datacache");
    }

    public Person getPersonByPersonID(String personID){

        for(Person person: personArrayList){
            if(person.getPersonID().equals(personID)){
                return person;
            }
        }

        return null;
    }

    //end of class
}


