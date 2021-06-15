package com.example.parkmania.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.parkmania.models.Parking;
import com.example.parkmania.repository.ParkingRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ParkingViewModel extends AndroidViewModel {
    private static ParkingViewModel ourInstance;
    private final ParkingRepository parkingRepository = new ParkingRepository();
    public FirebaseFirestore firestore;
    public MutableLiveData<List<Parking>> allParkings;

    public static ParkingViewModel getInstance(Application application, String userId) {
        if (ourInstance == null) {
            ourInstance = new ParkingViewModel(application,userId);
        }
        return ourInstance;
    }

    private ParkingViewModel(Application application,String userId) {
        super(application);
        this.parkingRepository.getAllParkings(userId);
        this.allParkings = this.parkingRepository.allParkings;
    }

    public void addParking(Parking parking) {
        this.parkingRepository.addParking(parking);
    }
}
