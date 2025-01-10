import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";
import { createAxios } from "../../../services/createInstance";

//Service
import { loginSuccess } from "../../../redux/authSlice";
import { deleteBannedKeyword } from "../../../services/bannedKeywordService/BannedKeywordService";

const ModalDeleteBanned = (props) => {
  const { show, handleClose, handleUpdateDeleteBanned, dataDeleteBanned } =
    props;

  ModalDeleteBanned.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateDeleteBanned: PropTypes.func.isRequired,
    dataDeleteBanned: PropTypes.object.isRequired,
  };

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const handleSaveKeyword = async () => {
    let res = await deleteBannedKeyword(
      dataDeleteBanned.id,
      currentUser?.accessToken,
      axiosJWT,
    );
    console.log(res);
    if (res && +res?.status === 200) {
      handleClose();
      handleUpdateDeleteBanned(dataDeleteBanned);
      toast.success(res.message);
    } else {
      toast.error("Error when creating banned keyword");
    }
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="md"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Delete Banned Keyword</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div>
          <p>This action can not be undone! Do want to delete this keyword?</p>
          <b>Title: {dataDeleteBanned?.keyword}</b>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveKeyword()}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalDeleteBanned;
