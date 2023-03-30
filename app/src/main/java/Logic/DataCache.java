package Logic;

import java.util.ArrayList;
import java.util.Arrays;

import RequestResult.EventResult;
import RequestResult.PersonResult;
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



    public void addFamilyMembers(PersonResult result){

        //Currently using a person Array...Would be better to create a datamember as an
        //arrayList...could loop through the array and just add everybody to the arrayList

        Person[] personArray = null;

        //how do we deal with clearing the data?
         personArray = (Person[])result.getData();
        //every person in the database, whether or not related.

       // people.addAll(Arrays.asList(personArray)); //adding everyone to the ArrayList bcuz easier





        System.out.println(personArray.length + " person were added to the datacache");


    }

    public Person getPersonByID(String personID){

        for(Person person: people){
            if(person.getPersonID().equals(personID)){
                return person;
            }
        }

        return null;
    }

    public void addEventsCache(EventResult result){

        Event[] eventArray = null;

        eventArray = (Event[]) result.getData();

        System.out.println(eventArray.length+" Events were added to the datacache");
    }


    //end of class
}

//to do in the datacache

//make an update cache method that will clear all of the people and use the proxy and get all the relatives and events

//in the update datacache function, will be called in the login fragment

//update data will clear the cache and set our arrays that hold events and people
//make server proxy and call get people and get events, and use that to update your cache.
//this way you will have your base person with their relatives and events and arrays

//Use the proxy server to get all the relatives that just logged in.


