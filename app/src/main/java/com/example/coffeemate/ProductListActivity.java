package com.example.coffeemate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        // Initialize product list and adapter
        List<CoffeeProduct> productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);

        // Set item click listener
        productAdapter.setOnItemClickListener(product -> {
            Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            startActivity(intent);
        });

        recyclerViewProducts.setAdapter(productAdapter);

        // Fetch products from Firestore
        fetchProducts();
    }

    private void fetchProducts() {
        db.collection("coffee_products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CoffeeProduct> productList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Get only the name and id fields
                            String id = document.getString("id"); // Get the document ID
                            String name = document.getString("name"); // Get the name field
                            String description = document.getString("description");
                            String imageUrl = document.getString("imageUrl");
                            Double price = document.getDouble("price");
                            // Create a CoffeeProduct object with only the required fields
                            CoffeeProduct product = new CoffeeProduct();
                            product.setId(id); // Assuming you have a setId method
                            product.setName(name); // Assuming you have a setName method
                            product.setDescription(description); // Assuming you have a setName method
                            product.setImageUrl(imageUrl); // Assuming you have a setName method
                            product.setPrice(price); // Assuming you have a setName method

                            Log.d("ProductList", "Product fetched: " + product);
                            productList.add(product);
                        }
                        productAdapter.updateProducts(productList);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show Toast on the main thread
                        ProductListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProductListActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.e("ProductListActivity", "Error getting documents: ", e);
                    }
                });
    }


}
