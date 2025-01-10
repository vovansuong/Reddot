import { Card, Row, Col } from "react-bootstrap";

import PropTypes from "prop-types";

const CardInFo = (props) => {
  const { title, icon, number, updateNumber } = props;

  const handleUpdateNow = () => {
    updateNumber();
  };

  return (
    <Card className="card-stats" style={{ maxHeight: "200px" }}>
      <Card.Body>
        <Row>
          <Col md="4" xs="5">
            <div className="icon-big text-center icon-warning">
              <i className={icon} />
            </div>
          </Col>
          <Col md="8" xs="7">
            <div className="numbers">
              <strong
                className="card-category"
                style={{
                  whiteSpace: "nowrap",
                  textOverflow: "ellipse",
                  overflow: "hidden",
                }}
              >
                {title}
              </strong>
              <Card.Title tag="p">{number}</Card.Title>
              <p />
            </div>
          </Col>
        </Row>
      </Card.Body>
      <Card.Footer>
        <hr />
        <div className="stats">
          <button className="fas fa-sync-alt" onClick={handleUpdateNow} />{" "}
          Update Now
        </div>
      </Card.Footer>
    </Card>
  );
};

CardInFo.propTypes = {
  title: PropTypes.string,
  icon: PropTypes.string,
  number: PropTypes.number,
  updateNumber: PropTypes.func,
};

export default CardInFo;
