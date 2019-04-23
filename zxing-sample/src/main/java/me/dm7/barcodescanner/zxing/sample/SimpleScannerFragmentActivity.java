package me.dm7.barcodescanner.zxing.sample;

import android.os.Bundle;
import com.softdev.barcodescanner.R;

public class SimpleScannerFragmentActivity extends BaseScannerActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner_fragment);
        setupToolbar();
    }
}