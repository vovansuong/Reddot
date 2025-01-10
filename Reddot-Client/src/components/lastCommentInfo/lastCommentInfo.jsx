import Avatar from "../avatar/Avatar";
import { Col } from "react-bootstrap";
import { useState, useEffect } from "react";
import _ from "lodash";
import { fetchImage } from "../../services/userService/UserService";

import { formatDifferentUpToNow } from "../../utils/FormatDateTimeHelper";
import { getLastCommentServiceResponseForum } from "../../services/forumService/ForumService";
import { getLastCommentServiceResponseDiscussion } from "../../services/forumService/DiscussionViewService";
import PropTypes from "prop-types";
import "./lastComment.scss";

const LastCommentInfo = (props) => {
  const { id, type } = props;

  const [comment, setComment] = useState({});

  const getLastComment = async () => {
    if (type === "forum") {
      const res = await getLastCommentServiceResponseForum(id);
      if (res && +res.status === 200) {
        let cloneData = _.cloneDeep(comment);
        cloneData = res.data;
        setComment(cloneData);
      }
    }

    if (type === "discussion") {
      const res = await getLastCommentServiceResponseDiscussion(id);
      if (res && +res.status === 200) {
        let cloneData = _.cloneDeep(comment);
        cloneData = res.data;
        setComment(cloneData);
      }
    }
  };

  function getAvatar(user) {
    if (user?.imageUrl) {
      return user.imageUrl;
    }
    if (user?.avatar) {
      return fetchImage(user.avatar);
    }
    return "";
  }

  useEffect(() => {
    getLastComment();
  }, []);

  return (
    <div className="row d-flex">
      <Col lg={3} sm={6}>
        <Avatar src={getAvatar(comment?.author)} height={40} width={40} />
      </Col>
      <Col lg={9} sm={6}>
        <b className="text-ellipsis">{comment?.title}</b>
        <small
          className="text-ellipsis-content"
          style={{ fontSize: "12px", fontWeight: "400", fontStyle: "italic" }}
        >
          {comment?.contentAbbr}
        </small>{" "}
        <small>
          {comment?.commenter} -{" "}
          {comment?.commentDate
            ? formatDifferentUpToNow(comment?.commentDate)
            : ""}
        </small>
      </Col>
    </div>
  );
};

LastCommentInfo.propTypes = {
  id: PropTypes.number.isRequired,
  type: PropTypes.string,
};

export default LastCommentInfo;
