package Logic;

import java.util.ArrayList;

import model.Event;
import model.Person;

public class DataCache {

    ArrayList<Person> people; //or should this be just a List?

    ArrayList<Event> events;

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
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
}
