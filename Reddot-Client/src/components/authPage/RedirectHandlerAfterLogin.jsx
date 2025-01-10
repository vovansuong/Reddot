import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { Nav, NavItem, NavLink, Row } from "reactstrap";

const RedirectHandlerAfterLogin = () => {
  const navigate = useNavigate();

  const currentUser = useSelector((state) => state.auth.login?.currentUser);

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleGoHome = () => {
    navigate("/");
  };

  const handleGoProfile = () => {
    navigate("/my-profile");
  };

  return (
    <section className="content text-center">
      <h1>Hi, {currentUser?.name ?? currentUser?.username}!</h1>
      <h3>Welcome back to Forum.</h3>
      <Row>
        <Nav className="d-flex justify-content-center">
          <NavItem>
            <NavLink>
              <button className="btn btn-primary" onClick={handleGoBack}>
                Go Back
              </button>
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink>
              <button className="btn btn-primary" onClick={handleGoHome}>
                Go Home
              </button>
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink>
              <button className="btn btn-primary" onClick={handleGoProfile}>
                Go Profile
              </button>
            </NavLink>
          </NavItem>
        </Nav>
      </Row>
    </section>
  );
};

export default RedirectHandlerAfterLogin;
