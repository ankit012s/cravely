package com.foodapp;

import com.foodapp.model.*;
import com.foodapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(RestaurantRepository rr, MenuItemRepository mr) {
        return args -> {
            seedRestaurant(rr, mr, "Spice Garden", "Indian", "Delhi",
                    "https://images.unsplash.com/photo-1601050690597-df0568f70950",
                    List.of(new Dish("Paneer Tikka", "Char-grilled paneer with mint chutney", 220,
                            "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8"),
                            new Dish("Veg Biryani", "Aromatic basmati rice and garden vegetables", 180,
                                    "https://images.unsplash.com/photo-1563379091339-03246963d51a")));
            seedRestaurant(rr, mr, "Crust & Co.", "Pizza", "Mumbai",
                    "https://images.unsplash.com/photo-1579751626657-72bc17010498",
                    List.of(new Dish("Margherita Pizza", "Tomato, basil and creamy mozzarella", 299,
                            "https://images.unsplash.com/photo-1574071318508-1cdbab80d002"),
                            new Dish("Farmhouse Pizza", "Roasted vegetables and melted cheese", 349,
                                    "https://images.unsplash.com/photo-1579751626657-72bc17010498")));
            seedRestaurant(rr, mr, "Green Bowl", "Healthy", "Bengaluru",
                    "https://images.unsplash.com/photo-1512621776951-a57141f2eefd",
                    List.of(new Dish("Avocado Power Bowl", "Avocado, greens, quinoa and roasted seeds", 279,
                            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd"),
                            new Dish("Rainbow Salad", "Crunchy vegetables with lemon dressing", 229,
                                    "https://images.unsplash.com/photo-1540420773420-3366772f4999")));
            seedRestaurant(rr, mr, "Sugar & Crumb", "Desserts", "Pune",
                    "https://images.unsplash.com/photo-1551024506-0bccd828d307",
                    List.of(new Dish("Chocolate Lava Cake", "Warm chocolate cake with a molten center", 199,
                            "https://images.unsplash.com/photo-1606313564200-e75d5e30476c"),
                            new Dish("Berry Cheesecake", "Creamy cheesecake with fresh berry compote", 249,
                                    "https://images.unsplash.com/photo-1565958011703-44f9829ba187"),
                            new Dish("Mango Kulfi", "Silky Indian ice cream with ripe mango", 149,
                                    "https://images.unsplash.com/photo-1563805042-7684c019e1cb")));
        };
    }

    private void seedRestaurant(RestaurantRepository rr, MenuItemRepository mr, String name,
            String cuisine, String address, String imageUrl, List<Dish> dishes) {
        Restaurant restaurant = rr.findAll().stream()
                .filter(existing -> name.equals(existing.getName())).findFirst().orElseGet(Restaurant::new);
        restaurant.setName(name);
        restaurant.setCuisine(cuisine);
        restaurant.setAddress(address);
        restaurant.setImageUrl(imageUrl);
        restaurant.setActive(true);
        restaurant = rr.save(restaurant);
        Restaurant saved = restaurant;
        dishes.forEach(dish -> {
            if (mr.findByRestaurantIdAndAvailableTrue(saved.getId()).stream()
                    .noneMatch(item -> dish.name().equals(item.getName()))) {
                MenuItem item = new MenuItem();
                item.setName(dish.name());
                item.setDescription(dish.description());
                item.setPrice(dish.price());
                item.setImageUrl(dish.imageUrl());
                item.setRestaurant(saved);
                mr.save(item);
            }
        });
    }

    private record Dish(String name, String description, double price, String imageUrl) {
    }
}
