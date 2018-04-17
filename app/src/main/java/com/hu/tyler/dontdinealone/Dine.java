package com.hu.tyler.dontdinealone;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Toast;

        import com.google.firebase.auth.FirebaseAuth;

public class Dine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dine);
    }

    protected void logoff(View v){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged Off.", Toast.LENGTH_SHORT).show();

        //Next time the app opens go to MainActivity
        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
        //Terminate the current activity
        finish();
    }
}
