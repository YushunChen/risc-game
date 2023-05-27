import React, { createContext, useMemo, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  const login = useCallback((data) => {
    setUser(data);
    navigate("/gameList");
  }, [navigate]);

  const logout = useCallback(() => {
    setUser(null);
    navigate("/login");
  }, [navigate]);

  const value = useMemo(
    () => ({
      user,
      login,
      logout,
    }),
    [user, login, logout]
  );

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};