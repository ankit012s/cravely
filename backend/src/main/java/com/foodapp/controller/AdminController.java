package com.foodapp.controller;

import com.foodapp.model.*;
import com.foodapp.repository.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final RestaurantRepository restaurants;
    private final MenuItemRepository menu;
    private final FoodOrderRepository orders;

    public AdminController(RestaurantRepository restaurants, MenuItemRepository menu, FoodOrderRepository orders){
        this.restaurants=restaurants; this.menu=menu; this.orders=orders;
    }

    @PostMapping("/restaurants")
    public Restaurant addRestaurant(@RequestBody Restaurant r){ return restaurants.save(r); }

    @PutMapping("/restaurants/{id}")
    public Restaurant updateRestaurant(@PathVariable Long id,@RequestBody Restaurant r){
        r.setId(id); return restaurants.save(r);
    }

    @PostMapping("/menu/{restaurantId}")
    public MenuItem addMenu(@PathVariable Long restaurantId,@RequestBody MenuItem m){
        Restaurant r=restaurants.findById(restaurantId).orElseThrow();
        m.setId(null); m.setRestaurant(r); return menu.save(m);
    }

    @PutMapping("/menu/{id}")
    public MenuItem updateMenu(@PathVariable Long id,@RequestBody MenuItem m){
        MenuItem old=menu.findById(id).orElseThrow();
        old.setName(m.getName()); old.setDescription(m.getDescription());
        old.setPrice(m.getPrice()); old.setImageUrl(m.getImageUrl());
        old.setAvailable(m.isAvailable());
        return menu.save(old);
    }

    @GetMapping("/orders")
    public List<FoodOrder> allOrders(){ return orders.findAll(); }

    @PutMapping("/orders/{id}/status")
    public FoodOrder status(@PathVariable Long id,@RequestParam OrderStatus value){
        FoodOrder o=orders.findById(id).orElseThrow();
        o.setStatus(value); return orders.save(o);
    }
}
