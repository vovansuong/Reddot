import { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";
import { createAxios } from "../../../services/createInstance";

//Service
import { updateBannedKeyword } from "../../../services/bannedKeywordService/BannedKeywordService";
import { loginSuccess } from "../../../redux/authSlice";
import { getAllBannedKeywords } from "../../../services/bannedKeywordService/BannedKeywordService";

//Utils
import { validateKeyword } from "../../../utils/validForumAndDiscussionUtils";

const ModalUpdateBanned = (props) => {
  const { show, handleClose, handleUpdateEditBanned, dataEditBanned } = props;

  ModalUpdateBanned.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateEditBanned: PropTypes.func.isRequired,
    dataEditBanned: PropTypes.object.isRequired,
  };

  const [keyword, setKeyword] = useState("");

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const [keywordError, setKeywordError] = useState("");

  const [listBanned, setListBanned] = useState([]);

  const getAllBannedData = async () => {
    const res = await getAllBannedKeywords(currentUser?.accessToken, axiosJWT);
    if (res && +res.status === 200) {
      const filterData = res.data.filter(
        (data) => data.keyword !== dataEditBanned.keyword,
      );
      setListBanned(filterData);
      toast.success(res?.data?.message);
    } else {
      toast.error(res?.data?.message);
    }
  };

  const dataUpdate = {
    keyword: keyword,
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

    let res = await updateBannedKeyword(
      dataEditBanned.id,
      dataUpdate,
      currentUser?.accessToken,
      axiosJWT,
    );
    console.log(res);
    if (res && +res?.status === 200) {
      handleClose();
      setKeyword("");
      handleUpdateEditBanned({
        ...dataEditBanned,
        id: res.data.id,
        keyword: keyword,
      });
      toast.success(res.message);
    } else {
      toast.error("Error when creating banned keyword");
    }
  };

  useEffect(() => {
    if (dataEditBanned) {
      setKeyword(dataEditBanned.keyword);
    }
    getAllBannedData();
  }, [dataEditBanned]);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="md"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Update Banned Keyword</Modal.Title>
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
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalUpdateBanned;
