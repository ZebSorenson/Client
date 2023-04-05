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

    public RegisterRequest request;
    public Handler handler;
    public RegisterResult registerResults;

    public RegisterTask(Handler handler, RegisterRequest request) {
        this.handler = handler;
        this.request = request;
    }


    @Override
    public void run() {
        ServerProxy proxy = new ServerProxy(); //want to add in the port and host number here

        registerResults = proxy.register(request);

        if (registerResults.getSuccess()) {

            //Update the dataCache Here!!

            // Person firstChild = dataCache.getPersonByID(registerResults.getPersonID());

            //need a better way of getting the first and last name

            PersonIDResult firstChild = proxy.getSinglePerson(registerResults.getPersonID(), registerResults.getAuthtoken());

            //make the change here to get the person!!!

            Bundle bundle = new Bundle();
            Message message = Message.obtain();
            bundle.putBoolean(SUCCESS, true);
            bundle.putString(FIRSTNAME, firstChild.getFirstName());
            bundle.putString(LASTNAME, firstChild.getLastName());
            message.setData(bundle);


            PersonResult allPeople = proxy.getAllPersons(registerResults.getAuthtoken());

            dataCache.addFamilyMembers(allPeople);

            EventResult allEvents = proxy.getAllEvents(registerResults.getAuthtoken());

            dataCache.addEventsCache(allEvents);
            handler.sendMessage(message);
        } else {
            Bundle bundle = new Bundle();
            Message message = Message.obtain();
            bundle.putBoolean(SUCCESS, false);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
