import { useState, useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";

//Service
import { logOutSuccess } from "../../../redux/authSlice";
import { createAxios } from "../../../services/createInstance";
import { updateForum } from "../../../services/forumService/ForumService";

const ModelUpdateActiveForum = (props) => {
  const {
    show,
    handleClose,
    dataUpdateForum,
    forumIsActive,
    handleUpdateForumFromModel,
  } = props;

  ModelUpdateActiveForum.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    dataUpdateForum: PropTypes.object.isRequired,
    forumIsActive: PropTypes.bool.isRequired,
    handleUpdateForumFromModel: PropTypes.func.isRequired,
  };

  const [isActive, setIsActive] = useState(true);

  // const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, logOutSuccess);

  const updateForumObject = {
    ...dataUpdateForum,
    active: forumIsActive,
  };

  const handleSaveForum = async () => {
    let res = await updateForum(
      dataUpdateForum.id,
      dataUpdateForum.idForumGroup,
      updateForumObject,
      currentUser?.accessToken,
      axiosJWT,
    );

    if (res && +res.data?.status === 200) {
      handleClose();
      handleUpdateForumFromModel({
        ...dataUpdateForum,
        active: isActive,
      });
      toast.success(res.data.message);
    } else {
      toast.error("Error when updating active Forum");
    }
  };

  useEffect(() => {
    if (show) {
      setIsActive(forumIsActive);
    }
  }, [dataUpdateForum, forumIsActive, show]);
  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="lg"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Update Active Forum</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div>
          <h3>
            Do you want to{" "}
            <b className={forumIsActive ? "text-success" : "text-danger"}>
              {forumIsActive ? "active" : "inactive"}
            </b>{" "}
            this Forum?
          </h3>
          <b>Title: {dataUpdateForum?.title}</b>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveForum()}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModelUpdateActiveForum;
