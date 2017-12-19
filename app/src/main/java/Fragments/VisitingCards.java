package Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nish.application.shopinterio.ImageUpload;
import com.nish.application.shopinterio.R;
import com.nish.application.shopinterio.imageListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VisitingCards.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VisitingCards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitingCards extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private EditText txtImageName;
    private Button gallery,camera,store,show;
    private Uri imgUri;

    public static final String STORAGE_PATH = "visitingCard/";
    public static final String DATABASE_PATH = "visitingCard";
    public static final int REQUEST_CODE=1234;



    private OnFragmentInteractionListener mListener;

    public VisitingCards() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VisitingCards.
     */
    // TODO: Rename and change types and number of parameters
    public static VisitingCards newInstance(String param1, String param2) {
        VisitingCards fragment = new VisitingCards();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_visiting_cards, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);
        mAuth = FirebaseAuth.getInstance();

        imageView = (ImageView) view.findViewById(R.id.image);
        txtImageName = (EditText) view.findViewById(R.id.txt);
        gallery= (Button) view.findViewById(R.id.btnGallery);
        store= (Button) view.findViewById(R.id.btnUpload);
        show= (Button) view.findViewById(R.id.btnShow);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),REQUEST_CODE);
            }
        });


        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imgUri!=null){
                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setTitle("Uploading Image");
                    dialog.show();

                    String ssss = mAuth.getCurrentUser().getEmail();

                    StorageReference ref = mStorageRef.child(STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri)); ;

                    ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            dialog.dismiss();
                            Toast.makeText(getActivity(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                            ImageUpload imageUpload = new ImageUpload(txtImageName.getText().toString(),taskSnapshot.getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(imageUpload);
                            imageView.setImageBitmap(null);
                            txtImageName.setText("");

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    dialog.dismiss();
                                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            })

                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    double Progess = (100 * taskSnapshot.getBytesTransferred())/ taskSnapshot.getTotalByteCount();
                                    dialog.setMessage("Uploaded " + (int)Progess + "%");

                                }


                            });
                }

                else {
                    Toast.makeText(getActivity(),"Please Select Image",Toast.LENGTH_SHORT).show();

                }
            }


        });


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),imageListActivity.class);
                startActivity(intent);

            }
        });




        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK &&data !=null && data.getData()!=null){

            imgUri = data.getData();

            try {

                Bitmap bm= MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),imgUri);
                imageView.setImageBitmap(bm);
            }

            catch (FileNotFoundException e){
                e.printStackTrace();
            }

            catch (IOException e){
                e.printStackTrace();
            }

        }

    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
