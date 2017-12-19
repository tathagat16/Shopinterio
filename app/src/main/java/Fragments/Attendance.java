package Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nish.application.shopinterio.GpsLocation;
import com.nish.application.shopinterio.MainActivity;
import com.nish.application.shopinterio.MapsActivity;
import com.nish.application.shopinterio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Attendance.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Attendance#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Attendance extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button b,b2;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Attendance() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Attendance.
     */
    // TODO: Rename and change types and number of parameters
    public static Attendance newInstance(String param1, String param2) {
        Attendance fragment = new Attendance();
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
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_attendance, container, false);


        b = (Button) view.findViewById (R.id.button);
      //  b2 = (Button) view.findViewById (R.id.button2);




        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GpsLocation gt = new GpsLocation(getActivity());

                Location l = gt.getLocation();
                if( l == null){
                    Toast.makeText(getActivity().getApplicationContext(),"GPS unable to location. Please open GPS in High Accuracy mode",Toast.LENGTH_SHORT).show();
                }else {
                     double lat = l.getLatitude();
                     double lon = l.getLongitude();


                   //Toast.makeText(getActivity(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_LONG).show();

                    Intent sendingIntent = new Intent(getActivity(),MapsActivity.class);
                    sendingIntent.putExtra("lat", lat);
                    sendingIntent.putExtra("long",lon);
                    startActivity(sendingIntent);

                    try {
                        getAddress(lat, lon);
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                }
            }
        });


        return view;

    }

    public  void getAddress(double lat,double lon) throws IOException{




      //  DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        // DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());

        // DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("Attendance").document("gX5HcJAZUKistr4JC8mn");
        String email = mAuth.getCurrentUser().getEmail();
        DocumentReference mColRef = FirebaseFirestore.getInstance().collection("Attendance").document(email);

        Geocoder geocoder;
        geocoder = new Geocoder(getContext(), Locale.getDefault());


        List<Address> addresses = geocoder.getFromLocation(lat,lon,1);

        String result = addresses.get(0).getAddressLine(0);

        String address = addresses.get(0).getAddressLine(0);
        //Toast.makeText(getActivity(),"add : "+address,Toast.LENGTH_LONG).show();
// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();


        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


        String store = address +", "+city +", "+ state;

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("location",store);
        dataToSave.put("time",currentDateTimeString);
       // mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>()

        mColRef.collection("all").document(currentDateTimeString).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Location Saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

            }
        });
        }

/*
        currentUserDB.child("Time").setValue(currentDateTimeString);
        currentUserDB.child("Location").setValue(store);
        currentUserDB.child("Name").setValue(mAuth.getCurrentUser().getEmail());
        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(new Date());
         */



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

/*

 */