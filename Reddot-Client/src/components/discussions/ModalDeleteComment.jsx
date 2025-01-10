import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";

//Service
import { deleteComment } from "../../services/forumService/CommentService";
import { logOutSuccess } from "../../redux/authSlice";
import { createAxios } from "../../services/createInstance";
import { useParams } from "react-router-dom";

const ModalDeleteComment = (props) => {
  const { discussionId } = useParams();
  const { show, handleClose, commentDelete, handleEditDeleteCommentModel } =
    props;

  ModalDeleteComment.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    commentDelete: PropTypes.object.isRequired,
    handleEditDeleteCommentModel: PropTypes.func.isRequired,
  };

  // const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, logOutSuccess);

  const handleSaveForumGroup = async () => {
    let res = await deleteComment(
      commentDelete.commentId,
      discussionId,
      currentUser?.accessToken,
      axiosJWT,
    );

    if (res && +res.data?.status === 200) {
      handleClose();
      handleEditDeleteCommentModel();
      toast.success(res.data.message);
    } else if (+res.data?.status === 202) {
      toast.error(res.data.message);
    } else {
      toast.error("Error when deleting Forum Group");
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
        <Modal.Title>Delete Forum Group</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div>
          <p>
            This action can not be undone! Do want to delete this forum group?
          </p>
          <b>Title: {commentDelete?.title}</b>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveForumGroup()}>
          Delete
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalDeleteComment;
