import React from "react";
import ReactDOM from "react-dom/client";

import "perfect-scrollbar/css/perfect-scrollbar.css";

import "bootstrap/dist/css/bootstrap.min.css";
import "./assets/scss/paper-dashboard.scss?v=1.3.0";
import "./assets/css/paper-dashboard.min.css";

import "react-toastify/dist/ReactToastify.css";
import "@fortawesome/fontawesome-free/css/all.min.css";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import { store, persistor } from "./redux/store";
import { PersistGate } from "redux-persist/integration/react";
import "react-quill/dist/quill.snow.css";

import App from "./App.jsx";
import "./index.scss";

ReactDOM.createRoot(document.getElementById("root")).render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <React.StrictMode>
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </React.StrictMode>
    </PersistGate>
  </Provider>,
);
