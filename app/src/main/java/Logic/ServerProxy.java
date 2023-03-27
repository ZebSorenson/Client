package Logic;
import java.io.*;
import java.net.*;

import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.PersonIDResult;

import com.google.gson.Gson;

/*
	The Client class shows how to call a web API operation from
	a Java program.  This is typical of how your Android client
	app will call the web API operations of your server.
*/
public class ServerProxy {

   private String port;

   private String host;



    // The client's "main" method.
    // The "args" parameter should contain two command-line arguments:
    // 1. The IP address or domain name of the machine running the server
    // 2. The port number on which the server is accepting client connections


    //for GET, the parameters will be an authtoken and a name.
    //will be making a url that goes to the host and port but is going to the name of the person
    //similar to addrequest property authorization and pass in the authtoken
    //shouldn't need to use get until later in the project when you get all the people from the DB

    //will follow same process for register

    private static String get(String AuthToken, String functionName) { //WILL BECOME THE GET FUNCTION
        //function name will be /person or /events depending on if getting events or getting people

        // This method shows how to send a GET request to a server

        String result = null;

        try {
            URL url = new URL("http://" + "10.0.2.2" + ":" + "8080" + functionName);


            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");

            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", AuthToken);

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                result = readString(respBody);
            } else {
                //System.out.println("serverGet ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // The claimRoute method calls the server's "/routes/claim" operation to
    // claim the route between Atlanta and Miami
    private static String post(String requestString, String functionCall) {

        //take in a json string

        //need login function
        //register function
        //post and get

        // This method shows how to send a POST request to a server
//        System.out.println("\n");
//        System.out.println("Printing out the request data");
//        System.out.println(requestString);

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + "10.0.2.2" + ":" + "8080" + functionCall);

            //192.168.5.200 the mcdonalds IP lol
            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();


            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);	// There is a request body


            // Connect to the server and send the HTTP request
            http.connect();

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body

            writeString(requestString, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();


            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // The HTTP response status code indicates success,
                // so print a success message
                System.out.println("Login successfull");

                //need to return the response we get back from the server.

                //post is taking in the req data and the server will send back response body
                //on top of this, we'll make login and register functions to turn this into the appropriate result.

                InputStream respBody = http.getInputStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                return respData;
            }
            else {

             //not success, return null

                return null;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return null;
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
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
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    //login function that returns a login result and takes in a loginRequest object
    //this will be called from within our run function

    public LoginResult login(LoginRequest request){

        String requestString = "{\"username\":\"" + request.getUsername() +
                "\", \"password\":\"" + request.getPassword() +
                "\"}";

        //should be able to get register from specs

       String responseData = post(requestString,"/user/login" );

       //do some error checking here

        if(responseData == null){
            LoginResult result = new LoginResult();

            result.setMessage("Error logging in");
            result.setSuccess(false);
            return result;
        }
        //possibly do more error checking

            //use gson to take in response data
            Gson gson = new Gson();

            LoginResult result = gson.fromJson(responseData, LoginResult.class);

            return result;


    }

    public PersonIDResult getPersonsByID(String authToken){

        String peopleList = get(authToken, "/person");

        Gson gson = new Gson();

        PersonIDResult allPersons  = gson.fromJson(peopleList, PersonIDResult.class);

        return allPersons;
    }




//end of class
}


//TODO in server proxy

//1st create the get function
//Make a get people and events functions which will be similar to the login function...getpeople will take authtoken withot string

//call server get, do the gson to json logic and return the result (get people and events functions)

//get will take in an authtoken and a name