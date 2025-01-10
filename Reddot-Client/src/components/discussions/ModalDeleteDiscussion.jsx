import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

//Service
import { deleteDiscussion } from "../../services/forumService/DiscussionService";
import { logOutSuccess } from "../../redux/authSlice";
import { createAxios } from "../../services/createInstance";
import { useParams } from "react-router-dom";

const ModalDeleteDiscussion = (props) => {
  const { discussionId } = useParams();
  const { show, handleClose, titleDisc } = props;

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, logOutSuccess);

  const handleSaveDelete = async () => {
    let res = await deleteDiscussion(
      discussionId,
      currentUser?.accessToken,
      axiosJWT,
    );

    if (res && +res.data?.status === 200) {
      handleClose();
      toast.success(res.data.message);
      navigate("/admin/discussions");
    } else if (+res.data?.status === 202) {
      toast.error(res.data.message);
    } else {
      toast.error("Error when deleting comment");
    }
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="lg"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Delete Comment</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div>
          <p>This action can not be undone! Do want to delete this comment?</p>
          <b>Title: {titleDisc?.title}</b>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveDelete()}>
          Delete
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ModalDeleteDiscussion.propTypes = {
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  titleDisc: PropTypes.object.isRequired,
};

export default ModalDeleteDiscussion;
