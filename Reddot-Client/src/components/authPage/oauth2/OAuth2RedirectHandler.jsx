import { useEffect } from "react";
import { useLocation, Navigate } from "react-router-dom";
import { getCurrentUser } from "../../../redux/apiRequest";
import { useDispatch } from "react-redux";

function OAuth2RedirectHandler() {
  const searchQueryString = useLocation().search;
  console.info("OAuth2RedirectHandler location: ", searchQueryString);
  const dispatch = useDispatch();

  function getUrlParameter(name) {
    const urlParams = new URLSearchParams(searchQueryString);
    let results = urlParams.get(name);
    console.log(`Check results: ${results}`);
    return results;
  }

  const token = getUrlParameter("token");

  const error = getUrlParameter("error");

  useEffect(() => {
    if (token) {
      getCurrentUser(dispatch, token);
    }
  }, [token, dispatch]);

  let navigateTo;
  if (token) {
    navigateTo = <Navigate to="/" state={{ from: searchQueryString }} replace />;
  } else {
    navigateTo = (
      <Navigate to="/login" state={{ from: searchQueryString, error: error }} replace />
    );
  }
  return navigateTo;
}

export default OAuth2RedirectHandler;
