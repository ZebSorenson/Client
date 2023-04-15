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
import BackendLogic.DataCache;
import RequestResult.LoginRequest;
import RequestResult.RegisterRequest;
import Tasks.LoginTask;
import Tasks.RegisterTask;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1"; //Not sure what this is. What added when Fragment was created
    private static final String ARG_PARAM2 = "param2";//Not sure what this is. What added when Fragment was created

    public static final String SUCCESS = "Success";

    public static final String FIRSTNAME = "first name";

    public static final String LASTNAME = "last name";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText host, port, username, password, email, firstName, lastName;

    private String Host, Port, Username, Password, Email, FirstName, LastName;

    private RadioButton male, female;

    private Button Register, Login;



    public static DataCache dataCache = DataCache.getInstance();

    public LoginFragment() {
        // Empty constructor which is required for the fragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { //this is the view of the fragment

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


         //brings the fragment into view

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

        //We need to watch for any changes in the text!

        host.addTextChangedListener(textWatcher);

        port.addTextChangedListener(textWatcher);

        username.addTextChangedListener(textWatcher);

        password.addTextChangedListener(textWatcher);

        firstName.addTextChangedListener(textWatcher);

        lastName.addTextChangedListener(textWatcher);

        email.addTextChangedListener(textWatcher);

        male.setOnClickListener(v -> updateRegisterButtonStatus());
        female.setOnClickListener(v -> updateRegisterButtonStatus());




        //This is the functionality for what happenign when the login button is clicked
        Login.setOnClickListener(v -> {

            String userName = username.getText().toString();
            String passWord = password.getText().toString();

            //this code handles getting a message back from the request and displaying the toast
            @SuppressLint("HandlerLeak") Handler handler = new Handler(Looper.getMainLooper()) {

                @Override
                public void handleMessage(Message message) {

                    Bundle loginBundle = message.getData();

                    if (loginBundle.getBoolean(SUCCESS)) {

                        Message successMessage =Message.obtain();

                        Bundle messageBundle = new Bundle();

                        messageBundle.putBoolean("LoginSuccess", true);

                        messageBundle.putString("whichFragment", "MapsFragment");

                        successMessage.setData(messageBundle);



                        System.out.println("you have entered a good toast object"); //used for testing the toast and verifying we arrive here

                        Toast.makeText(getActivity(), "Successful Login", Toast.LENGTH_SHORT).show(); //this toast will display first and then the next

                        Toast.makeText(getActivity(), "Welcome " + loginBundle.getString(FIRSTNAME) + " " + loginBundle.getString(LASTNAME) +" to your family app!", Toast.LENGTH_SHORT).show();

                        FragmentManager fragmentManager = getParentFragmentManager();

                        Fragment googleMapsFragment = new MapsFragment();

                        fragmentManager.beginTransaction().replace(R.id.mainActivity, googleMapsFragment).commit();

                    } else {
                        //we'll arrive here for anything other than a succesfull login. Should handle any type of error
                        Toast.makeText(getActivity(), "Error logging in. Please check input and try again", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            LoginRequest login_Request = new LoginRequest();

            login_Request.setUsername(userName);

            login_Request.setPassword(passWord);

            LoginTask task = new LoginTask(handler, login_Request, host.getText().toString(), port.getText().toString()); //call the login task that's in the Tasks package

            ExecutorService executor_Service = Executors.newSingleThreadExecutor(); //handles the thread work

            executor_Service.submit(task); //send the task to the executor service

        });

        Register.setOnClickListener(v -> { //handles the logic for what happens when the Register button is clicked

            String gender = ""; //place holder for the gender string

            //check which gender button is checked and then set the gender to either m or f respectively
            if (female.isChecked()) {

                gender = "f";
            } else if (male.isChecked()) {

                gender = "m";
            }

            String Firstname = firstName.getText().toString();

            String Lastname = lastName.getText().toString();

            String Username = username.getText().toString();

            String Password = password.getText().toString();

            String Email = email.getText().toString();

            String Host = host.getText().toString();

            String Port = port.getText().toString();

            @SuppressLint("HandlerLeak") Handler handler = new Handler(Looper.getMainLooper()) {

                @Override
                public void handleMessage(Message message) {

                    Bundle register_Bundle = message.getData();

                    if (register_Bundle.getBoolean(SUCCESS)) {

                        System.out.println("you have entered a good toast object for register"); //used for debugging

                        Toast.makeText(getActivity(), "Successful New Register", Toast.LENGTH_SHORT).show();

                        Toast.makeText(getActivity(), "Welcome newly registered user!  " + register_Bundle.getString(FIRSTNAME) + " " + register_Bundle.getString(LASTNAME), Toast.LENGTH_SHORT).show();

//                        FragmentManager fragmentManager = getParentFragmentManager();
//
//                        Fragment googleMapsFragment = new MapsFragment();
//
//                        fragmentManager.beginTransaction().replace(R.id.mainActivity, googleMapsFragment).commit();

                    } else {
                        Toast.makeText(getActivity(), "Error registering the user. Check input and try again", Toast.LENGTH_SHORT).show();
                        //we'll arrive here for anything that isn't a successful register which should handle a majority of the erros
                    }
                }
            };



            RegisterRequest register_Request = new RegisterRequest();

            register_Request.setUsername(Username);

            register_Request.setPassword(Password);

            register_Request.setFirstName(Firstname);

            register_Request.setLastName(Lastname);

            register_Request.setEmail(Email);

            register_Request.setGender(gender);

            RegisterTask register_Task = new RegisterTask(handler, register_Request, host.getText().toString(),port.getText().toString()); //send the needed info to the register task class in Tasks

            ExecutorService executor_Service = Executors.newSingleThreadExecutor();

            executor_Service.submit(register_Task);

        });



        return view;
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Don't need to do anything
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //capture the needed text from the login/register screen

            Host = host.getText().toString();

            Port = port.getText().toString();

            Username = username.getText().toString();

            Password = password.getText().toString();

            Email = email.getText().toString();

            FirstName = firstName.getText().toString();

            LastName = lastName.getText().toString();

            Login.setEnabled(!Username.isEmpty() && !Password.isEmpty()&& !Host.isEmpty() && !Port.isEmpty());

            updateRegisterButtonStatus();

           // Register.setEnabled(!Host.isEmpty() && !Port.isEmpty() && !Username.isEmpty() && !Password.isEmpty() && !Email.isEmpty() && !FirstName.isEmpty() && !LastName.isEmpty() && ((male.isChecked() || female.isChecked())));

            //want to set up listeners for both male and female buttons. Us an onclick listener and then put this code into those button listeners

            // Register.setEnabled(male.isSelected() || female.isSelected());

            //need to update this so that male or female can be selected last

            // Register.setEnabled(!Host.isEmpty() && !Port.isEmpty() && !Username.isEmpty() && !Password.isEmpty() && !Email.isEmpty() && !FirstName.isEmpty() && !LastName.isEmpty());

            //DO THE ABOVE TASK TO HELP WITH THE GENDER BUTTONS!!!


        }

        @Override
        public void afterTextChanged(Editable s) {
            //Don't need to do anything
        }




    };

    private void updateRegisterButtonStatus() {
        Register.setEnabled(!Host.isEmpty() && !Port.isEmpty() && !Username.isEmpty() && !Password.isEmpty() && !Email.isEmpty() && !FirstName.isEmpty() && !LastName.isEmpty() && (male.isChecked() || female.isChecked()));
    }


    //end of class
}