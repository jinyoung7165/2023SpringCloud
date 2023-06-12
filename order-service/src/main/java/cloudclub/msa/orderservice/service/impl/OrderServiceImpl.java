package cloudclub.msa.orderservice.service.impl;

import cloudclub.msa.orderservice.dto.OrderDto;
import cloudclub.msa.orderservice.jpa.OrderEntity;
import cloudclub.msa.orderservice.jpa.OrderRepository;
import cloudclub.msa.orderservice.service.OrderService;
import cloudclub.msa.orderservice.utils.CustomObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        OrderEntity orderEntity = CustomObjectMapper.to(orderDto, OrderEntity.class);
        orderRepository.save(orderEntity);

        OrderDto resOrderDto = CustomObjectMapper.to(orderEntity, OrderDto.class);
        return resOrderDto;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        return CustomObjectMapper.to(orderEntity, OrderDto.class);
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
