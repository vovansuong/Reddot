import { Card, ListGroup, Badge } from "react-bootstrap";
import PropTypes from "prop-types";
import { useState, useEffect } from "react";

import { getForumStat } from "../../services/forumService/ForumStatService";

const ForumInfo = () => {
  const [forumStat, setForumStat] = useState({});

  const ObjectForumStat = async () => {
    let res = await getForumStat();
    if (res) {
      setForumStat(res);
    }
  };

  useEffect(() => {
    ObjectForumStat();
  }, []);

  return (
    <Card style={{ backgroundColor: "#FADBD8" }}>
      <Card.Header>
        <Card.Title as="h5">Forum Info</Card.Title>
      </Card.Header>
      <Card.Body>
        <ListGroup as="ol" numbered>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">Forum</div>
            </div>
            <Badge bg="primary" pill>
              {forumStat.totalForums}
            </Badge>
          </ListGroup.Item>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">Discussions</div>
            </div>
            <Badge bg="primary" pill>
              {forumStat.totalDiscussions}
            </Badge>
          </ListGroup.Item>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">Discussion Tags</div>
            </div>
            <Badge bg="primary" pill>
              {forumStat.totalTags}
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
              {forumStat.totalComments}
            </Badge>
          </ListGroup.Item>
        </ListGroup>
      </Card.Body>
    </Card>
  );
};

ForumInfo.propTypes = {
  form: PropTypes.object.isRequired,
};

export default ForumInfo;
