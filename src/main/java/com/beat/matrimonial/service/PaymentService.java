package com.beat.matrimonial.service;

import com.beat.matrimonial.entity.OrderEntity;
import com.beat.matrimonial.enums.OrderStatus;
import com.beat.matrimonial.payload.request.OrderRequest;
import com.beat.matrimonial.repository.OrderRepository;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {


  private final OrderRepository orderRepository;

  @Autowired
  public PaymentService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public void saveOrder(Order order, OrderRequest orderRequest) {
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setRzpOrderId(order.get("id"));
    orderEntity.setAmount(orderRequest.getAmount());
    orderEntity.setStatus(OrderStatus.CREATED);
    orderEntity.setUserId(orderRequest.getUserId());
    orderRepository.save(orderEntity);
  }

  public void completeOrder(String razorpayOrderId, String razorpayPaymentId) {

    // Update order status in database
    OrderEntity order = orderRepository.findByRzpOrderId(razorpayOrderId);
    order.setStatus(OrderStatus.COMPLETED);
    order.setRzpPaymentId(razorpayPaymentId);
    orderRepository.save(order);
  }

}

