package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * A class that provides a base for implementing a chain of responsibility pattern
 * for validating game rules in the game engine.
 */
public abstract class RuleChecker {
    private final RuleChecker next;

    public RuleChecker() {
        this.next = null;
    }

    public RuleChecker(RuleChecker next) {
        this.next = next;
    }

    /**
     * Use parameter polymorphism to check on different types of orders
     * TODO: changed to pass in PlayerOrder, parametric polymorphism may not work here
     */
    protected abstract void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException;

    /**
     * Handle chaining rules, Subclasses will generally NOT override
     * this method.
     *
     * @return null if the placement is OK
     * any non-null String: a description of what is wrong, suitable to show the user
     */
    public String checkOrder(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        //if we fail our own rule: stop the placement is not legal
        checkMyRule(order, player);

        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkOrder(order, player);
        }
        //if there are no more rules, then the placement is legal
        return null;
    }

}
