import React from "react";
import { useContext } from "react";
import { AuthContext } from "./AuthProvider";
import { Navigate } from "react-router-dom";

export const ProtectedRoute = ({ children }) => {
  const { user } = useContext(AuthContext);
  if (!user) {
    // user is not authenticated
    return <Navigate to="/login" />;
  }
  return children;
};