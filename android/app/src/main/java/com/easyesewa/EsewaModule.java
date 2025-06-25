package com.easyesewa;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.*;

import com.esewa.sdk.payment.ESewaConfiguration;
import com.esewa.sdk.payment.ESewaPayment;
import com.esewa.sdk.payment.ESewaPaymentActivity;

public class EsewaModule extends ReactContextBaseJavaModule {
    private static final int REQUEST_CODE_PAYMENT = 1001;
    private Promise paymentPromise;

    public EsewaModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "EsewaModule";
    }

    @ReactMethod
    public void startEsewaPayment(String amount, String productName, String productId, String callbackUrl, Promise promise) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject("NO_ACTIVITY", "No activity found.");
            return;
        }

        paymentPromise = promise;

        ESewaConfiguration config = new ESewaConfiguration()
                .clientId("JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R")
                .secretKey("BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==")
                .environment(ESewaConfiguration.ENVIRONMENT_TEST);

        ESewaPayment payment = new ESewaPayment(amount, productName, productId, callbackUrl);

        Intent intent = new Intent(currentActivity, ESewaPaymentActivity.class);
        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, config);
        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, payment);

        currentActivity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @ActivityEventListener
    private final ActivityEventListener activityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode != REQUEST_CODE_PAYMENT || paymentPromise == null) return;

            if (resultCode == Activity.RESULT_OK && data != null) {
                String message = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                paymentPromise.resolve(message);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                paymentPromise.reject("CANCELLED", "User cancelled payment.");
            } else {
                String message = data != null ? data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE) : "Unknown Error";
                paymentPromise.reject("FAILED", message);
            }

            paymentPromise = null;
        }
    };

    @Override
    public void initialize() {
        super.initialize();
        getReactApplicationContext().addActivityEventListener(activityEventListener);
    }
}
