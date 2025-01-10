import { Container, Row, Col } from "reactstrap";
// used for making the prop types of this component
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

function Footer(props) {
  return (
    <footer className={+props.default ? "footer footer-default" : "footer"}>
      <Container fluid={props.fluid != ""}>
        <Row>
          <Col md="6">
            <nav className="footer-nav">
              <ul>
                <li>
                  <Link to="/policy">Privacy Policy</Link>
                </li>
                <li>
                  <Link to="/term">Terms And Conditions</Link>
                </li>
              </ul>
            </nav>
          </Col>
          <Col md="6">
            <div className="credits ml-auto">
              <div className="copyright">
                &copy; {1900 + new Date().getYear()}, made with{" "}
                <i className="fa fa-heart heart" /> by Reddot
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </footer>
  );
}

Footer.propTypes = {
  default: PropTypes.bool,
  fluid: PropTypes.bool,
};

export default Footer;
