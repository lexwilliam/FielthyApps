package example.com.fielthyapps.Auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import example.com.fielthyapps.Feature.Nutrition.HasilNutritionActivity;
import example.com.fielthyapps.Feature.Nutrition.NutritionActivity;
import example.com.fielthyapps.R;

public class EditAuthActivity extends AppCompatActivity {
private EditText eT_email,eT_password,eT_email_baru;
private Button btn_edit;
private String email_lama,password,email_baru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_auth);
        eT_email = findViewById(R.id.eT_email_auth);
        eT_password = findViewById(R.id.eT_password_edit);
        eT_email_baru = findViewById(R.id.eT_email_baru);
        btn_edit = findViewById(R.id.btn_edit_auth_submit);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        eT_email.setText(user.getEmail());
        disableEditText(eT_email);

        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), "testing123"); // Ganti dengan kata sandi saat ini

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Reauthentication successful, proceed to update email
                            updateEmail(user, "newemail@example.com");
                        } else {
                            Log.e("Reauthenticate", "Error reauthenticating", task.getException());
                            Toast.makeText(this, "Error reauthenticating: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_lama = eT_email.getText().toString();
                email_baru = eT_email_baru.getText().toString();
                password = eT_password.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

//                user.sendEmailVerification()
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "Email sent.");
//                                }
//                            }
//                        });




            }
        });
    }



    // Function to reauthenticate and update email
    private void reauthenticateAndUpdateEmail(String currentPassword, String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Reauthenticate", "Reauthentication successful");
//                            sendVerificationEmail(newEmail, user);
                        } else {
                            Log.e("Reauthenticate", "Error reauthenticating: " + task.getException().getMessage());
                            Toast.makeText(this, "Error reauthenticating: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.e("UserError", "User or email is null");
            Toast.makeText(this, "User or email is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationEmail(String currentPassword, String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Reauthentication successful, proceed to update email
                            user.updateEmail(newEmail)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Send verification email to new email address
                                            user.sendEmailVerification()
                                                    .addOnCompleteListener(verificationTask -> {
                                                        if (verificationTask.isSuccessful()) {
                                                            Log.d("EmailVerification", "Verification email sent to " + newEmail);
                                                            Toast.makeText(this, "Verification email sent to " + newEmail, Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.e("EmailVerification", "Error sending verification email", verificationTask.getException());
                                                            Toast.makeText(this, "Error sending verification email: " + verificationTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Log.e("EmailUpdate", "Error updating email: " + updateTask.getException().getMessage());
                                            Toast.makeText(this, "Error updating email: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.e("Reauthenticate", "Error reauthenticating: " + task.getException().getMessage());
                            Toast.makeText(this, "Error reauthenticating: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.e("UserError", "User or email is null");
            Toast.makeText(this, "User or email is null", Toast.LENGTH_SHORT).show();
        }
    }



    // Function to update email in Firestore
    private void updateEmailInFirestore(String uid, String newEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("user").document(uid);

        userRef.update("email", newEmail)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Email successfully updated in Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreUpdate", "Error updating email in Firestore", e);
                    Toast.makeText(this, "Error updating email in Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    private void updateEmail(FirebaseUser user, String newEmail) {
        user.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EmailUpdate", "User email address updated.");
                        Toast.makeText(this, "User email address updated.", Toast.LENGTH_SHORT).show();

                        // Optional: Send verification email to the new email address
                        sendVerificationEmail(user);
                    } else {
                        Log.e("EmailUpdate", "Error updating email", task.getException());
                        Toast.makeText(this, "Error updating email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EmailVerification", "Verification email sent to " + user.getEmail());
                        Toast.makeText(this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("EmailVerification", "Error sending verification email", task.getException());
                        Toast.makeText(this, "Error sending verification email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}