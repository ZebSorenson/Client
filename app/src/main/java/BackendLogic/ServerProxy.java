package BackendLogic;
import java.io.*;
import java.net.*;

import RequestResult.EventResult;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.PersonIDResult;
import RequestResult.PersonResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;

import com.google.gson.Gson;

/*
	The Client class shows how to call a web API operation from
	a Java program.  This is typical of how your Android client
	app will call the web API operations of your server.
*/
public class ServerProxy {

   private static String port;

   private static String host;

   //A ServerProxy constructor that takes in the host and port number as strings and sets them to the private variables
    public ServerProxy(String host, String port){ //this is how we will now be able to accept the host and port instead of hard coding it

         this.host = host;

         this.port = port;

    }

    private static String get(String AuthToken, String functionName) { //will handle get methods with the server

        String get_String = null;

        try {

            URL url = new URL("http://" + host + ":" + port + functionName);


            HttpURLConnection http = (HttpURLConnection) url.openConnection();


            http.setRequestMethod("GET");


            http.setDoOutput(false);


            http.addRequestProperty("Authorization", AuthToken); //pass in our Auth token to make sure we can complete the request


            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                get_String = readString(respBody);
            }
        } catch (IOException e) {

            e.printStackTrace(); //we can see what kind of error we're getting if we run into an issue when having a GET request
        }

        return get_String;
    }

    private static String post(String requestString, String functionCall) { //almost the exact same logic as GET but for post requests


        try {

            URL url = new URL("http://" + host + ":" + port + functionCall); //create our URL we'll use

            //192.168.5.200 the mcdonalds IP lol...keeping this comment for the memory of going crazy trying to use Mcdonald's wifi and get this to work haha

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST"); //distinguish what kind of request we're doing

            http.setDoOutput(true);

            http.connect(); //make the connection!

            OutputStream request_String = http.getOutputStream();

            writeString(requestString, request_String); //write string

            request_String.close(); //after the request is made we need to close it


            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();

                String respData = readString(respBody); //get the needed data out of the string

                return respData;
            }
            else {

                return null; //anything that isn't a good execution will return a null object
            }
        }
        catch (IOException e) {e.printStackTrace();} //for debugging. Get the needed error to print out

        return null; //if we've somehow made it here, something weird has happened and we need to return null
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException { //example code given from class
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {//example code given from class
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    //login function that returns a login result and takes in a loginRequest object
    //this will be called from within our run function

    public LoginResult login(LoginRequest loginRequestParam){

        String login_request_dataString = "{\"username\":\"" + loginRequestParam.getUsername() + "\", \"password\":\"" + loginRequestParam.getPassword() + "\"}";

        //should be able to get register from specs

       String responseData = post(login_request_dataString,"/user/login" );

       //do some error checking here

        if(responseData == null){

            LoginResult result = new LoginResult();

            result.setMessage("Error logging in");

            result.setSuccess(false);

            return result;
        }
        //possibly do more error checking here

        //use gson to take in response data
        Gson gson = new Gson();

        return gson.fromJson(responseData, LoginResult.class);
    }

    public PersonIDResult getSinglePerson(String personID, String token){

       String responseData = get(token,"/person/"+personID);

        if(responseData == null){

            PersonIDResult person_result = new PersonIDResult();

            person_result.setMessage("Error grabbing person info");

            person_result.setSuccess(false);

            return person_result;
        }

        Gson gson = new Gson();

        return gson.fromJson(responseData, PersonIDResult.class);

    }

    public PersonResult getAllPersons(String authToken){

        String peopleList = get(authToken, "/person");

        Gson gson = new Gson();

        return gson.fromJson(peopleList, PersonResult.class);
    }

    public EventResult getAllEvents(String authToken){

            String eventList = get(authToken, "/event");

            Gson gson = new Gson();

        return gson.fromJson(eventList, EventResult.class);
    }

    public RegisterResult register(RegisterRequest register_requestParam) {

        System.out.println("Entered into register proxy function");

        String register_Request_dataString =

                "{\"username\":\"" + register_requestParam.getUsername() + "\", \"password\":\""+register_requestParam.getPassword() + "\", \"email\":\""

                +register_requestParam.getEmail() + "\", \"firstName\":\""+register_requestParam.getFirstName() +

                "\", \"lastName\":\""+register_requestParam.getLastName() +

                "\", \"gender\":\""+register_requestParam.getGender() +

                "\"}"; //dang that is a long string

        String resister_responseString = post(register_Request_dataString, "/user/register");

        if (resister_responseString == null) {
            //bad object!
            RegisterResult badResult = new RegisterResult();

            badResult.setSuccess(false);

            badResult.setMessage("Error registering user");

            System.out.println("There was an issue registering the user");

            return badResult;
        }

        Gson gson = new Gson();

        return gson.fromJson(resister_responseString, RegisterResult.class);
    }

//end of class
}

