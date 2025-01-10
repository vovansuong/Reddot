import { Button, Modal } from "react-bootstrap";
import PropTypes from "prop-types";

const ModalConfirmDeleteUser = (props) => {
  const { show, handleClose, handleDelete, user } = props;

  const handleConfirmDeleteUser = () => handleDelete(user);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="md"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title className="text-danger">
          <i className="fa-solid fa-triangle-exclamation"></i> Delete user{" "}
          {user?.username}
        </Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <p className="text-center">
          Are you sure you want to delete this user?
        </p>
      </Modal.Body>

      <Modal.Footer>
        <Button
          variant="secondary"
          onClick={handleClose}
          className="ml-0 me-auto"
        >
          Close
        </Button>
        <Button
          variant="danger"
          onClick={handleConfirmDeleteUser}
          className="ml-auto me-0"
        >
          Delete User
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ModalConfirmDeleteUser.propTypes = {
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  handleDelete: PropTypes.func.isRequired,
  user: PropTypes.object.isRequired,
};

export default ModalConfirmDeleteUser;
