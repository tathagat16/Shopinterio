package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.application.shopinterio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeetingDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeetingDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MeetingDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingDetails newInstance(String param1, String param2) {
        MeetingDetails fragment = new MeetingDetails();
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
        View view= inflater.inflate(R.layout.fragment_meeting_details, container, false);

        final EditText your_name        = (EditText) view.findViewById(R.id.your_name);
        final EditText client_name       = (EditText) view.findViewById(R.id.client_name);
        final EditText meeting_venue     = (EditText) view.findViewById(R.id.meeting_venue);
        final EditText remarks     = (EditText) view.findViewById(R.id.remarks);


        Button email = (Button) view.findViewById(R.id.confirm);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name      = your_name.getText().toString();
                String client     = client_name.getText().toString();
                String venue   = meeting_venue.getText().toString();
                String details   = remarks.getText().toString();


                if (TextUtils.isEmpty(name)){
                    your_name.setError("Enter Your Name");
                    your_name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(client)){
                    client_name.setError("Enter Client Name");
                    client_name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(venue)){
                    meeting_venue.setError("Enter Meeting Venue");
                    meeting_venue.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(details)){
                    remarks.setError("Enter Remarks");
                    remarks.requestFocus();
                    return;
                }

               // DocumentReference mDocRef = FirebaseFirestore.getInstance().document("meetingDetails/B7HQqdsqmgV2Tb7kwyya/allShared/vveE2TolnpmnSL4oGiLZ\n");
                CollectionReference mColRef = FirebaseFirestore.getInstance().collection("meetingDetails");

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String email = mAuth.getCurrentUser().getEmail();

                Map<String, Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put("clientName",client);
                dataToSave.put("meetingVenue",venue);
                dataToSave.put("remarks",details);
                dataToSave.put("yourName",name);
                mColRef.document(email).set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });


        return view;
    }

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
