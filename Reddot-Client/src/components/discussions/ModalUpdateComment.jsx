import { useState, useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import ReactQuill from "react-quill";
import { useSelector, useDispatch } from "react-redux";

//Service
import { loginSuccess } from "../../redux/authSlice";
import { createAxios } from "../../services/createInstance";
import { updateComment } from "../../services/forumService/CommentService";
import "./Discussion.scss";
import { getAllComments } from "../../services/forumService/CommentService";
import { getAllBannedKeywords } from "../../services/bannedKeywordService/BannedKeywordService";

//Utils
import {
  validateTitle,
  validateContent,
  replaceBannedWords,
} from "../../utils/validForumAndDiscussionUtils";

const ModalUpdateComment = (props) => {
  const { show, handleClose, dataUpdateComment, handleEditCommentFromModel } =
    props;

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const commentData = {
    title: title,
    content: content,
  };

  //validate
  const [titleError, setTitleError] = useState("");
  const [contentError, setContentError] = useState("");

  const [listValidComment, setValidListComment] = useState([]);
  const getAllDataComments = async () => {
    const res = await getAllComments();
    if (res && +res.data.status === 200) {
      const filterData = res.data.data.filter(
        (data) => data.title !== dataUpdateComment.title,
      );
      setValidListComment(filterData);
    }
  };

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const handleSaveComment = async () => {
    setTitleError("");
    setContentError("");

    let titleValidationError = validateTitle(title, listValidComment);
    let contentValidationError = validateContent(content, listValidComment);

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
      let res = await updateComment(
        dataUpdateComment.commentId,
        commentData,
        currentUser?.accessToken,
        axiosJWT,
      );

      if (res && +res.data?.status === 200) {
        handleClose();
        setContent("");
        setTitle("");
        handleEditCommentFromModel({
          ...dataUpdateComment,
          content: content,
          title: title,
          id: dataUpdateComment.commentId,
        });
        toast.success(res.data.message);
      } else {
        toast.error("Error when updating Comment");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("Error when updating Comment");
    }
  };

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
    if (show) {
      setTitle(dataUpdateComment.title);
      setContent(dataUpdateComment.content);
      getAllDataComments();
    }
    getBannedKeywords();
  }, [dataUpdateComment, show]);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="lg"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Update Comment</Modal.Title>
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
          {contentError && (
            <div className="text-danger mt-1">{contentError}</div>
          )}
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleSaveComment()}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ModalUpdateComment.propTypes = {
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  dataUpdateComment: PropTypes.object.isRequired,
  handleEditCommentFromModel: PropTypes.func.isRequired,
};
export default ModalUpdateComment;
