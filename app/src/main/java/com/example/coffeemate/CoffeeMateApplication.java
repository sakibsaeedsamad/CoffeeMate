package com.example.coffeemate;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayOutputStream;

public class CoffeeMateApplication extends Application {

    private FirebaseFirestore db;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Ensure Firebase initialization is complete before accessing Firestore
        if (FirebaseApp.getApps(this).isEmpty()) {
            Log.e("CoffeeMateApplication", "Firebase is not initialized correctly.");
            return;
        }

        // Add default coffee products if they don't already exist
        addDefaultCoffeeProducts();
    }

    private void addDefaultCoffeeProducts() {
        // Define the coffee products
        String[][] products = {
                {"1", "Espresso", "Strong coffee with intense flavor", "2.99", "espresso"},
                {"2", "Latte", "Smooth and creamy coffee with milk", "3.49", "latte"},
                {"3", "Affogato", "A shot of espresso over vanilla ice cream", "4.99", "affogato"},
                {"4", "Cortado", "A balanced coffee with equal parts espresso and milk", "3.29", "cortado"},
                {"5", "Turkish Coffee", "Strong coffee brewed with finely ground coffee beans", "2.79", "turkish_coffee"},
                {"6", "Mocha", "A delicious blend of chocolate and coffee", "3.99", "mocha"},
                {"7", "Macchiato", "Espresso topped with a dollop of foamed milk", "3.19", "macchiato"},
                {"8", "Cappuccino", "A rich blend of espresso, steamed milk, and foam", "3.69", "cappuccino"},
                {"9", "Americano", "Espresso diluted with hot water", "2.49", "americano"}
        };

        // Iterate through each product and add to Firestore if not already present
        for (String[] productInfo : products) {
            String productId = productInfo[0];
            db.collection("coffee_products").document(productId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Product already exists, do not add
                                Log.d("CoffeeMateApplication", "Product already exists: " + productId);
                            } else {
                                // Product does not exist, add it
                                CoffeeProduct product = new CoffeeProduct(
                                        productInfo[0], // ID
                                        productInfo[1], // Name
                                        productInfo[2], // Description
                                        Double.parseDouble(productInfo[3]), // Price
                                        productInfo[4] // Image as base64
                                );

                                db.collection("coffee_products").document(product.getId())
                                        .set(product)
                                        .addOnSuccessListener(aVoid -> Log.d("CoffeeMateApplication", "Product added: " + product.getName()))
                                        .addOnFailureListener(e -> Log.w("CoffeeMateApplication", "Error adding product: " + product.getName(), e));
                            }
                        } else {
                            Log.w("CoffeeMateApplication", "Error getting document: ", task.getException());
                        }
                    });
        }
    }
}
