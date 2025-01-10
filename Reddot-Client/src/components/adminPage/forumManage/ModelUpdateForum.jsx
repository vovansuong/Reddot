import { useState, useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";

//Service
import { logOutSuccess } from "../../../redux/authSlice";
import { createAxios } from "../../../services/createInstance";
import { updateForum } from "../../../services/forumService/ForumService";
import { getAllForumGroup } from "../../../services/forumService/ForumGroupService";
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

const ModelUpdateForum = (props) => {
  const {
    show,
    handleClose,
    dataUpdateForum,
    forumIsActive,
    handleUpdateForumFromModel,
  } = props;

  ModelUpdateForum.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    dataUpdateForum: PropTypes.object.isRequired,
    forumIsActive: PropTypes.bool.isRequired,
    handleUpdateForumFromModel: PropTypes.func.isRequired,
  };

  const [forumGroup, setForumGroup] = useState([]);
  const [updateForumByGroupId, setUpdateForumByGroupId] = useState("");
  const [titleForumGroup, setTitleForumGroup] = useState("");

  const handleSelectForumTitle = (event) => {
    const selectedTitle = event.target.value;
    setTitleForumGroup(selectedTitle);
    const selectedForum = forumGroup.find((f) => f.title === selectedTitle);
    if (selectedForum) {
      setUpdateForumByGroupId(selectedForum.id);
    }
  };

  const listForumGroups = async () => {
    let res = await getAllForumGroup();
    if (res && res.data) {
      setForumGroup(res.data);
    }
  };

  const [title, setTitle] = useState(dataUpdateForum?.title || "");
  const [icon, setIcon] = useState(dataUpdateForum?.icon || "");
  const [color, setColor] = useState(dataUpdateForum?.color || "#ffffff");
  const [isActive, setIsActive] = useState(forumIsActive || true);
  const [description, setDescription] = useState(
    dataUpdateForum?.description || "",
  );

  // const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, logOutSuccess);

  const updateForumObject = {
    ...dataUpdateForum,
    title: title,
    icon: icon,
    color: color,
    description: description,
    active: forumIsActive,
  };

  //Error
  const [forum, setForum] = useState([]);
  const listForums = async () => {
    let res = await getAllForum();
    if (res && res.data) {
      const filterData = res.data.filter(
        (data) => data.title !== dataUpdateForum?.title,
      );
      setForum(filterData);
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

    let res = await updateForum(
      dataUpdateForum.id,
      updateForumByGroupId,
      updateForumObject,
      currentUser?.accessToken,
      axiosJWT,
    );

    if (res && +res.data?.status === 200) {
      handleClose();
      handleUpdateForumFromModel({
        id: dataUpdateForum.id,
        title: title,
        icon: icon,
        color: color,
        description: description,
        active: isActive,
      });
      toast.success(res.data.message);
    } else {
      toast.error("Error when updating Forum");
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
    if (show && dataUpdateForum) {
      setTitle(dataUpdateForum.title || "");
      setIcon(dataUpdateForum.icon || "");
      setColor(dataUpdateForum.color || "#ffffff");
      setDescription(dataUpdateForum.description || "");
      setIsActive(forumIsActive);
      setTitleForumGroup(dataUpdateForum.forumGroup?.title || "");
      setUpdateForumByGroupId(dataUpdateForum.forumGroup?.id || 0);
      listForums();
    }
    listForumGroups();
  }, [dataUpdateForum, forumIsActive, show]);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="md"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Update Forum</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className="form-group mb-3">
          <label className="form-label" htmlFor="forumGroupTitle">
            Select Forum Group
          </label>
          <select
            className="form-control mb-3"
            id="forumGroupTitle"
            value={titleForumGroup}
            onChange={handleSelectForumTitle}
          >
            {forumGroup.map((item) => (
              <option key={item.id} value={item.title}>
                {item.title}
              </option>
            ))}
          </select>
        </div>
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
        <Button variant="primary" onClick={handleSaveForum}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModelUpdateForum;
