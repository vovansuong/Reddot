import { useSelector } from "react-redux";
import { useLocation, Navigate, Outlet } from "react-router-dom";

const RequireAuth = ({ allowedRoles }) => {
  const user = useSelector((state) => state.auth.login?.currentUser);

  const location = useLocation();

  const isAuthorized = user?.roles?.find((role) =>
    allowedRoles?.includes(role),
  );
  const isUserAuthenticated = user?.accessToken !== null;

  let navigateTo;

  if (isAuthorized) {
    navigateTo = <Outlet />;
  } else if (isUserAuthenticated) {
    navigateTo = (
      <Navigate to="/unauthorized" state={{ from: location }} replace />
    );
  } else {
    navigateTo = <Navigate to="/login" state={{ from: location }} replace />;
  }

  return navigateTo;
};

export default RequireAuth;
