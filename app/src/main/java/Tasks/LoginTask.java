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
        //want to create proxy server object and give it the given host and port numbers to make the conenction

        ServerProxy proxyServer = new ServerProxy(host, port);

        LoginResult result = proxyServer.login(requestObject); //our result object will be what we get back from sending our request object to the login API of the server
        //so we both set the result and make the call right here


        if(result.getSuccess()){ //this means the login was successful and we can move onto our next steps

            PersonIDResult firstChild = proxyServer.getSinglePerson(result.getPersonID(), result.getAuthtoken()); //this is going to be the person logging in

            dataCache.setFirstChildPerson(firstChild); //set the first child in the datacache

            System.out.println("The user logged in was "+ result.getUsername());

            Bundle goodBundle = new Bundle();

            Message message = Message.obtain();

            goodBundle.putBoolean(SUCCESS, true); //indicate we had a good login

            goodBundle.putString(FIRSTNAME, firstChild.getFirstName()); //need to get first and last name of person

            goodBundle.putString(LASTNAME, firstChild.getLastName());

            message.setData(goodBundle);

            PersonResult allPeople = proxyServer.getAllPersons(result.getAuthtoken()); //now we take care of updating the datacache with the logged in user's family members with the API
            //the above code just gets the people back from the API call

            dataCache.addFamilyMembers(allPeople); //now this actually handles adding it to an ArrayList in our datacache

            EventResult allEvents = proxyServer.getAllEvents(result.getAuthtoken()); //the next 3 lines of code are the same logic as above just for events instead of family memebers

            dataCache.addEventsCache(allEvents);

            handlerObject.sendMessage(message);


        }else{
            //bad login
            //new bundle
            //set the success to false
            Bundle badBundle = new Bundle();

            Message message = Message.obtain();

            badBundle.putBoolean(SUCCESS, false); //indicate we had a bad login

            message.setData(badBundle);

            handlerObject.sendMessage(message);

            System.out.println("An invalid username or password was given");
        }

    }
    //end of run method
}
