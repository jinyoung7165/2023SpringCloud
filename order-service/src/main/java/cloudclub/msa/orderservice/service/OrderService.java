package cloudclub.msa.orderservice.service;

import cloudclub.msa.orderservice.dto.OrderDto;
import cloudclub.msa.orderservice.jpa.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
