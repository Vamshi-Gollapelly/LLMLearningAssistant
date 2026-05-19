package com.vamshigollapelly.llmlearningassistant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vamshigollapelly.llmlearningassistant.R;

public class UpgradeActivity extends AppCompatActivity {

    private TextView backButton, selectedPlanText;
    private Button starterPurchaseButton, intermediatePurchaseButton, advancedPurchaseButton;
    private Button confirmPaymentButton, cancelPaymentButton;
    private View paymentSheet, sheetBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        backButton = findViewById(R.id.backButton);
        starterPurchaseButton = findViewById(R.id.starterPurchaseButton);
        intermediatePurchaseButton = findViewById(R.id.intermediatePurchaseButton);
        advancedPurchaseButton = findViewById(R.id.advancedPurchaseButton);

        paymentSheet = findViewById(R.id.paymentSheet);
        sheetBackground = findViewById(R.id.sheetBackground);
        selectedPlanText = findViewById(R.id.selectedPlanText);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);
        cancelPaymentButton = findViewById(R.id.cancelPaymentButton);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (starterPurchaseButton != null) {
            starterPurchaseButton.setOnClickListener(v -> showPaymentSheet("Starter Plan"));
        }

        if (intermediatePurchaseButton != null) {
            intermediatePurchaseButton.setOnClickListener(v -> showPaymentSheet("Intermediate Plan"));
        }

        if (advancedPurchaseButton != null) {
            advancedPurchaseButton.setOnClickListener(v -> showPaymentSheet("Advanced Plan"));
        }

        if (sheetBackground != null) {
            sheetBackground.setOnClickListener(v -> hidePaymentSheet());
        }

        if (cancelPaymentButton != null) {
            cancelPaymentButton.setOnClickListener(v -> hidePaymentSheet());
        }

        if (confirmPaymentButton != null) {
            confirmPaymentButton.setOnClickListener(v -> {
                Toast.makeText(this, "Payment successful. Account upgraded.", Toast.LENGTH_LONG).show();
                hidePaymentSheet();
            });
        }
    }

    private void showPaymentSheet(String planName) {
        if (selectedPlanText != null) {
            selectedPlanText.setText(planName + " selected");
        }

        if (sheetBackground != null) {
            sheetBackground.setVisibility(View.VISIBLE);
        }

        if (paymentSheet != null) {
            paymentSheet.setVisibility(View.VISIBLE);
        }
    }

    private void hidePaymentSheet() {
        if (paymentSheet != null) {
            paymentSheet.setVisibility(View.GONE);
        }

        if (sheetBackground != null) {
            sheetBackground.setVisibility(View.GONE);
        }
    }
}