package cloudclub.msa.orderservice.vo;

import cloudclub.msa.orderservice.dto.OrderDto;
import cloudclub.msa.orderservice.jpa.OrderEntity;
import cloudclub.msa.orderservice.utils.CustomObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;

    public static ResponseOrder of(OrderDto order) { return CustomObjectMapper.to(order, ResponseOrder.class); }
    public static ResponseOrder of(OrderEntity order) { return CustomObjectMapper.to(order, ResponseOrder.class); }
}
