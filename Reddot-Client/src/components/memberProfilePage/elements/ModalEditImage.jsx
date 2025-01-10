import { useState } from "react";
import { Button, Modal, Col } from "react-bootstrap";
import PropTypes from "prop-types";
import { toast } from "react-toastify";
import { useSelector } from "react-redux";

import NoAvatar from "../../../assets/img/default-avatar.png";
import { fetchImage } from "../../../services/userService/UserService";

const ModalEditImage = (props) => {
  const { show, handleClose, handleUpdateAvatar } = props;

  const [file, setFile] = useState(null);
  const [img, setImg] = useState(null);
  let avatarUser = useSelector((state) => state.avatar.avatar?.name);
  let currentUser = useSelector((state) => state.auth.login?.currentUser);

  const reader = new FileReader();
  const loadFile = (event) => {
    const fileImage = event.target.files[0];
    reader.readAsDataURL(fileImage);

    setFile(fileImage);

    reader.addEventListener("load", (event) => {
      const url = event.target.result;
      setImg(url);
    });

    return null;
  };

  const handleCloseModal = () => {
    setImg(null);
    handleClose();
  };

  const handleUpload = () => {
    if (!file) {
      toast.error("Please select file");
      return;
    }
    handleUpdateAvatar(file);
    return null;
  };

  function getAvatar() {
    if (avatarUser) {
      return fetchImage(avatarUser);
    }
    if (currentUser.imageUrl) {
      return currentUser.imageUrl;
    }
    if (currentUser.avatar) {
      return fetchImage(currentUser.avatar);
    }
    return NoAvatar;
  }

  return (
    <Modal
      show={show}
      onHide={handleCloseModal}
      backdrop="static"
      size="lg"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Edit avatar</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Col className="d-flex justify-content-center mb-3">
          <div className="">
            <img
              src={img ?? getAvatar()}
              alt="avatar"
              id="ava"
              style={{
                height: `300px`,
                width: `300px`,
                objectFit: `cover`,
                borderRadius: `50%`,
              }}
            />
          </div>
        </Col>
        <Col className="text-center mb-3">
          <label htmlFor="file">Choose new file</label>
          <input
            type="file"
            name="file"
            className="d-none"
            id="file"
            onChange={(event) => loadFile(event)}
          />
          <p className="mt-3">Note: the size of the image file.</p>
        </Col>
      </Modal.Body>

      <Modal.Footer>
        <Button
          variant="secondary"
          onClick={handleCloseModal}
          className="ml-0 me-auto"
        >
          Close
        </Button>
        <Button
          variant="primary"
          onClick={handleUpload}
          className="ml-auto me-0"
        >
          Save change
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ModalEditImage.propTypes = {
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  handleUpdateAvatar: PropTypes.func.isRequired,
};

export default ModalEditImage;
