import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";
import { createAxios } from "../../../services/createInstance";

//Service
import { updateTag } from "../../../services/tagService/tagService";
import { loginSuccess } from "../../../redux/authSlice";

const ModalSetStatusTags = (props) => {
  const { show, handleClose, handleUpdateEditTags, dataEditTag } = props;

  ModalSetStatusTags.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateEditTags: PropTypes.func.isRequired,
    dataEditTag: PropTypes.object.isRequired,
  };

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const addNewTag = {
    ...dataEditTag,
    disabled: !dataEditTag?.disabled,
  };
  const handleSaveTag = async () => {
    let res = await updateTag(addNewTag, currentUser?.accessToken, axiosJWT);
    if (res && +res.data?.status === 200) {
      handleClose();
      handleUpdateEditTags({
        ...dataEditTag,
        id: res.data.data.id,
        disabled: !dataEditTag?.disabled,
      });
      toast.success("Set tag successfully");
    } else {
      toast.error("Error when updating Tag");
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
        <Modal.Title>Update Tag</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div>
          <p>
            Do want to set the status of this tag to{" "}
            <b style={{ color: dataEditTag?.disabled ? "red" : "green" }}>
              {dataEditTag?.disabled ? "disabled" : "enabled"}
            </b>
          </p>
          <b>Title: {dataEditTag?.title}</b>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveTag()}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalSetStatusTags;
