import { useState, useEffect } from "react";
import { Button, Modal } from "react-bootstrap";
import { toast } from "react-toastify";
import PropTypes from "prop-types";
import ReactQuill from "react-quill";
import { useSelector, useDispatch } from "react-redux";

//Service
import { loginSuccess } from "../../../redux/authSlice";
import { createAxios } from "../../../services/createInstance";
import { updateComment } from "../../../services/forumService/CommentService";
import "../../discussions/Discussion.scss";

const ModalUpdateComment = (props) => {
  const { show, handleClose, dataUpdateComment, handleEditCommentFromModel } =
    props;

  ModalUpdateComment.propTypes = {
    show: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    dataUpdateComment: PropTypes.object.isRequired,
    handleEditCommentFromModel: PropTypes.func.isRequired,
  };

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const commentData = {
    title: title,
    content: content,
  };

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const handleSaveComment = async () => {
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

  useEffect(() => {
    if (show) {
      setTitle(dataUpdateComment.title);
      setContent(dataUpdateComment.content);
    }
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
            onChange={(event) => setTitle(event.target.value)}
            placeholder="Enter Title"
          />
        </div>

        <div className="form-group mb-3">
          <label htmlFor="content">Content</label>
          <ReactQuill
            theme="snow"
            modules={module}
            value={content}
            onChange={setContent}
            id="content"
            placeholder="Enter content here"
            className="content-editor"
          />
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

export default ModalUpdateComment;
