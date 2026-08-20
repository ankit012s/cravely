package com.foodapp.repository;
import com.foodapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
public interface FoodOrderRepository extends JpaRepository<FoodOrder,Long> {
    @EntityGraph(attributePaths = {"restaurant", "items", "items.menuItem"})
    List<FoodOrder> findByCustomerIdOrderByCreatedAtDesc(@Param("customerId") Long customerId);

    @Query("select distinct o from FoodOrder o join fetch o.restaurant left join fetch o.items i left join fetch i.menuItem where o.id = :id")
    Optional<FoodOrder> findByIdWithDetails(@Param("id") Long id);
}
