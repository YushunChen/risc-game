package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

public class CreateSpyOwnershipChecker extends RuleChecker{
    public CreateSpyOwnershipChecker(RuleChecker next){super(next);}

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if(order.getSource().getOwner() != player){
            throw new IllegalArgumentException("Invalid create spy order: The source territory is not owned by you.");
        }
        if(order.getDestination() != null){
            throw new IllegalArgumentException("Invalid create spy order: The destination territory should be null for create spy.");
        }
    }
}
