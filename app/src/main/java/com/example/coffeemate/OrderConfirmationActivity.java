package com.example.coffeemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView confirmationMessage, productName, productPrice;
    private ImageView productImage;
    private Button returnHomeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize UI components
        confirmationMessage = findViewById(R.id.order_confirmation_message);
        productImage = findViewById(R.id.confirmation_product_image);
        productName = findViewById(R.id.confirmation_product_name);
        productPrice = findViewById(R.id.confirmation_product_price);
        returnHomeButton = findViewById(R.id.return_home_button);

        // Get the product details from intent extras
        String name = getIntent().getStringExtra("PRODUCT_NAME");
        String imageUrl = getIntent().getStringExtra("PRODUCT_IMAGE_URL");
        double price = getIntent().getDoubleExtra("PRODUCT_PRICE", 0);

        // Set product details in the UI
        productName.setText(name);
        productPrice.setText("Price: $" + price);

        // Load the product image using Glide
        Glide.with(this)
                .load(imageUrl)
                .into(productImage);

        // Return to home button action
        returnHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, ProductListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
