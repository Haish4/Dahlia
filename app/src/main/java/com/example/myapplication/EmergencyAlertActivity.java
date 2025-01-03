import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class EmergencyAlertActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_alert);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            sendEmergencyAlert();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void sendEmergencyAlert() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Send SOS message via SMS
                            String message = "SOS! I'm in danger. Location: " + location.getLatitude() + ", " + location.getLongitude();
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage("EMERGENCY_CONTACT_NUMBER", null, message, null, null);
                            Toast.makeText(EmergencyAlertActivity.this, "Emergency alert sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
