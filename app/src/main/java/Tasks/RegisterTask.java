package Tasks;

import static com.example.zebfamilymap.LoginFragment.FIRSTNAME;
import static com.example.zebfamilymap.LoginFragment.LASTNAME;
import static com.example.zebfamilymap.LoginFragment.SUCCESS;
import static com.example.zebfamilymap.LoginFragment.dataCache;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import BackendLogic.ServerProxy;
import RequestResult.EventResult;
import RequestResult.PersonIDResult;
import RequestResult.PersonResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;

public class RegisterTask implements Runnable {

    public RegisterRequest registerRequestObject;
    public Handler handlerObject;
    public RegisterResult registerResultObject;

    public String host;
    public String port;

    public RegisterTask(Handler handlerObject, RegisterRequest registerRequestObject, String host, String port) {

        this.handlerObject = handlerObject;
        this.registerRequestObject = registerRequestObject;
        this.host = host;
        this.port = port;
    }


    @Override
    public void run() {
        ServerProxy proxy = new ServerProxy(host, port); //passing in the host and port number in order to make the connection with the server

        registerResultObject = proxy.register(registerRequestObject); //same as with login. Our result object comes from calling the register API with our request object
        //so we both set it and make the call all on one line of code

        if (registerResultObject.getSuccess()) {//we were able to successfully registered the new user

            PersonIDResult firstChild = proxy.getSinglePerson(registerResultObject.getPersonID(), registerResultObject.getAuthtoken()); //we get the newly registered person

            Bundle goodBundle = new Bundle();

            Message message = Message.obtain();

            goodBundle.putBoolean(SUCCESS, true); //indicate we had a good register

            goodBundle.putString(FIRSTNAME, firstChild.getFirstName());

            goodBundle.putString(LASTNAME, firstChild.getLastName());

            message.setData(goodBundle);

            PersonResult allPeople = proxy.getAllPersons(registerResultObject.getAuthtoken()); //get an array of relatives just like in login

            dataCache.addFamilyMembers(allPeople); //add those people to the datacache

            EventResult allEvents = proxy.getAllEvents(registerResultObject.getAuthtoken());

            dataCache.addEventsCache(allEvents);

            handlerObject.sendMessage(message);
        } else { //anything at all that is not a good login response will get taken care of here.

            Bundle badBundle = new Bundle();

            Message message = Message.obtain();

            badBundle.putBoolean(SUCCESS, false);//indicate we had a bad register

            message.setData(badBundle);

            handlerObject.sendMessage(message);
        }
    }
}
