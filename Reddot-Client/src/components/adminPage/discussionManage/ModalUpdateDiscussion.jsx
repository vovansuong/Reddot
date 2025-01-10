import { useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";

//Service
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import { updateDetailsDiscussion } from "../../../services/forumService/DiscussionService";

const ModalUpdateDiscussion = (props) => {
  const {
    show,
    handleClose,
    dataUpdateDiscussion,
    action,
    handleEditDiscussion,
    dataDiscussion,
  } = props;

  ModalUpdateDiscussion.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    dataUpdateDiscussion: PropTypes.bool.isRequired,
    action: PropTypes.string.isRequired,
    handleEditDiscussion: PropTypes.func.isRequired,
    dataDiscussion: PropTypes.object.isRequired,
  };

  const discussion = {
    id: dataDiscussion.id,
    closed: action === "closed" ? dataUpdateDiscussion : dataDiscussion.closed,
    important:
      action === "important" ? dataUpdateDiscussion : dataDiscussion.important,
  };

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const handleSaveDiscussion = async () => {
    try {
      let res = await updateDetailsDiscussion(
        dataDiscussion.id,
        discussion,
        currentUser?.accessToken,
        axiosJWT,
      );

      if (res && +res.data?.status === 200) {
        handleClose();
        handleEditDiscussion({
          ...dataDiscussion,
          closed:
            action === "closed" ? dataUpdateDiscussion : dataDiscussion.closed,
          important:
            action === "important"
              ? dataUpdateDiscussion
              : dataDiscussion.important,
        });
        toast.success(res.data.message);
      } else {
        toast.error("Error when updating Discussion");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("Error when updating Discussion");
    }
  };

  useEffect(() => {}, [dataDiscussion]);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="lg"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>
          Update <b>{action === "closed" ? "Closed" : "Important"}</b>
        </Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className="form-group mb-3">
          Do want to set the status of this{" "}
          <span
            style={{
              backgroundColor: dataUpdateDiscussion ? "green" : "red",
              padding: "5px 10px",
              borderRadius: "5px",
              color: "white",
            }}
          >
            {action === "closed" ? "closed" : "important"}
          </span>{" "}
          to{" "}
          <b style={{ color: dataUpdateDiscussion ? "green" : "red" }}>
            {action === "closed"
              ? dataUpdateDiscussion
                ? "OPEN"
                : "CLOSED"
              : dataUpdateDiscussion
                ? "YES"
                : "NO"}
          </b>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveDiscussion()}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalUpdateDiscussion;
