package com.example.zebfamilymap;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Logic.DataCache;
import Logic.ServerProxy;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.PersonResult;


/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String SUCCESS = "Success";

    private static final String FIRSTNAME = "first name";

    private static final String LASTNAME = "last name";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText host, port, username, password, email, firstName, lastName;

    private String Host, Port, Username, Password, Email, FirstName, LastName;

    private RadioButton male, female;

    private Button Register, Login;

    private boolean goodLogin;

    public static DataCache dataCache = DataCache.getInstance();



    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment LoginRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static LoginFragment newInstance(String param1, String param2) {
//        LoginFragment fragment = new LoginFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //make a view of your login fragment
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //creates the fragment into the view

        View view = inflater.inflate(R.layout.fragment_login_register, container, false);



        Login = view.findViewById(R.id.login); //double check this

        Register = view.findViewById(R.id.register);


        host = view.findViewById(R.id.hostInput);

        port = view.findViewById(R.id.serverPortInput);

        username = view.findViewById(R.id.usernameInput);

        password = view.findViewById(R.id.passwordinput);

        email = view.findViewById(R.id.emailInput);

        firstName = view.findViewById(R.id.firstNameInput);

        lastName = view.findViewById(R.id.lastNameInput);



        male = view.findViewById(R.id.male);

        female = view.findViewById(R.id.female);

        //need to watch the text and see if it changes

        host.addTextChangedListener(textWatcher);

        port.addTextChangedListener(textWatcher);

        username.addTextChangedListener(textWatcher);

        password.addTextChangedListener(textWatcher);

        firstName.addTextChangedListener(textWatcher);

        lastName.addTextChangedListener(textWatcher);

        email.addTextChangedListener(textWatcher);

//        Handler handler = new Handler(Looper.getMainLooper()){
//            //within here need to create function called handle message
//            //will need to overide this function
//
//            //possibly look into listener to show your Toast object
//
//            @Override
//            public void handleMessage(Message message){
//                //Make
//
//                Bundle bundle = message.getData();
//                if(bundle.getBoolean(SUCCESS)){
//                    //will need to make a toast object here
//                    //will let you send a message on a fragment
//                    //this is where you will change fragments to a map fragment
//                    System.out.println("you have entered a good toast object");
//                    Toast.makeText(getActivity().getApplicationContext(), "Welcome " + bundle.getString(FIRSTNAME) + " " + bundle.getString(LASTNAME), Toast.LENGTH_SHORT).show();
//                    // Toast.makeText(requireContext(), "Welcome " + bundle.getString(FIRSTNAME) + " " + bundle.getString(LASTNAME), Toast.LENGTH_SHORT).show();
//                    //possibly have this as getActivity
//                    FragmentManager fragmentManager = getParentFragmentManager();
//                }else{
////getConext
//                    Toast.makeText(getActivity().getApplicationContext(), "Error logging user in. There may be an invalid password or username", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//        };

        Login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Epic Failure dude", Toast.LENGTH_SHORT).show();

                //grab the username for the text, and all the information?

                //Toast.makeText(getActivity().getApplicationContext(), "Welcome " + username.getText().toString(), Toast.LENGTH_LONG).show();


                //create your handler
                //get the data
                //login task takes in a handler and a request...These are the functions that communicate with the server

                Handler handler = new Handler(Looper.getMainLooper()){ // is this looper supposed to be here?
                    //within here need to create function called handle message
                    //will need to overide this function

                    //possibly look into listener to show your Toast object

                    @Override
                    public void handleMessage(Message message){
                        //Make

                        Bundle bundle = message.getData();

                        String firstName = bundle.getString(FIRSTNAME);

                        //!firstName.equals("error")
                        if(bundle.getBoolean(SUCCESS)){
                            //will need to make a toast object here
                            //will let you send a message on a fragment
                            //this is where you will change fragments to a map fragment
                            System.out.println("you have entered a good toast object");



                            Toast.makeText(getActivity(), "Welcome " + bundle.getString(FIRSTNAME), Toast.LENGTH_SHORT).show();


                           // toast.show();

                            // Toast.makeText(requireContext(), "Welcome " + bundle.getString(FIRSTNAME) + " " + bundle.getString(LASTNAME), Toast.LENGTH_SHORT).show();
                            //possibly have this as getActivity
                            FragmentManager fragmentManager = getParentFragmentManager();
                        }else{
//getConext
                            Toast.makeText(getActivity().getApplicationContext(), "Error logging user in. There may be an invalid password or username", Toast.LENGTH_SHORT).show();
                        }

                    }

                };

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(username.getText().toString());
                loginRequest.setPassword(password.getText().toString());

                loginTask task = new loginTask(handler, loginRequest);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(task);

                //call the executor service and set it to a single thread executor?
            }
        });

       // Login.setOnClickListener(v -> { //onclick listener will be diff than onClick lister

