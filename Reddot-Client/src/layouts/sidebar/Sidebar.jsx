import { NavLink, useLocation, useNavigate } from "react-router-dom";
import React from "react";
import { Nav } from "react-bootstrap";
import PropTypes from "prop-types";
// javascript plugin used to create scrollbars on windows
import PerfectScrollbar from "perfect-scrollbar";

import { useDispatch, useSelector } from "react-redux";
import { logOut } from "../../redux/apiRequest";
import { createAxios } from "../../services/createInstance";
import { logOutSuccess } from "../../redux/authSlice";

let ps;
const Sidebar = (props) => {
  // ...rest of the code
  Sidebar.propTypes = {
    bgColor: PropTypes.string,
    activeColor: PropTypes.string,
    routes: PropTypes.arrayOf(
      PropTypes.shape({
        path: PropTypes.string.isRequired,
        layout: PropTypes.string.isRequired,
        name: PropTypes.string.isRequired,
        icon: PropTypes.string.isRequired,
        pro: PropTypes.bool,
      }),
    ).isRequired,
  };

  const { bgColor, activeColor, routes } = props;

  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleLogout = () => {
    let axiosJWT = createAxios(currentUser, dispatch, logOutSuccess);
    logOut(
      dispatch,
      currentUser?.id,
      navigate,
      currentUser?.accessToken,
      axiosJWT,
    );
  };

  const location = useLocation();
  const sidebar = React.useRef();

  // verifies if routeName is the one active (in browser input)
  const activeRoute = (routeName) => {
    return location.pathname.indexOf(routeName) > -1 ? "active" : "";
  };

  React.useEffect(() => {
    if (navigator.platform.indexOf("Win") > -1) {
      ps = new PerfectScrollbar(sidebar.current, {
        suppressScrollX: true,
        suppressScrollY: false,
      });
    }
    return function cleanup() {
      if (navigator.platform.indexOf("Win") > -1) {
        ps.destroy();
      }
    };
  });

  return (
    <div
      className={"sidebar white "}
      data-color={bgColor}
      data-active-color={activeColor}
    >
      <div className="logo">
        <a href="/" className="simple-text logo-mini">
          <div className="logo-img">
            <img src="/logo.png" alt="logo" />
          </div>
        </a>
        <a href="/" className="simple-text logo-normal">
          Reddot
        </a>
      </div>
      <hr />
      <div className="sidebar-wrapper" ref={sidebar}>
        <Nav>
          {routes?.map((prop, key) => {
            return (
              <li
                className={
                  activeRoute(prop.path) + (prop.pro ? " active-pro" : "")
                }
                key={key}
              >
                <NavLink to={prop.layout + prop.path} className="nav-NavLink">
                  <i className={prop.icon} />
                  <p>{prop.name}</p>
                </NavLink>
              </li>
            );
          })}
          <hr style={{ height: "2px", color: "whitesmoke" }} />

          {currentUser?.username ? (
            <li className="active-pro">
              <NavLink
                to="#"
                className="nav-NavLink"
                onClick={() => handleLogout()}
              >
                <i className="fa-solid fa-right-from-bracket" />
                <p>Logout</p>
              </NavLink>
            </li>
          ) : (
            <li className="active-pro">
              <NavLink to="login" className="nav-NavLink">
                <i className="fa-solid fa-right-to-bracket" />
                <p>Log in</p>
              </NavLink>
            </li>
          )}
        </Nav>
      </div>
    </div>
  );
};

export default Sidebar;
