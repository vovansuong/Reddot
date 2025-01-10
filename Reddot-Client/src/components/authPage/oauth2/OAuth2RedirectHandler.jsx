import { useEffect } from "react";
// import { ACCESS_TOKEN } from '../../../constants';
import { useLocation, Navigate } from "react-router-dom";
import { getCurrentUser } from "../../../redux/apiRequest";
import { useDispatch } from "react-redux";

function OAuth2RedirectHandler() {
  const location = useLocation();

  const dispatch = useDispatch();

  function getUrlParameter(name) {
    name = name.replace("[", "\\[").replace("]", "\\]");
    const regex = new RegExp("[\\?&]" + name + "=([^&#]*)");

    let results = regex.exec(location.search);
    // console.log(`Check results`, results);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  }

  const token = getUrlParameter("token"); //access token
  const error = getUrlParameter("error");

  useEffect(() => {
    if (token) {
      getCurrentUser(dispatch, token);
    }
  }, [token, dispatch]);

  let navigateTo;
  if (token) {
    navigateTo = <Navigate to="/" state={{ from: location }} replace />;
  } else {
    navigateTo = (
      <Navigate to="/login" state={{ from: location, error: error }} replace />
    );
  }
  return navigateTo;
}

export default OAuth2RedirectHandler;
