package com.example.irlightremote;

import android.hardware.ConsumerIrManager;

public record IrRemoteService(ConsumerIrManager irManager) {
    private static final int FREQUENCY_HZ = 38000;
    private static final int HDR_MARK_MS = 9000;
    private static final int HDR_SPACE_MS = 4500;
    private static final int BIT_MARK_MS = 560;
    private static final int BIT_ZERO_MS = 560;
    private static final int BIT_ONE_MS = 1690;

    public IrRemoteService {
        if (irManager == null)
            throw new IllegalArgumentException("irManager cannot be null");
    }

    public void sendNecCode(int address, long command) {
        long address0 = address & 0xFF;
        long address1 = (address >> 8) & 0xFF;
        long invCommand = command ^ 0xFF;

        long frame = address1 | address0 << 8 | command << 16 | invCommand << 24;

        var pattern = new int[67];
        int i = 0;
        pattern[i++] = HDR_MARK_MS;
        pattern[i++] = HDR_SPACE_MS;

        for (int bit = 0; bit < 32; ++bit) {
            pattern[i++] = BIT_MARK_MS;
            int bitToSendMs = ((frame >> bit) & 1) == 1 ? BIT_ONE_MS : BIT_ZERO_MS;
            pattern[i++] = bitToSendMs;
        }

        pattern[i] = BIT_MARK_MS;
        irManager.transmit(FREQUENCY_HZ, pattern);
    }
}