package com.example.irlightremote;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private IrRemoteService irService;
    private TextView txtProfileStatus;

    private enum Profile {
        A(0xA0B7),
        B(0xAD09);

        private final int address;

        Profile(int address) {
            this.address = address;
        }

        public int getAddress() {
            return address;
        }

        public Profile next() {
            return (this == A) ? B : A;
        }
    }

    private Profile currentProfile = Profile.A;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        txtProfileStatus = findViewById(R.id.txtProfileStatus);
        updateProfileLabel();

        var irManager = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        if (!irManager.hasIrEmitter()) {
            Toast.makeText(this, "IR Blaster not found!", Toast.LENGTH_LONG).show();
        }
        irService = new IrRemoteService(irManager);

        setupButton(R.id.btnPower, 0xE9);
        setupButton(R.id.btnSleep, 0xF5);
        setupButton(R.id.btnBalanceMode, 0xAC);
        setupButton(R.id.btnBrightUp, 0xAB);
        setupButton(R.id.btnBrightDown, 0xBC);
        setupButton(R.id.btnBalanceUp, 0xEF);
        setupButton(R.id.btnBalanceDown, 0xEB);
        setupButton(R.id.btnMemory, 0xAF);
        setupButton(R.id.btnTimer, 0xFA);

        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            currentProfile = currentProfile.next();
            updateProfileLabel();
            irService.sendNecCode(currentProfile.getAddress(), 0xA8);
        });
    }

    private void setupButton(int viewId, int command) {
        findViewById(viewId).setOnClickListener(v ->
                irService.sendNecCode(currentProfile.getAddress(), command)
        );
    }

    private void updateProfileLabel() {
        String statusText = getString(R.string.active_profile, currentProfile.name());
        txtProfileStatus.setText(statusText);
    }
}