//            Toast.makeText(getActivity().getApplicationContext(), "Welcome " , Toast.LENGTH_LONG).show();
//
//            //create your handler
//            //get the data
//            //login task takes in a handler and a request...These are the functions that communicate with the server
//
//            LoginRequest loginRequest = new LoginRequest();
////            loginRequest.setUsername(username.toString());
////            loginRequest.setPassword(password.toString());
//            loginRequest.setUsername(username.getText().toString());
//            loginRequest.setPassword(password.getText().toString());
//
//            loginTask task = new loginTask(handler, loginRequest);
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            executorService.submit(task);


      //  });

//        Register.setOnClickListener(v ->{
//
//
//
//
//        });


        //look at videos of how to send a bundle in android studio.
        // Inflate the layout for this fragment
        return view;
    }




    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //make your strings for your text that you are inputing and then get the text and set it to a string



            Host = host.getText().toString();

            Port = port.getText().toString();

            Username = username.getText().toString();

            Password = password.getText().toString();

            Email = email.getText().toString();

            FirstName = firstName.getText().toString();

            LastName = lastName.getText().toString();

            Login.setEnabled(!Username.isEmpty() && !Password.isEmpty());

            Register.setEnabled(!Host.isEmpty() && !Port.isEmpty() && !Username.isEmpty() && !Password.isEmpty() && !Email.isEmpty() && !FirstName.isEmpty() && !LastName.isEmpty());



        }

        @Override
        public void afterTextChanged(Editable s) {

        }


    };

    private static class loginTask implements Runnable{

        //make a constructor to pass in items



        public LoginRequest requestObject;

        public Handler handlerObject;
        //our onclick listener is giving us our request
        //this will make this request and send it to the server and get back the result

        public loginTask(Handler handler, LoginRequest request){

            this.handlerObject = handler;
            this.requestObject = request;

        }

        @Override
        public void run() {
            //want to create proxy server

            ServerProxy proxyServer = new ServerProxy(); // can set up to take in the host and port number

            //YOU COULD SET THE PORT AND HOST RIGHT HERE SINCE YOU'VE ALREADY GABBED THE INFO ABOVE


           LoginResult result = proxyServer.login(requestObject);

           String error = "Error";





           if(result.getSuccess()){
               //call the update datacache function
               //update the datacache
               //from the datacache you can get the base person
               //create a bundle and a message that will be used in the on click listener
               //bundle is a collection of data that you put in the message and then use the handler to send the message.

               System.out.println("The user logged in was "+ result.getUsername());
               Bundle bundle = new Bundle();
               Message message = Message.obtain();
//               bundle.putBoolean(SUCCESS, true);
// this is where we set items to true

               bundle.putString(FIRSTNAME, result.getUsername());

               message.setData(bundle);
               handlerObject.sendMessage(message);

               PersonResult allPeople = proxyServer.getAllPersons(result.getAuthtoken());

               dataCache.addFamilyMembers(allPeople);






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





    //end of class
}