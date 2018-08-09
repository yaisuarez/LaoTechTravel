package android.big.dev.laotechtravel.fragment;

import android.app.ProgressDialog;
import android.big.dev.laotechtravel.R;
import android.big.dev.laotechtravel.ServiceActivity;
import android.big.dev.laotechtravel.utility.MyAlert;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {

    private ProgressDialog progressDialog;

    private String emailString, passwordString;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Check Status
        checkStatus();

//        Register Controller
        registerController();

//        Login Controlerr
        loginControlerr();

    }   // Method Main

    private void loginControlerr() {
        Button button = getView().findViewById(R.id.button_signin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage(" Check Authen few minus ... ");
                progressDialog.show();


                EditText editEmail = getView().findViewById(R.id.etmail);
                EditText editPassword = getView().findViewById(R.id.etpssword);

                emailString = editEmail.getText().toString().trim();
                passwordString = editPassword.getText().toString().trim();


                if (emailString.isEmpty() || passwordString.isEmpty()) {

                    MyAlert myAlert = new MyAlert(getActivity());
                    myAlert.normalDialog("Have Speac", "Plase Fill All Blank");
                    progressDialog.dismiss();

                } else {

                    checkAuthen();


                }

            }
        });
    }

    private void checkAuthen() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "welcome", Toast.LENGTH_SHORT).show();
                           moveToService();

                        } else {

//                            Sign In False
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.normalDialog("Cannot Sign In",
                                    "Beacuse ==> " + task.getException().getMessage().toString());
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    private void checkStatus() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Log.d("9AugV1", "Login Status");

            moveToService();

        } else {
            Log.d("9AugVl", "No Login");
        }
    }

    private void moveToService() {

        startActivity(new Intent(getContext(), ServiceActivity.class));
        getActivity().finish();

    }

    private void registerController() {
        TextView textView = getView().findViewById(R.id.txtRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Replace Fragment
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragment, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}
