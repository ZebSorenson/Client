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

    public void updateFamilyCache(String auth, ServerProxy serverProxy){


        //how do we deal with clearing the data?
       // Person[] personArray = (Person[])serverProxy.getPersonsByID(auth);
    }
}

//to do in the datacache

//make an update cache method that will clear all of the people and use the proxy and get all the relatives and events

//in the update datacache function, will be called in the login fragment

//update data will clear the cache and set our arrays that hold events and people
//make server proxy and call get people and get events, and use that to update your cache.
//this way you will have your base person with their relatives and events and arrays

//Use the proxy server to get all the relatives that just logged in.


