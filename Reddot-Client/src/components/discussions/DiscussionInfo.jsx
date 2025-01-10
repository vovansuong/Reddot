import { useEffect } from "react";
import { Card, ListGroup, Badge } from "react-bootstrap";
import PropTypes from "prop-types";

//Service
const DiscussionInfo = (props) => {
  const { discussionInfo } = props;

  useEffect(() => {}, [discussionInfo]);

  return (
    <Card>
      <Card.Header>
        <Card.Title as="h5">Discussion Info</Card.Title>
      </Card.Header>
      <Card.Body>
        <ListGroup as="ol" numbered>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">
                Started by: <span>{discussionInfo?.createdBy}</span>
              </div>
            </div>
          </ListGroup.Item>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">
                Lasted by:{" "}
                <span>{discussionInfo?.stat?.lastComment?.commenter}</span>
              </div>
            </div>
          </ListGroup.Item>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">View count</div>
            </div>
            <Badge bg="primary" pill>
              {discussionInfo.stat?.viewCount}
            </Badge>
          </ListGroup.Item>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">Comments</div>
            </div>
            <Badge bg="primary" pill>
              {discussionInfo?.comments?.length}
            </Badge>
          </ListGroup.Item>
        </ListGroup>
      </Card.Body>
    </Card>
  );
};

DiscussionInfo.propTypes = {
  discussionInfo: PropTypes.object.isRequired,
};

export default DiscussionInfo;
