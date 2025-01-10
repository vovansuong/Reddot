import { Card, ListGroup, Badge } from "react-bootstrap";
import PropTypes from "prop-types";
import { formatDifferentUpToNow } from "../../utils/FormatDateTimeHelper";
import { useEffect } from "react";

const ForumInfo = (props) => {
  const { forum, listDiscussions } = props;

  const totalTags = listDiscussions.reduce(
    (acc, discussion) => acc + discussion.tags.length,
    0,
  );

  useEffect(() => {}, [forum, listDiscussions]);
  return (
    <Card>
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
              <div className="fw-bold">
                Forum : <span>{forum?.title}</span>
              </div>
            </div>
          </ListGroup.Item>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div className="fw-bold">Discussions</div>
            </div>
            <Badge bg="primary" pill>
              {listDiscussions?.length}
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
              {totalTags}
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
              {forum.stat?.commentCount}
            </Badge>
          </ListGroup.Item>
          <ListGroup.Item
            as="li"
            className="d-flex justify-content-between align-items-start"
          >
            <div className="ms-2 me-auto">
              <div>
                {forum.stat?.lastComment && (
                  <li>
                    <div className="fw-bold">Last Comment: </div>
                    {forum.stat?.lastComment.commenter} -{" "}
                    {forum.stat?.lastComment.commentDate
                      ? formatDifferentUpToNow(
                          forum.stat?.lastComment.commentDate,
                        )
                      : ""}
                  </li>
                )}
              </div>
            </div>
          </ListGroup.Item>
        </ListGroup>
      </Card.Body>
    </Card>
  );
};

ForumInfo.propTypes = {
  forum: PropTypes.object.isRequired,
  listDiscussions: PropTypes.array.isRequired,
};

export default ForumInfo;
