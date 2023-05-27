import React, { createContext, useState, useMemo } from "react";

export const PlayerContext = createContext();

export const PlayerProvider = ({ children }) => {
  const [hasResearched, setHasResearched] = useState(false);
  const [hasDone, setHasDone] = useState(false);
  const [hasCloakResearched, setHasCloakResearched] = useState(false);
  const [cardsNum, setCardsNum] = useState(0);

  const value = useMemo(
    () => ({
      hasResearched,
      hasDone,
      hasCloakResearched,
      cardsNum,
      setHasResearched,
      setHasDone,
      setHasCloakResearched,
      setCardsNum,
    }),
    [
      hasResearched,
      hasDone,
      hasCloakResearched,
      cardsNum,
      setHasResearched,
      setHasDone,
      setHasCloakResearched,
      setCardsNum,
    ]
  );

  return (
    <PlayerContext.Provider value={value}>{children}</PlayerContext.Provider>
  );
};
