package edu.duke.ece651.team13.server.enums;

/**
 * 1. INVISIBLE: territory that has never been seen, only the outline should be displayed
 * 2. VISIBLE_NEW: player’s own territories & immediately adjacent enemy territory & Enemy territory with spies
 * 3. VISIBLE_OLD: territory that has been seen before but cannot be seen now
 */
public enum TerritoryDisplayEnum {
    INVISIBLE,
    VISIBLE_NEW,
    VISIBLE_OLD;
}
