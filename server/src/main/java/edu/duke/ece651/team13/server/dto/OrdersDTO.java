package edu.duke.ece651.team13.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * This is the DTO sent when /submitOrder
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {
    List<OrderDTO> orders;
}
