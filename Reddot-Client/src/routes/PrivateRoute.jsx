import { Navigate, useNavigate } from "react-router-dom";
import { Alert } from "react-bootstrap";
import { useSelector } from "react-redux";
import PropTypes from "prop-types";

const PrivateRoute = ({ children }) => {
  const user = useSelector((state) => state.user.account);

  const navigate = useNavigate();
  const goBack = () => navigate(-1);

  if (user && user.auth === false) {
    return (
      <Alert variant="danger">
        <Alert.Heading>Oh snap! You got an error!</Alert.Heading>
        <p>You do not have permission to access this router</p>
        <div className="flexGrow">
          <button onClick={goBack}>Go Back</button>
        </div>
      </Alert>
    );
  }

  return user && user.auth === true ? children : <Navigate to="/" />;
};

PrivateRoute.propTypes = {
  children: PropTypes.node.isRequired,
};

export default PrivateRoute;
