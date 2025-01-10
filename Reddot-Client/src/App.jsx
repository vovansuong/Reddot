import "./App.scss";
import AdminRoutes from "./routes/AdminRoutes";
import AppRoutes from "./routes/AppRoutes";
// import { Admin, Resource, CustomRoutes } from 'react-admin';
import { Routes, Route } from "react-router-dom";

import { ToastContainer } from "react-toastify";

function App() {
  return (
    <>
      <main className="app-container">
        <Routes>
          <Route path="/*" element={<AppRoutes />} />
          <Route path="/admin/*" element={<AdminRoutes />} />
        </Routes>
      </main>
      <ToastContainer
        position={"bottom-right"}
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </>
  );
}

export default App;
