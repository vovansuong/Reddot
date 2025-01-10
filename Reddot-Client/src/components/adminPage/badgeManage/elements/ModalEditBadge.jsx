import { useEffect, useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import PropTypes from "prop-types";
//Color Picker
import { ChromePicker } from "react-color";

import { validateString, validateNumber } from "../../../../utils/validUtils";

const ModalEditBadge = (props) => {
  const { show, handleClose, badge, handleEditBadge } = props;

  const [badgeName, setBadgeName] = useState(badge?.name);
  const [badgeDescription, setBadgeDescription] = useState(badge?.description);
  const [badgeIcon, setBadgeIcon] = useState(badge?.icon);
  const [badgeColor, setBadgeColor] = useState(badge?.color);

  const [totalScore, setTotalScore] = useState(badge?.totalScore);
  const [totalDiscussion, setTotalDiscussion] = useState(
    badge?.totalDiscussion,
  );
  const [totalComment, setTotalComment] = useState(badge?.totalComment);
  const [action, setAction] = useState(badge.action);

  const [validName, setValidName] = useState(true);
  const [validDes, setValidDes] = useState(true);
  const [validIcon, setValidIcon] = useState(true);
  const [validColor, setValidColor] = useState(true);
  const [validTotalScore, setValidTotalScore] = useState(true);
  const [validTotalDiscussion, setValidTotalDiscussion] = useState(true);
  const [validTotalComment, setValidTotalComment] = useState(true);

  useEffect(() => {
    setValidName(validateString(badgeName));
  }, [badgeName]);

  useEffect(() => {
    setValidDes(validateString(badgeDescription));
  }, [badgeDescription]);

  useEffect(() => {
    setValidIcon(validateString(badgeIcon));
  }, [badgeIcon]);

  useEffect(() => {
    setValidColor(validateString(badgeColor));
  }, [badgeColor]);

  useEffect(() => {
    setValidTotalScore(validateNumber(totalScore));
  }, [totalScore]);

  useEffect(() => {
    setValidTotalDiscussion(validateNumber(totalDiscussion));
  }, [totalDiscussion]);

  useEffect(() => {
    setValidTotalComment(validateNumber(totalComment));
  }, [totalComment]);

  const handleUpdate = () => {
    //validation
    if (!validName) {
      return;
    }

    // update
    const newBadge = {
      id: badge.id,
      name: badgeName,
      description: badgeDescription,
      icon: badgeIcon,
      color: badgeColor,
      totalScore: totalScore,
      totalDiscussion: totalDiscussion,
      totalComment: totalComment,
      action: action,
    };
    handleEditBadge(newBadge);
    handleClose();
  };

  useEffect(() => {
    setBadgeName(badge.name);
    setBadgeDescription(badge.description);
    setBadgeIcon(badge.icon);
    setBadgeColor(badge.color);
    setTotalScore(badge.totalScore);
    setTotalDiscussion(badge.totalDiscussion);
    setTotalComment(badge.totalComment);
    setAction(badge.action);
  }, [badge]);

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Edit Badge</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group controlId="formBadgeName">
            <Form.Label>Badge Name</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter badge name"
              value={badgeName}
              onChange={(e) => setBadgeName(e.target.value)}
            />
            {validName ? (
              ""
            ) : (
              <small className="text-danger">Name is not empty</small>
            )}
          </Form.Group>
          <Form.Group controlId="formBadgeDescription">
            <Form.Label>Badge Description</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter badge description"
              value={badgeDescription}
              onChange={(e) => setBadgeDescription(e.target.value)}
            />
            {validDes ? (
              ""
            ) : (
              <small className="text-danger">Name is not empty</small>
            )}
          </Form.Group>
          <Form.Group controlId="formBadgeIcon">
            <Form.Label>Badge Icon</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter badge image URL"
              value={badgeIcon}
              onChange={(e) => setBadgeIcon(e.target.value)}
            />
            {validIcon ? (
              ""
            ) : (
              <small className="text-danger">Name is not empty</small>
            )}
          </Form.Group>

          <div className="form-group mb-3">
            <label className="form-label" htmlFor="title">
              Color
            </label>
            <ChromePicker
              color={badgeColor}
              onChangeComplete={(color) => setBadgeColor(color.hex)}
            />
            {validColor ? (
              ""
            ) : (
              <small className="text-danger">Name is not empty</small>
            )}
          </div>

          <Form.Group controlId="formTotalScore">
            <Form.Label>Total Score</Form.Label>
            <Form.Control
              type="number"
              placeholder="Enter...."
              value={totalScore}
              onChange={(e) => setTotalScore(e.target.value)}
            />
            {validTotalScore ? (
              ""
            ) : (
              <small className="text-danger">Name is not empty</small>
            )}
          </Form.Group>

          <Form.Group controlId="formTotalDiscussion">
            <Form.Label>Total Discussion</Form.Label>
            <Form.Control
              type="number"
              placeholder="Enter...."
              value={totalDiscussion}
              onChange={(e) => setTotalDiscussion(e.target.value)}
            />
            {validTotalDiscussion ? (
              ""
            ) : (
              <small className="text-danger">Name is not empty</small>
            )}
          </Form.Group>

          <Form.Group controlId="formTotalComment">
            <Form.Label>Total Comment</Form.Label>
            <Form.Control
              type="number"
              placeholder="Enter...."
              value={totalComment}
              onChange={(e) => setTotalComment(e.target.value)}
            />
            {validTotalComment ? (
              ""
            ) : (
              <small className="text-danger">Name is not empty</small>
            )}
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={handleUpdate}>
          Save Changes
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ModalEditBadge.propTypes = {
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  badge: PropTypes.object.isRequired,
  handleEditBadge: PropTypes.func.isRequired,
};

export default ModalEditBadge;
