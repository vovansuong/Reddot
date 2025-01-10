import BannerTop from "../bannerTop/BannerTop";
import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import ReactQuill from "react-quill";
import { useSelector, useDispatch } from "react-redux";
import { toast } from "react-toastify";
import { Card, Row, Col, Button } from "reactstrap";
import _ from "lodash";
import { useNavigate } from "react-router-dom";

//Service
import { loginSuccess } from "../../redux/authSlice";
import { createAxios } from "../../services/createInstance";
import { createComment } from "../../services/forumService/CommentService";
import { upVote, downVote } from "../../services/voteService/voteService";
import {
  getFirstCommentByDiscussionId,
  getAllCommentByDiscussionId,
} from "../../services/forumService/DiscussionViewService";
import { fetchImage } from "../../services/userService/UserService";
import { registerBookmark } from "../../services/bookmarkService/bookmarkService";
import { getDiscussionById } from "../../services/forumService/DiscussionService";
import { getAllComments } from "../../services/forumService/CommentService";
import { getAllBannedKeywords } from "../../services/bannedKeywordService/BannedKeywordService";

//Modal
import DiscussionInfo from "./DiscussionInfo";
import Avatar from "../avatar/Avatar";
import "./stylecomment.scss";
import ModalUpdateComment from "./ModalUpdateComment";
import ModalDeleteDiscussion from "./ModalDeleteDiscussion";
import ModalAddNewReply from "./ModalAddNewReply";
import Pagination from "../pagination/Pagination";
import ModalDeleteComment from "./ModalDeleteComment";

//Util
import { formatLongDate } from "../../utils/FormatDateTimeHelper";
import {
  validateTitle,
  validateContent,
  replaceBannedWords,
} from "../../utils/validForumAndDiscussionUtils";

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

