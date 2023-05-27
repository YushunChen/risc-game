import "bootstrap/dist/css/bootstrap.min.css";
import "./styles/App.css";
import { AuthProvider } from "./auth/AuthProvider";
import { OrderProvider } from "./game/context/OrderProvider";
import { PlayerProvider } from "./game/context/PlayerProvider";

import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import GameView from "./game/GameView";
import AttackView from "./game/AttackView";
import MoveView from "./game/MoveView";
import LoginView from "./auth/LoginView";
import GameListView from "./game_list/GameListView";
import { ProtectedRoute } from "./auth/ProtectedRoute";
import RegisterView from "./auth/RegisterView";
import ResearchView from "./game/ResearchView";
import UpgradeView from "./game/UpgradeView";
import CloakView from "./game/CloakView";
import ResearchCloakView from "./game/ResearchCloakView";
import CardView from "./game/CardView";
import ProtectCardView from "./game/ProtectCardView";
import SpyView from "./game/SpyView";

function App() {
  return (
    <Router>
      <AuthProvider>
        <PlayerProvider>
          <OrderProvider>
            <Routes>
              <Route
                exact
                path="/gameList"
                element={
                  <ProtectedRoute>
                    <GameListView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/"
                element={
                  <ProtectedRoute>
                    <GameView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/attack"
                element={
                  <ProtectedRoute>
                    <AttackView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/move"
                element={
                  <ProtectedRoute>
                    <MoveView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/research"
                element={
                  <ProtectedRoute>
                    <ResearchView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/upgrade"
                element={
                  <ProtectedRoute>
                    <UpgradeView />
                  </ProtectedRoute>
                }
              />
                <Route
                    exact
                    path="/spy"
                    element={
                        <ProtectedRoute>
                            <SpyView />
                        </ProtectedRoute>
                    }
                />
              <Route
                exact
                path="/researchCloak"
                element={
                  <ProtectedRoute>
                    <ResearchCloakView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/cloak"
                element={
                  <ProtectedRoute>
                    <CloakView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/card"
                element={
                  <ProtectedRoute>
                    <CardView />
                  </ProtectedRoute>
                }
              />
              <Route
                exact
                path="/protectCard"
                element={
                  <ProtectedRoute>
                    <ProtectCardView />
                  </ProtectedRoute>
                }
              />
              <Route path="/login" element={<LoginView />} />
              <Route path="/register" element={<RegisterView />} />
            </Routes>
          </OrderProvider>
        </PlayerProvider>
      </AuthProvider>
    </Router>
  );
}

export default App;
