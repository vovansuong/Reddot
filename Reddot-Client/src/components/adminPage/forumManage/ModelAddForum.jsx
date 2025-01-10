import { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";

//Service
import { addForum } from "../../../services/forumService/ForumService";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import { getAllForum } from "../../../services/forumService/ForumService";

//Color Picker
import ColorComponents from "../../colorComponents/ColorComponents";

//Icon
import SelectIcon from "../../IconComponents/IconComponents";

//Utils
import {
  validateTitle,
  validateIcon,
  validateColor,
  validateDescription,
} from "../../../utils/validForumAndDiscussionUtils";

const ModelAddForum = (props) => {
  const { show, handleClose, handleUpdateForum, idForumGroup } = props;

  ModelAddForum.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateForum: PropTypes.func.isRequired,
    idForumGroup: PropTypes.number.isRequired,
  };

  const [title, setTitle] = useState("");
  const [icon, setIcon] = useState("");
  const [color, setColor] = useState("#ffffff");
  const [description, setDescription] = useState("");

  // const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const addForumObject = {
    idForumGroup: idForumGroup,
    title: title,
    icon: icon,
    color: color,
    description: description,
  };

  //Error
  const [forum, setForum] = useState([]);
  const listForums = async () => {
    let res = await getAllForum();
    if (res && res.data) {
      setForum(res.data);
    }
  };

  const [titleError, setTitleError] = useState("");
  const [iconError, setIconError] = useState("");
  const [colorError, setColorError] = useState("");
  const [descriptionError, setDescriptionError] = useState("");

  const handleSaveForum = async () => {
    setTitleError("");
    setIconError("");
    setColorError("");
    setDescriptionError("");

    let titleValidationError = validateTitle(title, forum);
    let iconValidationError = validateIcon(icon);
    let colorValidationError = validateColor(color);
    let descriptionValidationError = validateDescription(description, forum);

    if (titleValidationError) {
      setTitleError(titleValidationError);
    }
    if (iconValidationError) {
      setIconError(iconValidationError);
    }
    if (colorValidationError) {
      setColorError(colorValidationError);
    }

    if (descriptionValidationError) {
      setDescriptionError(descriptionValidationError);
    }

    if (
      titleValidationError ||
      iconValidationError ||
      colorValidationError ||
      descriptionValidationError
    ) {
      toast.error("Please fill in all required fields");
      return;
    }

    let res = await addForum(
      addForumObject,
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.data?.status === 201) {
      handleClose();
      setTitle("");
      setIcon(null);
      setColor("#ffffff");
      setDescription("");
      handleUpdateForum({
        id: res.data.data.id,
        description: description,
        title: title,
        icon: icon,
        color: color,
      });
      toast.success(res.data.message);
    } else {
      toast.error("Error when creating Forum");
    }
  };

  const handleSelectIcon = (iconValue) => {
    setIcon(iconValue);
    setIconError("");
  };

  const handleChangeComplete = (color) => {
    setColor(color.hex);
    setColorError("");
  };

  useEffect(() => {
    listForums();
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
        <Modal.Title>Add New Forum</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className="form-group mb-3">
          <label className="form-label" htmlFor="title">
            Title
          </label>
          <input
            className="form-control"
            id="title"
            type="text"
            value={title}
            onChange={(event) => {
              setTitle(event.target.value);
              setTitleError("");
            }}
            placeholder="Enter Title"
          />
        </div>
        {titleError && <div className="text-danger mt-1">{titleError}</div>}
        <div className="form-group mb-3">
          <label className="form-label" htmlFor="description">
            Description
          </label>
          <textarea
            className="form-control"
            id="description"
            value={description}
            onChange={(event) => {
              setDescription(event.target.value);
              setDescriptionError("");
            }}
            placeholder="Enter Description"
          />
          {descriptionError && (
            <div className="text-danger mt-1">{descriptionError}</div>
          )}
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
        <Button variant="primary" onClick={() => handleSaveForum()}>
          Add new
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModelAddForum;
