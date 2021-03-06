package com.example.parkmania;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkmania.repository.UserRepository;
import com.example.parkmania.session_manager.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText email, password, newEmail, newPassword;
    private Button btnUpdateEmail, btnDelete, btnLogout;
    private String userId = "Temp_Id";
    private TextView txtEmail;
    private UserRepository userRepository;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        Session session = new Session(getActivity());
        if (session.isIsLogin()) {
            userId = session.getUserId();
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        email = view.findViewById(R.id.edtEmail);
        email.setText(user.getEmail());

        newEmail = view.findViewById(R.id.edtNewEmail);
        password = view.findViewById(R.id.edtPassword);

        newPassword = view.findViewById(R.id.edtNewPassword);

        txtEmail = view.findViewById(R.id.txtEmail);


        userRepository = new UserRepository();
        txtEmail.setText(user.getEmail().toString());

        btnUpdateEmail = view.findViewById(R.id.btnUpdate);
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), password.getText().toString()); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User re-authenticated.");
                                //Now change your email address \\
                                //----------------Code for Changing Email Address----------\\
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(newEmail.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User email address updated.");
                                                }
                                            }
                                        });
                                user.updatePassword(newPassword.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Log.d(TAG, "User Password Changed!!");
                                                }
                                            }
                                        });
                                //----------------------------------------------------------\\
                            }
                        });
            }
        });

        btnDelete = view.findViewById(R.id.btnDeleteUser);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), password.getText().toString());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(getActivity(), SignInActivity.class));
                                                    Log.d(TAG, "User account deleted.");
                                                }
                                            }
                                        });
                            }
                        });
            }
        });

        btnLogout = view.findViewById(R.id.btnLogOut);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), SignInActivity.class));
            }
        });
        return view;
    }
}