const DiscussionDetails = () => {
  const { discussionId } = useParams();

  const [comments, setComments] = useState([]);

  const [replyToId, setReplyToId] = useState(null);

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const [content, setContent] = useState("");
  const [title, setTitle] = useState("");

  const [isShowAddNewComment, setIsShowAddNewComment] = useState(false);

  const [titleFG, setTitleFG] = useState({});
  const [titleForum, setTitleForum] = useState({});
  const [titleDisc, setTitleDisc] = useState({});

  const [firstComment, setFirstComment] = useState({});
  const [listComment, setListComment] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const fetchFirstCommentData = async () => {
    let res = await getFirstCommentByDiscussionId(discussionId);
    if (+res.status === 200 && res?.data) {
      setFirstComment(res.data?.commentInfo);
      setTitleFG({
        id: res.data.forumGroupId,
        title: res.data.forumGroupTitle,
      });

      setTitleForum({
        id: res.data.forumId,
        title: res.data.forumTitle,
      });

      setTitleDisc({
        id: res.data.discussionId,
        title: res.data.discussionTitle,
      });
    } else {
      console.log(`Error`, res?.message);
    }
  };

  const fetchAllCommentData = async () => {
    if (discussionId === null || discussionId <= 0) {
      return;
    }
    let pageData = {
      page: page,
      size: pageSize,
      orderBy: "createdAt",
      sort: "ASC",
      discussionId,
    };
    let res = await getAllCommentByDiscussionId(pageData);
    if (res?.data?.length > 0) {
      setListComment(res.data);
      setPageSize(res.pageSize);
      setTotalPages(res.totalPages);
    } else {
      console.log("error", res?.message);
    }
  };

  const commentAdd = {
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
      setValidListComment(res.data.data);
    }
  };

  const handleAddNewComment = async () => {
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
      let res = await createComment(
        discussionId,
        commentAdd,
        null,
        currentUser?.accessToken,
        axiosJWT,
      );
      if (+res?.data?.status === 407) {
        toast.error(res?.data?.message);
        return;
      }
      if (res && +res.data?.status === 201) {
        setIsShowAddNewComment(false);
        setContent("");
        setTitle("");
        fetchAllCommentData();
        getDiscussionByIdInfo();
        setComments([res.data.data, ...comments]);
        toast.success(res.data.message);
      } else {
        toast.error("Can not create Comment");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("Error when creating Comment");
    }
  };

  const isBookmarkOfCurrentUser = (comment) => {
    if (!comment) {
      return false;
    }
    const listBookmark = comment?.bookmarks;
    if (listBookmark === null) {
      return false;
    }
    const _name = currentUser?.username;
    const result = listBookmark?.some((item) => item?.bookmarkBy === _name);
    return result;
  };

  const urlAvatarUser = (author) => {
    if (author?.imageUrl) {
      return author.imageUrl;
    }
    if (author?.avatar) {
      return fetchImage(author.avatar);
    }
    return "";
  };

  const handlePageClick = (event) => {
    setPage(+event.selected + 1);
    return true;
  };

  const handleUpVote = async (commentId) => {
    const vote = {
      commentId: commentId,
      voteName: currentUser.username,
      voteId: 1,
    };
    let res = await upVote(vote, currentUser?.accessToken, axiosJWT);
    if (res && +res.data.status === 200) {
      toast.success(res.data.message);
      fetchFirstCommentData();
      fetchAllCommentData();
    } else if (+res?.data?.status === 400) {
      toast.error(res?.data?.message);
    } else {
      toast.error("Cannot vote");
    }
  };

  const handleDownVote = async (commentId) => {
    const vote = {
      commentId: commentId,
      voteName: currentUser.username,
      voteId: -1,
    };
    let res = await downVote(vote, currentUser?.accessToken, axiosJWT);
    if (res && +res.data.status === 200) {
      toast.success(res.data.message);
      fetchFirstCommentData();
      fetchAllCommentData();
    } else if (+res?.data?.status === 400) {
      toast.error(res?.data?.message);
    } else {
      toast.error("Cannot vote");
    }
  };

  const handleBookmark = async (comment) => {
    if (comment === null || comment?.commentId < 0) {
      return;
    }

    if (comment?.author?.username === currentUser?.username) {
      toast.error("User cannot bookmark own post comment");
      return;
    }
    const bookmarkData = {
      commentId: comment?.commentId,
      bookmarkBy: currentUser.username,
      bookmarked: isBookmarkOfCurrentUser(comment),
    };
    let res = await registerBookmark(
      bookmarkData,
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.data.status === 200) {
      toast.success(res.data.message);
      fetchFirstCommentData();
      fetchAllCommentData();
    } else {
      toast.error("Error when bookmark");
    }
  };

  //Add new Reply
  const [isShowAddNewReply, setIsShowAddNewReply] = useState(false);

  const handleReply = (commentId) => {
    if (currentUser?.accessToken) {
      setIsShowAddNewReply(true);
      setReplyToId(commentId);
    } else {
      navigate("/login");
    }
  };

  const handleUpdateAddReply = (reply) => {
    let cloneComments = _.cloneDeep(comments);
    let index = cloneComments.findIndex(
      (comment) => comment.commentId === reply.commentId,
    );
    cloneComments[index] = {
      ...cloneComments[index],
      replies: [
        {
          replyId: reply.id,
          createdAt: reply.createdAt,
          author: {
            username: reply.createdBy,
            avatar: null,
            imageUrl: null,
            reputation: null,
            totalDiscussions: null,
          },
          content: reply.content,
        },
      ],
    };
    setComments(cloneComments);
    fetchFirstCommentData();
    fetchAllCommentData();
    getDiscussionByIdInfo();
  };

  //update Comment
  const [dataUpdateComment, setDataUpdateComment] = useState({});
  const [showModelUpdateDiscussion, setShowModelUpdateComment] =
    useState(false);
  const handleEditDiscussion = (comment) => {
    setDataUpdateComment(comment);
    setShowModelUpdateComment(true);
  };

  const handleEditCommentFromModel = (comment) => {
    let cloneListComments = _.cloneDeep(comments);
    let index = cloneListComments.findIndex((c) => c.id === comment.id);
    cloneListComments[index] = comment;
    setListComment(cloneListComments);
    fetchFirstCommentData();
    fetchAllCommentData();
  };

  const [discussionInfo, setDiscussionInfo] = useState({});
  const getDiscussionByIdInfo = async () => {
    let res = await getDiscussionById(discussionId);
    if (res && res.data) {
      setDiscussionInfo(res.data);
    }
  };

  const handelCheckLoginAndAddNewComment = () => {
    if (currentUser?.accessToken) {
      setIsShowAddNewComment(true);
    } else {
      navigate("/login");
    }
  };

  //Delete Comment
  const [commentDelete, setCommentDelete] = useState({});
  const [showModelDeleteComment, setShowModelDeleteComment] = useState(false);

  const handleClickDelete = (comment) => {
    setCommentDelete(comment);
    setShowModelDeleteComment(true);
  };

  const handleEditDeleteCommentModel = () => {
    fetchFirstCommentData();
    fetchAllCommentData();
  };

  const [showModelDeleteDiscussion, setShowModelDeleteDiscussion] =
    useState(false);
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
    fetchFirstCommentData();
    fetchAllCommentData();
    getDiscussionByIdInfo();
    getAllDataComments();
    getBannedKeywords();
  }, [discussionId]);

  useEffect(() => {
    getAllDataComments();
  }, []);

  const breadcrumbs = [
    { id: 1, name: `${titleFG.title}`, link: `/forumGroup` },
    { id: 2, name: `${titleForum.title}`, link: `/forum/${titleForum.id}` },
    { id: 3, name: `${titleDisc.title}`, link: `/discussion/${discussionId}` },
  ];

  const commentCard = (comment) => {
    return (
      <Row>
        <div className="col-1 vote-container">
          <button
            className="vote fa-solid fa-caret-up"
            onClick={() => handleUpVote(comment?.commentId)}
          ></button>
          <button className="vote-count px-2 rounded-circle">
            {comment?.totalVotes ?? 0}
          </button>
          <button
            className="vote fa-solid fa-caret-down mb-3"
            onClick={() => handleDownVote(comment?.commentId)}
          ></button>
          <button
            className={
              isBookmarkOfCurrentUser(comment)
                ? "fa-solid fa-bookmark bookmark-checked"
                : "fa-regular fa-bookmark"
            }
            onClick={() => handleBookmark(comment)}
          ></button>
        </div>
        <div className="col-11">
          <div className="card-header d-flex justify-content-between">
            {comment?.author && (
              <>
                <span className="ml-0 me-auto">
                  <Link
                    to={`/member-profile/${comment?.author?.username}`}
                    className="text-decoration-none"
                  >
                    <Avatar
                      src={urlAvatarUser(comment?.author)}
                      username={"@" + comment?.author?.username}
                      height={36}
                      width={36}
                    />
                  </Link>
                  <small>
                    post at:
                    {comment?.createdAt && formatLongDate(comment?.createdAt)}
                    <div className="d-flex justify-content-start align-items-center">
                      <div>
                        <i className="fa-solid fa-star" alt="reputation"></i>
                        {comment?.author?.reputation}{" "}
                      </div>
                      <div className="mx-2">
                        <i className="fa-solid fa-pen"></i>{" "}
                        {comment?.author?.totalDiscussions}
                      </div>
                      <div style={{ color: comment?.author?.badgeColor }}>
                        <i className={comment?.author?.badgeIcon} />
                        {comment?.author?.badgeName}
                      </div>
                    </div>
                  </small>
                </span>

                {currentUser?.username === comment?.author?.username && (
                  <small className="ml-auto me-0 d-inline-block">
                    <button
                      onClick={() => handleEditDiscussion(comment)}
                      className="mx-2 fa-solid fa-edit fa-2x"
                    ></button>
                    {comment?.title !== titleDisc.title && (
                      <button
                        className="mx-2 fa-solid fa-xmark fa-2x"
                        onClick={() => handleClickDelete(comment)}
                      ></button>
                    )}
                  </small>
                )}
              </>
            )}
          </div>
          <hr />
          <div className="card-body">
            <div
              className="contentByDiscussion"
              dangerouslySetInnerHTML={{ __html: comment?.content }}
            ></div>
            {comment?.tags?.map((tag) => (
              <span key={tag.id}>
                <button
                  className="btn btn-sm mx-2"
                  style={{ backgroundColor: tag.color }}
                >
                  <i className="fa-solid fa-tag"></i>{" "}
                  <span style={{ color: "white" }}>{tag?.label}</span>
                </button>
              </span>
            ))}
          </div>
          {comment?.replies?.length > 0 && <hr />}
          {comment?.replies?.map((reply, index) => {
            const isLastReply = index === comment?.replies?.length - 1;
            return (
              <div
                key={reply?.replyId}
                className={`d-block ${isLastReply ? "" : "reply-container"}`}
                style={{ marginLeft: "20px" }}
              >
                <div style={{ paddingTop: "10px" }}>
                  <div
                    dangerouslySetInnerHTML={{ __html: reply?.content }}
                  ></div>
                  <div
                    style={{
                      fontSize: "12px",
                      textAlign: "right",
                    }}
                  >
                    {reply?.author?.username} {formatLongDate(reply?.createdAt)}
                  </div>
                </div>
              </div>
            );
          })}
          <hr />
          <div className="card-footer d-flex justify-content-between">
            <span>
              <button onClick={() => handleReply(comment?.commentId)}>
                <i className="fa-solid fa-reply"></i>Reply
              </button>
            </span>
            <span>
              <button>
                <i className="fa-regular fa-flag"></i>Report
              </button>
            </span>

            <span>
              <small>
                Edit at:{" "}
                {comment?.updatedAt && formatLongDate(comment?.updatedAt)}
              </small>
            </span>
          </div>
        </div>
      </Row>
    );
  };

  return (
    <section className="discussion-details content mb-3">
      <Col>
        <BannerTop bannerName={firstComment?.title} breadcrumbs={breadcrumbs} />
      </Col>

      <Col className="mx-auto row">
        <Row>
          <Col className="mb-3 col-12 col-md-8 col-lg-9">
            {/* first comment */}
            {firstComment && (
              <section className="card mb-3 p-3">
                {commentCard(firstComment)}
              </section>
            )}
            {/* first comment */}

            {/* list comment */}
            <section>
              {listComment?.map(
                (item) =>
                  item &&
                  !item.firstComment && (
                    <section className="card mb-3 p-3" key={item.commentId}>
                      {commentCard(item)}
                    </section>
                  ),
              )}{" "}
              {isShowAddNewComment && (
                <section className="card mb-3 p-3">
                  <div>
                    <b>Add new Comment</b>
                    <form>
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
                        {titleError && (
                          <div className="text-danger mt-1">{titleError}</div>
                        )}
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

                      <div className="mb-3">
                        <Button
                          type="reset"
                          className="btn btn-secondary w-25 mx-3"
                          onClick={() => setIsShowAddNewComment(false)}
                        >
                          Cancel
                        </Button>
                        <Button
                          className="btn btn-primary w-25"
                          onClick={() => handleAddNewComment()}
                        >
                          Add new
                        </Button>
                      </div>
                    </form>
                  </div>
                </section>
              )}
              <Card className="card">
                <button
                  className="btn btn-success w-100 h-100 m-0"
                  onClick={() => handelCheckLoginAndAddNewComment()}
                >
                  <i className="fa-solid fa-circle-plus"></i>
                  <></>
                  Add New Comment
                </button>
              </Card>
              <Pagination
                handlePageClick={handlePageClick}
                pageSize={+pageSize}
                totalPages={+totalPages}
              />
            </section>
            {/* list comment */}
          </Col>
          {/* right column */}
          <Col className="mb-3 col-12 col-md-4 col-lg-3">
            <DiscussionInfo discussionInfo={discussionInfo} />
          </Col>
        </Row>
      </Col>
      <ModalAddNewReply
        show={isShowAddNewReply}
        handleClose={() => setIsShowAddNewReply(false)}
        handleUpdateAddReply={handleUpdateAddReply}
        replyToId={replyToId}
      />
      <ModalUpdateComment
        show={showModelUpdateDiscussion}
        handleClose={() => setShowModelUpdateComment(false)}
        dataUpdateComment={dataUpdateComment ? dataUpdateComment : null}
        handleEditCommentFromModel={handleEditCommentFromModel}
      />
      <ModalDeleteComment
        show={showModelDeleteComment}
        handleClose={() => setShowModelDeleteComment(false)}
        commentDelete={commentDelete ? commentDelete : null}
        handleEditDeleteCommentModel={handleEditDeleteCommentModel}
      />
      <ModalDeleteDiscussion
        show={showModelDeleteDiscussion}
        handleClose={() => setShowModelDeleteDiscussion(false)}
        titleDisc={titleDisc}
      />
    </section>
  );
};

export default DiscussionDetails;
