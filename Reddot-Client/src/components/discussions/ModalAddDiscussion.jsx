import { useState, useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import ReactQuill from "react-quill";
import { useNavigate, useParams } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import "./Discussion.scss";

//Service
import { loginSuccess } from "../../redux/authSlice";
import { createAxios } from "../../services/createInstance";
import { createDiscussion } from "../../services/forumService/DiscussionService";
import { getPageDiscussion } from "../../services/forumService/DiscussionService";
import { getAllBannedKeywords } from "../../services/bannedKeywordService/BannedKeywordService.jsx";

//Utils
import {
  validateTitle,
  validateContentDiscussion,
  replaceBannedWords,
} from "../../utils/validForumAndDiscussionUtils";

const ModalAddDiscussion = (props) => {
  const { show, handleClose, handleUpdateAddDiscussion } = props;
  const { forumId } = useParams();

  const navigate = useNavigate();

  ModalAddDiscussion.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleUpdateAddDiscussion: PropTypes.func.isRequired,
  };

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const discussion = {
    title: title,
    closed: true,
    sticky: false,
    important: false,
  };

  const [titleError, setTitleError] = useState("");
  const [contentError, setContentError] = useState("");

  //Discussion
  const [discussionList, setDiscussionList] = useState([]);
  const listDiscussions = async () => {
    const res = await getPageDiscussion(1, 10, "id", "ASC", "", null);
    if (res && res.data) {
      setDiscussionList(res.data);
    } else {
      setDiscussionList([]);
    }
  };

  // const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const handleSaveDiscussion = async () => {
    setTitleError("");
    setContentError("");

    let titleValidationError = validateTitle(title, discussionList);
    let contentValidationError = validateContentDiscussion(
      content,
      discussionList,
    );

    if (titleValidationError) {
      setTitleError(titleValidationError);
    }

    if (contentValidationError) {
      setContentError(contentValidationError);
    }

    if (titleValidationError || contentValidationError) {
      toast.error("Please fill in all required fields");
      return;
    }

    try {
      let res = await createDiscussion(
        forumId,
        discussion,
        content,
        currentUser?.accessToken,
        axiosJWT,
      );
      if (+res?.data?.status === 407) {
        toast.error(res?.data?.message);
        return;
      }
      if (res && +res.data?.status === 201) {
        handleClose();
        setContent("");
        setTitle("");
        handleUpdateAddDiscussion({
          ...discussion,
          createdBy: res.data.data.createdBy,
          id: res.data.data.id,
          content: content,
          forum: res.data.data.forum,
          stat: res.data.data.stat,
          createdAt: res.data.data.createdAt,
        });
        navigate(`/discussion/${res.data.data.id}`);
        toast.success(res.data.message);
      } else {
        toast.error("Error when creating Discussion");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("Error when creating Discussion");
    }
  };

  const [list, setList] = useState([]);

  const getBannedKeywords = async () => {
    try {
      const res = await getAllBannedKeywords(
        currentUser?.accessToken,
        axiosJWT,
      );
      if (res && res.data) {
        setList(res.data);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleTitleChange = (event) => {
    const filteredTitle = replaceBannedWords(event.target.value, list);
    setTitle(filteredTitle);
    setTitleError(""); // Reset titleError when title changes
  };

  const handleContentChange = (value) => {
    const filteredContent = replaceBannedWords(value, list);
    setContent(filteredContent);
    setContentError(""); // Reset contentError when content changes
  };

  useEffect(() => {
    listDiscussions();
    getBannedKeywords();
  }, []);

  const toolbarOptions = [
    ["bold", "italic", "underline", "strike"], // toggled buttons
    ["blockquote", "code-block"],
    ["link", "image", "video", "formula"],

    [{ header: 1 }, { header: 2 }], // custom button values
    [{ list: "ordered" }, { list: "bullet" }, { list: "check" }],
    [{ script: "sub" }, { script: "super" }], // superscript/subscript
    [{ indent: "-1" }, { indent: "+1" }], // outdent/indent
    [{ direction: "rtl" }], // text direction

    [{ size: ["small", false, "large", "huge"] }], // custom dropdown
    [{ header: [1, 2, 3, 4, 5, 6, false] }],

    [{ color: [] }, { background: [] }], // dropdown with defaults from theme
    [{ font: [] }],
    [{ align: [] }],

    ["clean"], // remove formatting button
  ];

  const module = {
    toolbar: toolbarOptions,
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
        <Modal.Title>Add New Discussion</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <div className="">
          <div className="form-group mb-3">
            <label className="form-label" htmlFor="title">
              Title
            </label>
            <input
              className="form-control"
              id="title"
              type="text"
              value={title}
              onChange={handleTitleChange}
              placeholder="Enter Title"
            />
            {titleError && <div className="text-danger mt-1">{titleError}</div>}
          </div>

          <div className="form-group mb-3">
            <label htmlFor="content">Content</label>
            <ReactQuill
              theme="snow"
              modules={module}
              value={content}
              onChange={handleContentChange}
              id="content"
              placeholder="Enter content here"
              className="content-editor"
            />
          </div>
          {contentError && (
            <div className="text-danger mt-1">{contentError}</div>
          )}
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveDiscussion()}>
          Add new
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalAddDiscussion;
