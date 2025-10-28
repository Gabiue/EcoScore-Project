import { Navigate, Route, Routes } from "react-router-dom";
import PraticasPage from "@/pages/praticas";

function App() {
  return (
    <Routes>
      {/* Root redirects to práticas page */}
      <Route element={<PraticasPage />} path="/" />
      <Route element={<PraticasPage />} path="/praticas" />
      {/* Catch-all redirect to root */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
