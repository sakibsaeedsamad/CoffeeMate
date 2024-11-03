package com.example.coffeemate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productDescription, productPrice;
    private Button buyButton;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String productId;
    private String imageUrl;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        buyButton = findViewById(R.id.buy_button);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        productId = getIntent().getStringExtra("PRODUCT_ID");
        if (productId != null) {
            loadProductDetails(productId);
        }

        buyButton.setOnClickListener(v -> purchaseProduct());
    }

    private void loadProductDetails(String productId) {
        db.collection("products").document(productId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String description = documentSnapshot.getString("description");
                        price = documentSnapshot.getDouble("price");
                        imageUrl = documentSnapshot.getString("imageUrl");

                        productName.setText(name);
                        productDescription.setText(description);
                        productPrice.setText("Price: $" + price);

                        Glide.with(this)
                                .load(imageUrl)
                                .into(productImage);
                    } else {
                        Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load product details", Toast.LENGTH_SHORT).show());
    }

    private void purchaseProduct() {
        if (auth.getCurrentUser() != null) {
            // Save order to Firebase with user ID and product details
            db.collection("orders").add(new Order(auth.getCurrentUser().getUid(), productId))
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show();

                        // Pass product details to OrderConfirmationActivity
                        Intent intent = new Intent(this, OrderConfirmationActivity.class);
                        intent.putExtra("PRODUCT_NAME", productName.getText().toString());
                        intent.putExtra("PRODUCT_IMAGE_URL", imageUrl);
                        intent.putExtra("PRODUCT_PRICE", price);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Purchase failed. Try again.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Please log in to make a purchase", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
