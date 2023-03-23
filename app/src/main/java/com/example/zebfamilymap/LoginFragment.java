package com.example.zebfamilymap;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import Logic.DataCache;
import Logic.ServerProxy;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
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

    public static DataCache dataCache = DataCache.getInstance();



    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginRegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

        Login.setOnClickListener(v -> {


            @SuppressLint("HandlerLeak") Handler handler = new Handler(){
                //within here need to create function called handle message
                //will need to overide this function

                @Override
                public void handleMessage(Message message){
                    //Make

                    Bundle bundle = message.getData();
                    if(bundle.getBoolean(SUCCESS)){
                        //will need to make a toast object here
                        //will let you send a message on a fragment
                        //this is where you will change fragments to a map fragment
                    }else{
                        //display a message displaying an error
                    }
                }

            };

            //create your login request and afterwards
            //will call your loginTask that takes in the handler and request object
            //executor service items to take care of

//            loginTask task = new loginTask(handler, loginRequest);
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            executorService.submit(task);


            //create your handler
            //get the data
            //login task takes in a handler and a request...These are the functions that communicate with the server
            //will be written at the bottom of this file. Next to text watcher.

            //make a login request object

            //make a new task object, pass in handler and request object

            //Executor service to be able to run on a single thread


        });

        Register.setOnClickListener(v ->{




        });

        //get the strings

        //make a loginRequest object

        //pass that into the login task

        //the login task

        //this will impliment the runnable class LoginTask that impliments rurnnable

        //will have a run function...

        //login task will take in a handler(bundle code) and a loginRequest
        //insude of the run function...create a server proxy...serverhost and port number

        //try yourr login through the proxy and get your login result back from this.
        //you'll get an authtoken and a person ID. and a success value.

        //if succesfful, update the datacache with the datacache

        //if succesfull use a bundle and a message..can put values into a bundle,

        //bundle is info that is put into a message...use the handler passed into the login task to send a message

        //regist will be very similar to this.




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


           LoginResult result = proxyServer.login(requestObject);

           if(result.getSuccess()){
               //call the update datacache function
               //update the datacache
               //from the datacache you can get the base person
               //create a bundle and a message that will be used in the on click listener
               //bundle is a collection of data that you put in the message and then use the handler to send the message.


           }else{
               //bad login
               //new bundle
               //send a false value
           }


        }
    }

//    Bundle bundle = new Bundle();
//    Message message = Message.obtain();
//bundle.putBoolean(SUCCESS, true);
//bundle.putString(FIRST_NAME, basePerson.getFirstName());
//bundle.putString(LAST_NAME, basePerson.getLastName());
//message.setData(bundle);
//handler.sendMessage(message);


    //these first names and last names, these are finals that are the keys




    //end of class
}