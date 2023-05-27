package edu.duke.ece651.team13.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the DTO contained in OrdersDTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long sourceTerritoryId;
    private Long destinationTerritoryId;
    private int unitNum;
    private String unitType;
    private String orderType;
}
