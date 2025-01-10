import { useState, useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import { useSelector, useDispatch } from "react-redux";
import { createAxios } from "../../../services/createInstance";

//Service
import { addForumGroup } from "../../../services/forumService/ForumGroupService";
import { logOutSuccess } from "../../../redux/authSlice";
import { getUserModerator } from "../../../services/userService/UserService";
import { getAllForumGroup } from "../../../services/forumService/ForumGroupService";

//Color Picker
import ColorComponents from "../../colorComponents/ColorComponents";

//Icon
import SelectIcon from "../../IconComponents/IconComponents";

//Utils
import {
  validateTitle,
  validateIcon,
  validateColor,
} from "../../../utils/validForumAndDiscussionUtils";

const ModelAddForumGroup = (props) => {
  const { show, handleClose, handleUpdateTable } = props;

  ModelAddForumGroup.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateTable: PropTypes.func.isRequired,
  };

  const [title, setTitle] = useState("");
  const [icon, setIcon] = useState("");
  const [color, setColor] = useState("#ffffff");
  const [roleName, setRoleName] = useState("");

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, logOutSuccess);

  const [listModerator, setListModerator] = useState([]);
  const ListUserModerator = async () => {
    let res = await getUserModerator(currentUser?.accessToken, axiosJWT);
    if (res && +res.status === 200) {
      setListModerator(res.data);
    }
  };

  //Forum Groups
  const [forumGroup, setForumGroup] = useState([]);
  const listForumGroup = async () => {
    let res = await getAllForumGroup();
    if (res && res.data) {
      setForumGroup(res.data);
    }
  };

  const [titleError, setTitleError] = useState("");
  const [iconError, setIconError] = useState("");
  const [colorError, setColorError] = useState("");

  const addForumGroupObject = {
    title: title,
    icon: icon,
    color: color,
  };

  const handleSaveForumGroup = async () => {
    setTitleError("");
    setIconError("");
    setColorError("");

    let titleValidationError = validateTitle(title, forumGroup);
    let iconValidationError = validateIcon(icon);
    let colorValidationError = validateColor(color);

    if (titleValidationError) {
      setTitleError(titleValidationError);
    }
    if (iconValidationError) {
      setIconError(iconValidationError);
    }
    if (colorValidationError) {
      setColorError(colorValidationError);
    }

    if (titleValidationError || iconValidationError || colorValidationError) {
      toast.error("Please fill in all required fields");
      return;
    }

    if (roleName.trim() === "") {
      setRoleName(listModerator[0]?.username || "");
    }

    let res = await addForumGroup(
      addForumGroupObject,
      roleName,
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.data?.status === 201) {
      handleClose();
      setTitle("");
      setIcon("");
      setColor("#ffffff");
      handleUpdateTable({
        id: res.data.data.id,
        title: title,
        icon: icon,
        color: color,
        manager: roleName,
      });
      toast.success(res.data.message);
    } else {
      toast.error("Error when creating Forum Group");
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
    setRoleName(listModerator[0]?.username || "");
    if (show) {
      listForumGroup();
    }
  }, [show]);

  useEffect(() => {
    ListUserModerator();
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
        <Modal.Title>Add New Forum Group</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className="form-group mb-3">
          <label className="form-label">Select Moderators</label>
          <select
            className="form-control mb-3"
            id="roleName"
            value={roleName}
            onChange={(event) => setRoleName(event.target.value)}
          >
            {listModerator.map((item) => (
              <option key={item.id} value={item.username}>
                {item.username}
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
          {titleError && <div className="text-danger mt-1">{titleError}</div>}
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
        <Button variant="primary" onClick={() => handleSaveForumGroup()}>
          Add new
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModelAddForumGroup;
