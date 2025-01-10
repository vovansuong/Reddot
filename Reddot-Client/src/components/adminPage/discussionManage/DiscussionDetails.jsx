import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import _ from "lodash";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import SelectMulti from "../selectMulti/SelectMulti";
import { Row, Col } from "reactstrap";
import { Link } from "react-router-dom";

//Service
import { getDiscussionById } from "../../../services/forumService/DiscussionService";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import { getAllTags } from "../../../services/tagService/tagService";
import { setDataListTags } from "../../../services/forumService/DiscussionService";
import { upVote, downVote } from "../../../services/voteService/voteService";
import { getAllCommentByDiscussionId } from "../../../services/forumService/DiscussionViewService";
import { fetchImage } from "../../../services/userService/UserService";
import { registerBookmark } from "../../../services/bookmarkService/bookmarkService";

//Utils
import {
  formatDate,
  formatLongDate,
} from "../../../utils/FormatDateTimeHelper";

//Modal
import ModalUpdateDiscussion from "./ModalUpdateDiscussion";
import ModalUpdateComment from "./ModalUpdateComment";
import ModalDeleteDiscussion from "./ModalDeleteDiscussion";

//Modal
import Avatar from "../../avatar/Avatar";
import "../../../components/discussions/stylecomment.scss";
import ModalDeleteComment from "./ModalDeleteComment";

