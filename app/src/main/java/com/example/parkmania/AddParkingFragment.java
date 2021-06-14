package com.example.parkmania;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.parkmania.models.Parking;
import com.example.parkmania.viewmodels.ParkingViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddParkingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddParkingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText etBuildingCode, etLicence, etSuitNumber, etStreetName, etCityName, etCountryName;
    Button btUseCurrentLocation, btAddParking, btnGetLocation;
    private int numberOfHours = 1;
    private String latitude = "";
    private String longitude = "";
    private String userId = "TEMP_ID";
    private final String TAG = this.getClass().getCanonicalName();
    private Parking newParking;
    private ParkingViewModel parkingViewModel;

    public AddParkingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddParkingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddParkingFragment newInstance(String param1, String param2) {
        AddParkingFragment fragment = new AddParkingFragment();
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
        return inflater.inflate(R.layout.fragment_add_parking, container, false);

    }
}