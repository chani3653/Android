package com.example.sns_project2.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sns_project2.GalleryActivity;
import com.example.sns_project2.Memberinfo;
import com.example.sns_project2.R;
import com.example.sns_project2.changestatus;
import com.example.sns_project2.googlemap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.app.Activity.RESULT_OK;
import static com.example.sns_project2.Util.GALLERY_IMAGE;
import static com.example.sns_project2.Util.INTENT_MEDIA;

public class Profile extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Profile";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    private Button button;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Profile() {
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView nameTextView = view.findViewById(R.id.nameTextView);
       // final TextView ageTextView = view.findViewById(R.id.ageTextView);
       // final TextView sexTextView = view.findViewById(R.id.sexTextView);
        final TextView statusView = view.findViewById(R.id.status);
        final ImageView imageView = view.findViewById(R.id.imageView);
        statusView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myStartActivity(changestatus.class);
            }
        });

        statusView.setPaintFlags(statusView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //이미지뷰 클릭시 이벤트처리

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(GalleryActivity.class, GALLERY_IMAGE, 0);
            }
        });



        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("user").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            nameTextView.setText(document.getData().get("name").toString());
                            //ageTextView.setText(document.getData().get("age").toString());
                            //sexTextView.setText(document.getData().get("sex").toString());
                            statusView.setText(document.getData().get("status").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return view;
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void myStartActivity(Class c, Memberinfo memberinfo) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("profileinfo", memberinfo);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.imageView);
                    //imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



    private void myStartActivity(Class c, int media, int requestCode) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra(INTENT_MEDIA, media);
        startActivityForResult(intent, requestCode);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.floatingActionButton2:
                    myStartActivity(googlemap.class);
                    break;
            }
        }
    };
}
