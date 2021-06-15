package com.example.parkmania.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.parkmania.models.Users;
import com.example.parkmania.repository.UserRepository;

import com.example.parkmania.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private static UserViewModel ourInstance;
    private final UserRepository userRepository = new UserRepository();

    public static UserViewModel getInstance(Application application){
        if (ourInstance == null){
            ourInstance = new UserViewModel(application);
        }

        return ourInstance;
    }

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public void addUser(Users user, Context context){ this.userRepository.addUser(user,context);}
}
