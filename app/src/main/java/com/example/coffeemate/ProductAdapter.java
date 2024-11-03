package com.example.coffeemate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<CoffeeProduct> productList; // List of products
    private OnItemClickListener listener; // Listener for item clicks

    // Interface to handle item clicks
    public interface OnItemClickListener {
        void onItemClick(CoffeeProduct product);
    }

    // Constructor
    public ProductAdapter(List<CoffeeProduct> productList) {
        this.productList = productList;
    }

    // Set the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coffee_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the product for this position
        CoffeeProduct product = productList.get(position);

        // Set product name and price
        holder.productName.setText(product.getName());
        holder.productPrice.setText("Price: $" + product.getPrice());

        if (product.getImageUrl().equals("espresso")) {
            holder.productImage.setImageResource(R.drawable.espresso);
        } else if (product.getImageUrl().equals("latte")) {
            holder.productImage.setImageResource(R.drawable.latte);
        } else if (product.getImageUrl().equals("affogato")) {
            holder.productImage.setImageResource(R.drawable.affogato);
        } else if (product.getImageUrl().equals("cortado")) {
            holder.productImage.setImageResource(R.drawable.cortado);
        } else if (product.getImageUrl().equals("turkish_coffee")) {
            holder.productImage.setImageResource(R.drawable.turkish_coffee);
        } else if (product.getImageUrl().equals("mocha")) {
            holder.productImage.setImageResource(R.drawable.mocha);
        } else if (product.getImageUrl().equals("macchiato")) {
            holder.productImage.setImageResource(R.drawable.macchiato);
        } else if (product.getImageUrl().equals("cappuccino")) {
            holder.productImage.setImageResource(R.drawable.cappuccino);
        } else if (product.getImageUrl().equals("americano")) {
            holder.productImage.setImageResource(R.drawable.americano);
        }

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size(); // Return the total number of products
    }

    // Method to update the product list
    public void updateProducts(List<CoffeeProduct> products) {
        this.productList = products;
        notifyDataSetChanged(); // Notify the adapter to refresh the view
    }

    // ViewHolder class
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView productImage;

        ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
