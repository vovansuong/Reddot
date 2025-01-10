import { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";
import { createAxios } from "../../../services/createInstance";

//Service
import { createBannedKeyword } from "../../../services/bannedKeywordService/BannedKeywordService";
import { loginSuccess } from "../../../redux/authSlice";
import { getAllBannedKeywords } from "../../../services/bannedKeywordService/BannedKeywordService";

//Utils
import { validateKeyword } from "../../../utils/validForumAndDiscussionUtils";

const ModalAddNewBanned = (props) => {
  const { show, handleClose, handleUpdateAddNewBanned } = props;

  ModalAddNewBanned.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateAddNewBanned: PropTypes.func.isRequired,
  };

  const [keyword, setKeyword] = useState("");

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const addNewBanned = {
    keyword: keyword,
  };

  const [keywordError, setKeywordError] = useState("");

  const [listBanned, setListBanned] = useState([]);

  const getAllBannedData = async () => {
    const res = await getAllBannedKeywords(currentUser?.accessToken, axiosJWT);
    if (res && +res.status === 200) {
      setListBanned(res.data);
      toast.success(res?.data?.message);
    } else {
      toast.error(res?.data?.message);
    }
  };

  const handleSaveKeyword = async () => {
    setKeywordError("");

    let keywordValidationError = validateKeyword(keyword, listBanned);

    if (keywordValidationError) {
      setKeywordError(keywordValidationError);
    }

    if (keywordValidationError) {
      toast.error("Please fill in all required fields");
      return;
    }

    let res = await createBannedKeyword(
      addNewBanned,
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.data?.status === 200) {
      handleClose();
      setKeyword("");
      handleUpdateAddNewBanned({
        id: res.data.data.id,
        keyword: keyword,
      });
      toast.success(res.data.message);
    } else {
      toast.error("Error when creating banned keyword");
    }
  };

  useEffect(() => {
    getAllBannedData();
  }, []);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="md"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Add New Banned Keyword</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className="form-group mb-3">
          <label className="form-label" htmlFor="label">
            Keyword
          </label>
          <input
            className="form-control"
            id="label"
            type="text"
            value={keyword}
            onChange={(event) => {
              setKeyword(event.target.value);
              setKeywordError("");
            }}
            placeholder="Enter label"
          />
          {keywordError && <div className="text-danger">{keywordError}</div>}
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveKeyword()}>
          Add new
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalAddNewBanned;
