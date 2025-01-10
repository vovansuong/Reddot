import { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";
import { createAxios } from "../../../services/createInstance";

//Service
import { createTag } from "../../../services/tagService/tagService";
import { loginSuccess } from "../../../redux/authSlice";
import { getAllTags } from "../../../services/tagService/tagService";

//Color Picker
import ColorComponents from "../../colorComponents/ColorComponents";

//Icon
import SelectIcon from "../../IconComponents/IconComponents";

//Utils
import {
  validateLabel,
  validateIcon,
  validateColor,
} from "../../../utils/validForumAndDiscussionUtils";
const ModalAddNewTags = (props) => {
  const { show, handleClose, handleUpdateAddNewTags } = props;

  ModalAddNewTags.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateAddNewTags: PropTypes.func.isRequired,
  };

  const [label, setLabel] = useState("");
  const [icon, setIcon] = useState("");
  const [color, setColor] = useState("#ffffff");

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const addNewTag = {
    label: label,
    icon: icon,
    color: color,
  };

  const [labelError, setLabelError] = useState("");
  const [iconError, setIconError] = useState("");
  const [colorError, setColorError] = useState("");

  //All Tags
  const [listTags, setListTags] = useState([]);

  const getAllTagsData = async () => {
    const res = await getAllTags(
      1,
      5,
      "id",
      "ASC",
      "",
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.status === 201) {
      setListTags(res.data.data);
      toast.success(res?.data?.message);
    } else {
      toast.error(res?.data?.message);
    }
  };

  const handleSaveTag = async () => {
    setLabelError("");
    setIconError("");
    setColorError("");

    let labelValidationError = validateLabel(label, listTags);
    let iconValidationError = validateIcon(icon);
    let colorValidationError = validateColor(color);

    if (labelValidationError) {
      setLabelError(labelValidationError);
    }
    if (iconValidationError) {
      setIconError(iconValidationError);
    }
    if (colorValidationError) {
      setColorError(colorValidationError);
    }

    if (labelValidationError || iconValidationError || colorValidationError) {
      toast.error("Please fill in all required fields");
      return;
    }

    let res = await createTag(addNewTag, currentUser?.accessToken, axiosJWT);
    if (res && +res.data?.status === 201) {
      handleClose();
      setLabel("");
      setIcon(null);
      setColor("#ffffff");
      handleUpdateAddNewTags({
        id: res.data.data.id,
        label: label,
        icon: icon,
        color: color,
        disabled: res.data.data.disabled,
      });
      toast.success(res.data.message);
    } else {
      toast.error("Error when creating Tags");
    }
  };

  const handleSelectIcon = (iconValue) => {
    setIcon(iconValue);
  };

  const handleChangeComplete = (color) => {
    setColor(color.hex);
    setColorError("");
  };

  useEffect(() => {
    getAllTagsData();
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
        <Modal.Title>Add New Tag</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className="form-group mb-3">
          <label className="form-label" htmlFor="label">
            Label
          </label>
          <input
            className="form-control"
            id="label"
            type="text"
            value={label}
            onChange={(event) => {
              setLabel(event.target.value);
              setLabelError("");
            }}
            placeholder="Enter label"
          />
          {labelError && <div className="text-danger mt-1">{labelError}</div>}
        </div>
        <SelectIcon handleSelectIcon={handleSelectIcon} icon={icon} />
        {iconError && <div className="text-danger mt-1">{iconError}</div>}
        <ColorComponents
          handleChangeComplete={handleChangeComplete}
          color={color}
        />
        {colorError && <div className="text-danger mt-1">{colorError}</div>}
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveTag()}>
          Add new
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalAddNewTags;
