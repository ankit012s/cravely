package com.foodapp.repository;
import com.foodapp.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);
}
