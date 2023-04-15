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
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.PersonIDResult;
import RequestResult.PersonResult;


 public class LoginTask implements Runnable{


     //make a constructor to pass in items



    public LoginRequest requestObject;

    public Handler handlerObject;

    public String host;

    public String port;
    //our onclick listener is giving us our request
    //this will make this request and send it to the server and get back the result

    public LoginTask(Handler handler, LoginRequest request, String host, String port) {

        this.handlerObject = handler;
        this.requestObject = request;
        this.host = host;
        this.port = port;

    }

    @Override
    public void run() {
        //want to create proxy server

        ServerProxy proxyServer = new ServerProxy(host, port); // can set up to take in the host and port number

        //YOU COULD SET THE PORT AND HOST RIGHT HERE SINCE YOU'VE ALREADY GABBED THE INFO ABOVE


        LoginResult result = proxyServer.login(requestObject);


        if(result.getSuccess()){
            //call the update datacache function
            //update the datacache
            //from the datacache you can get the base person
            //create a bundle and a message that will be used in the on click listener
            //bundle is a collection of data that you put in the message and then use the handler to send the message.

            //get your base person for the first and last name

            PersonIDResult firstChild = proxyServer.getSinglePerson(result.getPersonID(), result.getAuthtoken());

            dataCache.setFirstChildPerson(firstChild); //set the first child in the datacache

            System.out.println("The user logged in was "+ result.getUsername());
            Bundle bundle = new Bundle();
            Message message = Message.obtain();
            bundle.putBoolean(SUCCESS, true);
            bundle.putString(FIRSTNAME, firstChild.getFirstName()); //need to get first and last name of person
            bundle.putString(LASTNAME, firstChild.getLastName());
            message.setData(bundle);


            PersonResult allPeople = proxyServer.getAllPersons(result.getAuthtoken());

            dataCache.addFamilyMembers(allPeople);

            EventResult allEvents = proxyServer.getAllEvents(result.getAuthtoken());

            dataCache.addEventsCache(allEvents);
            handlerObject.sendMessage(message);


        }else{
            //bad login
            //new bundle
            //send a false value
            Bundle bundle = new Bundle();
            Message message = Message.obtain();
            bundle.putBoolean(SUCCESS, false);
            message.setData(bundle);
            handlerObject.sendMessage(message);
            System.out.println("An invalid username or password was given");
        }


    }
}
