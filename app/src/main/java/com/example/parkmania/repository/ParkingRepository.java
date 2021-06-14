package com.example.parkmania.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.parkmania.models.Parking;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final FirebaseFirestore db;
    private final String COLLECTION_NAME = "parkings";

    public MutableLiveData<List<Parking>> allParkings = new MutableLiveData<List<Parking>>();

    public ParkingRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void addParking(Parking parking) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("user_id", parking.getUserId());
            data.put("building_code", parking.getBuildingCode());
            data.put("number_of_hours", parking.getNoOfHrs());
            data.put("licence_number", parking.getCarLicensePlateNumber());
            data.put("suit_number", parking.getSuitNumber());
            data.put("latitude", parking.getLatitude());
            data.put("longitude", parking.getLongitude());
            data.put("date", parking.getDate());

            db.collection(COLLECTION_NAME)
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "OnSuccess: Document added succesfully" + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Error creating document on Firestore" + e.getLocalizedMessage());
                        }
                    });

            // for custom id
            //db.collection(COLLECTION_NAME).document("jk@jk.com").set(data);

        } catch (Exception ex) {
            Log.e(TAG, "addParking: " + ex.getLocalizedMessage());
        }

    }

    public void deleteParking(String id) {

    }

    public void getAllParkings(String userId) {
        try {
            db.collection(COLLECTION_NAME)
                    .whereEqualTo("user_id", userId)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "Listening to Collection failed due to some reasons" + error);
                                return;
                            }

                            List<Parking> parkingList = new ArrayList<>();
                            for (DocumentSnapshot doc : value) {
                                Parking parking = new Parking();
                                if (doc.getId() != null) {
                                    parking.setId(doc.getId().toString());
                                }
                                if (doc.get("user_id") != null) {
                                    parking.setUserId(doc.getString("user_id"));
                                }
                                if (doc.get("building_code") != null) {
                                    parking.setBuildingCode(doc.getString("building_code"));
                                }
                                if (doc.get("number_of_hours") != null) {
                                    parking.setNoOfHrs(doc.getLong("number_of_hours"));
                                }
                                if (doc.get("licence_number") != null) {
                                    parking.setCarLicensePlateNumber(doc.getString("licence_number"));
                                }
                                if (doc.get("suit_number") != null) {
                                    parking.setSuitNumber(doc.getString("suit_number"));
                                }
                                if (doc.get("latitude") != null) {
                                    parking.setLatitude(doc.getString("latitude"));
                                }
                                if (doc.get("longitude") != null) {
                                    parking.setLongitude(doc.getString("longitude"));
                                }
                                if (doc.get("date") != null) {
                                    parking.setDate(doc.getString("date"));
                                }

                                System.out.println(parking.toString());

                                parkingList.add(parking);
                            }
                            Log.d(TAG, "onEvent: parkingList" + parkingList.toString());
                            allParkings.postValue(parkingList);
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, "getAllParkings: " + ex.getLocalizedMessage());
        }
    }
}
