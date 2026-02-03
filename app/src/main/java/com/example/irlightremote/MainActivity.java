package com.example.irlightremote;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private IrRemoteService irService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        var irManager = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        if (!irManager.hasIrEmitter())
            Toast.makeText(this, "IR Blaster not found!", Toast.LENGTH_LONG).show();

        irService = new IrRemoteService(irManager);

        setupButton(R.id.btnPower, 0xE9);
        setupButton(R.id.btnSleep, 0xF5);
        setupButton(R.id.btnBalanceMode, 0xAC);
        setupButton(R.id.btnBrightUp, 0xAB);
        setupButton(R.id.btnBrightDown, 0xBC);
        setupButton(R.id.btnBalanceUp, 0xEF);
        setupButton(R.id.btnBalanceDown, 0xEB);
    }

    private void setupButton(int viewId, int command) {
        findViewById(viewId).setOnClickListener(v -> irService.sendNecCode(command));
    }
}