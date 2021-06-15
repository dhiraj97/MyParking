package com.example.parkmania;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parkmania.models.Parking;
import com.example.parkmania.session_manager.Session;
import com.example.parkmania.viewmodels.ParkingViewModel;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTracker myLocationTracker;

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
        View view = inflater.inflate(R.layout.fragment_add_parking, container, false);

        Session session = new Session(getContext());
        if (session.isIsLogin()) {
            userId = session.getUserId();
            Log.d("Parking Detaisl", "onCreateView: SESSION DETAILS"+userId);
        }

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        Spinner spinner = (Spinner) view.findViewById(R.id.edtNumberOfHours);
        etBuildingCode = view.findViewById(R.id.edtBuildingCode);
        etLicence = view.findViewById(R.id.edtLicenseNumber);
        etSuitNumber = view.findViewById(R.id.edtSuitNumber);
        etStreetName = view.findViewById(R.id.edtStreetName);
        etCityName = view.findViewById(R.id.edtCityName);
        etCountryName = view.findViewById(R.id.edtCountryName);

        this.parkingViewModel = ParkingViewModel.getInstance(this.getActivity().getApplication(),userId);
        this.newParking = new Parking();

        btUseCurrentLocation = (Button) view.findViewById(R.id.btnCurrentLocation);
        btUseCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });
        btnGetLocation = view.findViewById(R.id.btnGetLocation);
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationFromAddress();
            }
        });

        btAddParking = view.findViewById(R.id.btnAddParking);
        btAddParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addParking();
            }
        });

        // Spinner Drop down elements
        List<String> numberOfHoursTexts = new ArrayList<String>();
        numberOfHoursTexts.add("1 Hr Or Less");
        numberOfHoursTexts.add("4 Hrs");
        numberOfHoursTexts.add("12 Hrs");
        numberOfHoursTexts.add("24 Hrs");

        List<Integer> numberOfHoursList = new ArrayList<Integer>();
        numberOfHoursList.add(1);
        numberOfHoursList.add(4);
        numberOfHoursList.add(12);
        numberOfHoursList.add(24);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numberOfHoursTexts);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                numberOfHours = numberOfHoursList.get(i);
                System.out.println(numberOfHours);
                // Showing selected spinner item
                Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    private void addParking() {
        if (this.userId.isEmpty()) {
            return;
        }
        if (validateData()) {
            newParking = getDetails();
            System.out.println(newParking.toString());
            saveDataToDB();
        }
    }

    private void saveDataToDB() {
        this.parkingViewModel.addParking(this.newParking);
        Toast.makeText(getContext(), "Parking Added", Toast.LENGTH_LONG).show();
        clearData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void clearData() {
        etBuildingCode.setText("");
        etCountryName.setText("");
        etCityName.setText("");
        etStreetName.setText("");
        etSuitNumber.setText("");
        etLicence.setText("");
        latitude = "";
        longitude = "";
    }

    private Parking getDetails() {
        Parking parking = new Parking();
        parking.setUserId(userId);
        parking.setBuildingCode(etBuildingCode.getText().toString());
        parking.setCarLicensePlateNumber(etLicence.getText().toString());
        parking.setSuitNumber(etSuitNumber.getText().toString());
        parking.setNoOfHrs(numberOfHours);
        parking.setLatitude(latitude);
        parking.setLongitude(longitude);
        parking.setDate(getDate());
        return parking;
    }

    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy h:mm a", Locale.getDefault());
        return df.format(c);
    }

    private boolean validateData() {
        boolean isValidData = true;
        if (this.etBuildingCode.getText().toString().isEmpty()) {
            this.etBuildingCode.setError("Please enter Building Code");
            isValidData = false;
        }
        if (this.etLicence.getText().toString().isEmpty()) {
            this.etLicence.setError("Please enter Licence Number");
            isValidData = false;
        }
        if (this.etSuitNumber.getText().toString().isEmpty()) {
            this.etSuitNumber.setError("Please enter Suit Number");
            isValidData = false;
        }
        if (this.etStreetName.getText().toString().isEmpty()) {
            this.etStreetName.setError("Please enter Street Name");
            isValidData = false;
        }
        if (this.etCityName.getText().toString().isEmpty()) {
            this.etCityName.setError("Please enter City Name");
            isValidData = false;
        }
        if (this.etCountryName.getText().toString().isEmpty()) {
            this.etCountryName.setError("Please enter Country Name");
            isValidData = false;
        }
        if (latitude.isEmpty() || longitude.isEmpty()) {
            this.etStreetName.setError("Please enter valid Street Name");
            this.etCityName.setError("Please enter valid City Name");
            this.etCountryName.setError("Please enter valid Country Name");
            isValidData = false;
        }
        return isValidData;
    }

    private void getLocationFromAddress()
    {
        boolean isValidData = true;
        if (this.etStreetName.getText().toString().isEmpty()) {
            this.etStreetName.setError("Please enter Street Name");
            isValidData = false;
        }
        if (this.etCityName.getText().toString().isEmpty()) {
            this.etCityName.setError("Please enter City Name");
            isValidData = false;
        }
        if (this.etCountryName.getText().toString().isEmpty()) {
            this.etCountryName.setError("Please enter Country Name");
            isValidData = false;
        }

        if(isValidData)
        {
            String street = this.etStreetName.getText().toString();
            String city = this.etCityName.getText().toString();
            String country = this.etCountryName.getText().toString();

            Address address = new Address(Locale.getDefault());
            address.setCountryName(country);
            address.setThoroughfare(street);
            address.setLocality(city);

            String postalAddress = country +"," + city + "," + street;//"\(country), \(city), \(street)"

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(postalAddress,1);
                if(addresses != null && addresses.size() >= 1) {
                    double lat = addresses.get(0).getLatitude();
                    double lon = addresses.get(0).getLongitude();
                    this.latitude = lat != 0.0 ? String.valueOf(lat) : "";
                    this.longitude = lon != 0.0 ? String.valueOf(lon) : "";
                    Toast.makeText(getContext(), "Longitude:" + Double.toString(lat) + "\nLatitude:" + Double.toString(lon), Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "UNKNOWN ERROR Try Again", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void getCurrentLocation() {
        myLocationTracker = new LocationTracker(getContext());
        if (myLocationTracker.canGetLocation()) {
            double longitude = myLocationTracker.getLongitude();
            double latitude = myLocationTracker.getLatitude();
            this.latitude = latitude != 0.0 ? String.valueOf(latitude) : "";
            this.longitude = longitude != 0.0 ? String.valueOf(longitude) : "";
            useCurrentLocation(latitude,longitude);
            //Toast.makeText(getContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {
            myLocationTracker.showSettingsAlert();
        }
    }

    private void useCurrentLocation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses != null && addresses.size() >= 1) {
                String country = addresses.get(0).getCountryName();
                String city = addresses.get(0).getLocality();
                String street = addresses.get(0).getThoroughfare();

                etStreetName.setText(street);
                etCityName.setText(city);
                etCountryName.setText(country);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "UNKNOWN ERROR Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();
        for (Object perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(Object permission) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myLocationTracker != null) {
            myLocationTracker.stopListener();
        }
    }

    public void actionBack(View view) {
        super.getActivity().onBackPressed();;
    }
}