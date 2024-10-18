package com.beat.matrimonial.repository;


import com.beat.matrimonial.entity.OrderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  OrderEntity findByRzpOrderId(String razorpayOrderId);

  List<OrderEntity> findByUserId(Long userId);
}
