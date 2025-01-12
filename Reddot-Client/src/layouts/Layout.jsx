import React from "react";

import PerfectScrollbar from "perfect-scrollbar";

import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "./sidebar/Sidebar";
import { useSelector } from "react-redux";
import { ROLES } from "../constants";
import routes from "../routes/routes";
import routesAdmin from "../routes/routesForAdmin";
import routesForMod from "../routes/routesForMod";

import Header from "./header/Header";
import Footer from "./footer/Footer";
import FixedPlugin from "../components/adminPage/FixedPlugin/FixedPlugin";
import PropTypes from "prop-types";

let ps;
const Layout = (props) => {
  const { route } = props;

  const [backgroundColor, setBackgroundColor] = React.useState("white");
  const [activeColor, setActiveColor] = React.useState("info");

  const mainPanel = React.useRef();
  const location = useLocation();

  React.useEffect(() => {
    if (navigator.platform.indexOf("Win") > -1) {
      ps = new PerfectScrollbar(mainPanel.current);
      document.body.classList.toggle("perfect-scrollbar-on");
    }
    return function cleanup() {
      if (navigator.platform.indexOf("Win") > -1) {
        ps.destroy();
        document.body.classList.toggle("perfect-scrollbar-on");
      }
    };
  });

  React.useEffect(() => {
    (mainPanel.current || {}).scrollTop = 0;
    document.scrollingElement.scrollTop = 0;
  }, [location]);

  const handleActiveClick = (color) => {
    setActiveColor(color);
  };

  const handleBgClick = (color) => {
    setBackgroundColor(color);
  };

  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  const isShowRouteAdmin = () => {
    const rolesOfCurrentUser = currentUser?.roles;
    return rolesOfCurrentUser.includes(ROLES.ADMIN);
  };

  const routesSidebar = (route) => {
    if (route === "routesAdmin") {
      return isShowRouteAdmin() ? routesAdmin : routesForMod;
      // return routesAdmin;
    }
    return routes;
  };

  return (
    <div className="layout-admin wrapper">
      <Sidebar
        {...props}
        routes={routesSidebar(route)}
        bgColor={backgroundColor}
        activeColor={activeColor}
      />
      <div className="main-panel" ref={mainPanel}>
        <Header {...props} />

        <Outlet />

        <Footer fluid {...props} />
      </div>
      <FixedPlugin
        bgColor={backgroundColor}
        activeColor={activeColor}
        handleActiveClick={handleActiveClick}
        handleBgClick={handleBgClick}
      />
    </div>
  );
};

Layout.propTypes = {
  route: PropTypes.string.isRequired,
};

export default Layout;
