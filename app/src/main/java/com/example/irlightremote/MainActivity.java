package com.example.irlightremote;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ConsumerIrManager irManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        irManager = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);

        Button btn = findViewById(R.id.btnFanToggle);
        btn.setOnClickListener(v -> transmitSignal());
    }

    private void transmitSignal() {
        if (irManager == null || !irManager.hasIrEmitter()) {
            Toast.makeText(this, "No IR Hardware", Toast.LENGTH_SHORT).show();
            return;
        }

        final int FREQ = 38000;
        final int MARK = 560;
        final int ZERO = 560;
        final int ONE  = 1690;

        // EXACT bytes as printed by LIRC
        int byte1 = 0xA0; // address byte 1
        int byte2 = 0xB7; // address byte 2
        int byte3 = 0xE9; // command
        int byte4 = byte3 ^ 0xFF; // inverse = 0x16

        // FORCE WIRE ORDER: A0 → B7 → E9 → 16
        long frame =
                ((long) byte1) |
                        ((long) byte2 << 8) |
                        ((long) byte3 << 16) |
                        ((long) byte4 << 24);

        int[] pattern = new int[2 + 32 * 2 + 1];
        int i = 0;

        // NEC header
        pattern[i++] = 9000;
        pattern[i++] = 4500;

        // Send bits LSB-first
        for (int bit = 0; bit < 32; bit++) {
            pattern[i++] = MARK;
            pattern[i++] = ((frame >> bit) & 1) == 1 ? ONE : ZERO;
        }

        pattern[i] = MARK;

        irManager.transmit(FREQ, pattern);
    }
}
