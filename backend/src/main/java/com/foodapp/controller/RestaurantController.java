package com.foodapp.controller;

import com.foodapp.model.*;
import com.foodapp.repository.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    public record MenuItemResponse(Long id, String name, String description,
                                   double price, String imageUrl, boolean available) {}

    private final RestaurantRepository restaurants;
    private final MenuItemRepository menu;

    public RestaurantController(RestaurantRepository restaurants, MenuItemRepository menu) {
        this.restaurants=restaurants; this.menu=menu;
    }

    @GetMapping
    public List<Restaurant> all(){ return restaurants.findByActiveTrue(); }

    @GetMapping("/{id}/menu")
    public List<MenuItemResponse> menu(@PathVariable Long id){
        return menu.findByRestaurantIdAndAvailableTrue(id).stream()
                .map(item -> new MenuItemResponse(item.getId(), item.getName(),
                        item.getDescription(), item.getPrice(), item.getImageUrl(), item.isAvailable()))
                .toList();
    }
}
