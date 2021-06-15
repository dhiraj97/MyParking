package com.example.parkmania;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parkmania.models.Parking;
import com.example.parkmania.session_manager.Session;
import com.example.parkmania.viewmodels.ParkingViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirstFragment extends Fragment {

    private List<Parking> parkingList;
    private ParkingViewModel parkingViewModel;
    CustomAdapter itemsAdapter;
    FirebaseAuth mAuth;
    ListView listView;
    private String userId = "TEMP_ID";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_first, container, false);


        mAuth = FirebaseAuth.getInstance();

        Session session = new Session(getActivity());
        if (session.isIsLogin()) {
            userId = session.getUserId();
        }
        Log.d("First Fragment", "First Fragment: "+userId);


        listView = view.findViewById(R.id.parkingList);
        this.parkingViewModel = ParkingViewModel.getInstance(getActivity().getApplication(),userId);
        this.parkingViewModel.allParkings.observe(getActivity(), new Observer<List<Parking>>() {
            @Override
            public void onChanged(List<Parking> parkings) {
                if(parkings != null){
                    getAllParkings(userId);
                    parkingList = parkings;
                    itemsAdapter = new CustomAdapter(getActivity(), parkings);
                    listView.setAdapter(itemsAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                gotoDetailsPage(parkingList.get(position));
            }
        });


        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void gotoDetailsPage(Parking dataItem) {
        Intent i = new Intent(getActivity(),ParkingDetails.class);
        i.putExtra("PARKING",dataItem);
        startActivity(i);
    }


    public void getAllParkings(String userId) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String COLLECTION_NAME = "parkings";
        db.collection(COLLECTION_NAME)
                .whereEqualTo("user_id", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed:" + error);
                            return;
                        }
                        parkingList.clear();
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

                        itemsAdapter = new CustomAdapter(getActivity().getApplicationContext(), parkingList);
                        listView.setAdapter(itemsAdapter);
                    }
                });

    }

    /*@Override
    public void onStart() {
        super.onStart();
        Session session = new Session(getActivity());
        if (session.isIsLogin()) {
            userId = session.getUserId();
        }
        Log.d("First Fragment", "First Fragment: "+userId);

    }*/
}