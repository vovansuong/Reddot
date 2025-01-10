import { useEffect, useState } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import ReactQuill from "react-quill";
import { useSelector, useDispatch } from "react-redux";
import { useParams } from "react-router-dom";

//Service
import { loginSuccess } from "../../redux/authSlice";
import { createAxios } from "../../services/createInstance";
import { createComment } from "../../services/forumService/CommentService";
import "./Discussion.scss";
import { getAllComments } from "../../services/forumService/CommentService";
import { getAllBannedKeywords } from "../../services/bannedKeywordService/BannedKeywordService";

//Utils
import {
  validateContent,
  replaceBannedWords,
} from "../../utils/validForumAndDiscussionUtils";

const ModalAddNewReply = (props) => {
  const { show, handleClose, handleUpdateAddReply, replyToId = null } = props;
  const { discussionId } = useParams();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const commentAdd = {
    title: "Reply",
    content: content,
  };

  // const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const [contentError, setContentError] = useState("");

  const [listComment, setListComment] = useState([]);
  const getAllDataComments = async () => {
    const res = await getAllComments();
    if (res && +res.data.status === 200) {
      setListComment(res.data.data);
    }
  };

  const handleSaveReply = async () => {
    setContentError("");

    let contentValidationError = validateContent(content, listComment);

    if (contentValidationError) {
      setContentError(contentValidationError);
    }

    if (contentValidationError) {
      toast.error("Please fill in all required fields");
      return;
    }

    try {
      let res = await createComment(
        discussionId,
        commentAdd,
        replyToId,
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
        handleUpdateAddReply({
          replyToId: replyToId,
          id: res.data.data.id,
          title: title,
          content: content,
          createdAt: res.data.data.createdAt,
          createdBy: res.data.data.createdBy,
        });
        toast.success(res.data.message);
      } else {
        toast.error("Error when creating Comment");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("Error when creating Comment");
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

  const handleContentChange = (value) => {
    const filteredContent = replaceBannedWords(value, list);
    setContent(filteredContent);
    setContentError(""); // Reset contentError when content changes
  };

  useEffect(() => {
    getAllDataComments();
    getBannedKeywords();
  }, []);

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop="static"
      size="lg"
      keyboard={false}
    >
      <Modal.Header closeButton>
        <Modal.Title>Add New Reply</Modal.Title>
      </Modal.Header>

      <Modal.Body>
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
        <Button variant="primary" onClick={() => handleSaveReply()}>
          Add new
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ModalAddNewReply.propTypes = {
  show: PropTypes.bool.isRequired,
  handleClose: PropTypes.func.isRequired,
  handleUpdateAddReply: PropTypes.func.isRequired,
  replyToId: PropTypes.number,
};

export default ModalAddNewReply;
