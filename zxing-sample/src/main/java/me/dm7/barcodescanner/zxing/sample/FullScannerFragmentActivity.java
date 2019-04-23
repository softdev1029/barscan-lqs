package me.dm7.barcodescanner.zxing.sample;

import android.os.Bundle;
import com.softdev.barcodescanner.R;

public class FullScannerFragmentActivity extends BaseScannerActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_full_scanner_fragment);
        setupToolbar();
    }
}