const DiscussionDetails = () => {
  //Discussion
  const { discussionId } = useParams();

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const [discussion, setDiscussion] = useState({});
  const [action, setAction] = useState("");
  const [dataUpdateDiscussion, setDataUpdateDiscussion] = useState(false);
  const [isShowUpdateDiscussion, setIsShowUpdateDiscussion] = useState(false);

  const discussionById = async () => {
    let res = await getDiscussionById(discussionId);
    if (res && res.data) {
      setDiscussion(res.data);
    }
  };

  const handleUpdateDataDiscussion = (data, action) => {
    setDataUpdateDiscussion(data);
    setAction(action);
    setIsShowUpdateDiscussion(true);
  };

  const handleEditDiscussion = (data) => {
    let cloneDiscussion = _.cloneDeep(data);
    cloneDiscussion = data;
    setDiscussion(cloneDiscussion);
  };

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
      // find tag by disabled true
      setListTags(res.data.data.filter((tag) => tag.disabled === true));
      toast.success(res?.data?.message);
    } else {
      toast.error(res?.data?.message);
    }
  };

  // Format tags for react-select
  const tagOptions = listTags?.map((tag) => ({
    value: tag.id,
    label: tag.label,
  }));

  const [selectedTags, setSelectedTags] = useState([]);

  const handleTagChange = (selectedOptions) => {
    setSelectedTags(selectedOptions);
  };

  const handleUpdateTags = async () => {
    const res = await setDataListTags(
      discussionId,
      selectedTags,
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.status === 200) {
      toast.success(res?.data?.message);
      discussionById();
      getAllTagsData();
    } else {
      toast.error(res?.data?.message);
    }
  };

  useEffect(() => {
    discussionById();
    getAllTagsData();
  }, [discussionId]);

  useEffect(() => {
    if (discussion?.tags) {
      setSelectedTags(
        discussion.tags.map((tag) => ({ value: tag.id, label: tag.label })),
      );
    }
  }, [discussion]);

  //*****************************//
  //****
  //**			Comment AND Reply			**//
  //****************************//

  const [listComment, setListComment] = useState([]);
  const [pageSize, setPageSize] = useState(10);

  const fetchAllCommentData = async () => {
    if (discussionId === null || discussionId <= 0) {
      return;
    }
    let pageData = {
      size: pageSize,
      orderBy: "createdAt",
      sort: "ASC",
      discussionId,
    };
    let res = await getAllCommentByDiscussionId(pageData);
    if (res?.data?.length > 0) {
      setListComment(res.data);
      setPageSize(res.pageSize);
    } else {
      console.log("error", res?.message);
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

  const handleUpVote = async (commentId) => {
    const vote = {
      commentId: commentId,
      voteName: currentUser.username,
      voteId: 1,
    };
    let res = await upVote(vote, currentUser?.accessToken, axiosJWT);
    if (res && +res.data.status === 200) {
      toast.success(res.data.message);
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
      fetchAllCommentData();
    } else {
      toast.error("Error when bookmark");
    }
  };

  //update Comment
  const [dataUpdateComment, setDataUpdateComment] = useState(null);
  const [showModelUpdateDiscussion, setShowModelUpdateComment] =
    useState(false);
  const handleEditComment = (comment) => {
    setDataUpdateComment(comment);
    setShowModelUpdateComment(true);
  };

  const handleEditCommentFromModel = (comment) => {
    let cloneListComments = _.cloneDeep(listComment);
    let index = cloneListComments.findIndex((c) => c.id === comment.id);
    cloneListComments[index] = comment;
    setListComment(cloneListComments);
    fetchAllCommentData();
  };

  //Delete Comment
  const [commentDelete, setCommentDelete] = useState(null);
  const [showModelDeleteComment, setShowModelDeleteComment] = useState(false);

  const handleClickDelete = (comment) => {
    setCommentDelete(comment);
    setShowModelDeleteComment(true);
  };

  const handleEditDeleteCommentModel = () => {
    fetchAllCommentData();
  };

  const [showModelDeleteDiscussion, setShowModelDeleteDiscussion] =
    useState(false);

  useEffect(() => {
    fetchAllCommentData();
  }, [discussionId]);

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
                    <button className="fa-solid fa-user-plus"></button>
                    <br />
                    <i className="fa-solid fa-star" alt="reputation"></i>
                    {comment?.author?.reputation}{" "}
                    <i className="fa-solid fa-pen"></i>{" "}
                    {comment?.author?.totalDiscussions}
                  </small>
                </span>

                <small className="ml-auto me-0 d-inline-block">
                  <button
                    onClick={() => handleEditComment(comment)}
                    className="mx-2 fa-solid fa-edit fa-2x"
                  ></button>
                  <button
                    className="mx-2 fa-solid fa-xmark fa-2x"
                    onClick={() => handleClickDelete(comment)}
                  ></button>
                </small>
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
                <button className="btn btn-sm mx-2">{tag?.label}</button>
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
                <div className="d-flex" style={{ paddingTop: "10px" }}>
                  <span
                    dangerouslySetInnerHTML={{ __html: reply?.content }}
                  ></span>
                  <span style={{ marginLeft: "6px" }}>
                    - {reply?.author?.username}{" "}
                    {formatLongDate(reply?.createdAt)}
                  </span>
                </div>
              </div>
            );
          })}
          <hr />
          <div className="card-footer d-flex justify-content-between">
            <span></span>
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
    <div className="content container mt-4 ">
      <p className="text-center">
        Started by <strong>{discussion?.createdBy}</strong> -{" "}
        {formatDate(discussion?.createdAt)}
      </p>
      <p className="text-center">
        In forum <strong>{discussion?.forum?.title}</strong>{" "}
      </p>
      <div className="d-flex justify-content-center align-items-center">
        <div className="d-flex">
          <div style={{ color: discussion?.closed ? "green" : "red" }}>
            This discussion is{" "}
            <span>{discussion?.closed ? "OPEN" : "CLOSED"}</span>
          </div>
          <button
            type="checkbox"
            label="This discussion is OPEN"
            onClick={() =>
              handleUpdateDataDiscussion(!discussion?.closed, "closed")
            }
          >
            <span
              style={{
                border: "1px solid black",
                borderRadius: "5px",
                padding: "5px",
              }}
            >
              {" "}
              <i className="fa-regular fa-circle-xmark"></i> Click to{" "}
              {discussion?.closed ? "CLOSED" : "OPEN"}
            </span>
          </button>
        </div>
        <div className="px-5">
          <label>
            Mark as{" "}
            <strong>
              {discussion?.important ? "Important" : "Not Important"}
            </strong>
          </label>
          <button
            type="checkbox"
            onClick={() =>
              handleUpdateDataDiscussion(!discussion.important, "important")
            }
          >
            <span
              style={{
                border: "1px solid black",
                borderRadius: "5px",
                padding: "5px",
              }}
            >
              {" "}
              <i className="fa-solid fa-x"></i>{" "}
              {discussion.important ? "No" : "Yes"}
            </span>
          </button>
        </div>
        <div>
          <button
            onClick={() => setShowModelDeleteDiscussion(true)}
            style={{
              background: "red",
              color: "white",
              padding: "5px",
              borderRadius: "5px",
            }}
          >
            <i className="fa-solid fa-triangle-exclamation"></i> Delete this
            Discussion ?
          </button>
        </div>
      </div>
      <div className="my-3 d-flex justify-content-center align-items-center">
        <label className="mx-2" style={{ fontWeight: "bold" }}>
          Select Tags
        </label>
        <SelectMulti
          tagOptions={tagOptions}
          selectedTags={selectedTags}
          handleTagChange={handleTagChange}
        />
        <button
          onClick={() => handleUpdateTags()}
          style={{
            background: "green",
            color: "white",
            padding: "5px",
            borderRadius: "5px",
          }}
        >
          <i className="fa-solid fa-check"></i> Apply
        </button>
      </div>
      <hr style={{ width: "100%", border: "2px solid black" }} />
      <p>
        There are {discussion?.stat?.commentCount} comments in this discussion.
        This discussion has been viewed {discussion?.stat?.viewCount} times
      </p>
      <ModalUpdateDiscussion
        show={isShowUpdateDiscussion}
        handleClose={() => setIsShowUpdateDiscussion(false)}
        dataUpdateDiscussion={dataUpdateDiscussion}
        action={action}
        dataDiscussion={discussion}
        handleEditDiscussion={handleEditDiscussion}
      />

      <Col className="mx-auto row">
        <Row>
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
          </section>
        </Row>
      </Col>
      <ModalUpdateComment
        show={showModelUpdateDiscussion}
        handleClose={() => setShowModelUpdateComment(false)}
        dataUpdateComment={dataUpdateComment}
        handleEditCommentFromModel={handleEditCommentFromModel}
      />
      <ModalDeleteComment
        show={showModelDeleteComment}
        handleClose={() => setShowModelDeleteComment(false)}
        commentDelete={commentDelete}
        handleEditDeleteCommentModel={handleEditDeleteCommentModel}
      />
      <ModalDeleteDiscussion
        show={showModelDeleteDiscussion}
        handleClose={() => setShowModelDeleteDiscussion(false)}
        discussion={discussion}
      />
    </div>
  );
};

export default DiscussionDetails;
