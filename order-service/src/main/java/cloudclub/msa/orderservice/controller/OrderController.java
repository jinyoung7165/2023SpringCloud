package cloudclub.msa.orderservice.controller;

import cloudclub.msa.orderservice.dto.OrderDto;
import cloudclub.msa.orderservice.jpa.OrderEntity;
import cloudclub.msa.orderservice.service.OrderService;
import cloudclub.msa.orderservice.utils.CustomObjectMapper;
import cloudclub.msa.orderservice.utils.response.ApiResponse;
import cloudclub.msa.orderservice.vo.RequestOrder;
import cloudclub.msa.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {
    private final OrderService orderService;

    @GetMapping(value = "/health-check")
    public String status(HttpServletRequest request) {
        return String.format("It's working in User Service, on PORT %s",
                request.getServerPort()); // env-> local.server.port로 가져올 수도 있음
    }

    @PostMapping(value = "/{userId}/orders")
    public ResponseEntity<ResponseOrder> createUser(@PathVariable("userId") String userId,
                                                    @RequestBody RequestOrder order) {
        OrderDto orderDto = CustomObjectMapper.to(order, OrderDto.class);
        orderDto.setUserId(userId);
        return ApiResponse.created(ResponseOrder.of(orderService.createOrder(orderDto)));
    }

    @GetMapping(value = "/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(o -> {
            result.add(ResponseOrder.of(o));
        });
        return ApiResponse.success(result);
    }

}
