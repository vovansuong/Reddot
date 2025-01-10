import PropTypes from "prop-types";
import { useCallback, useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";

import Avatar from "../../avatar/Avatar";
import { fetchImage } from "../../../services/userService/UserService";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";

import { getAllCommentByUsername } from "../../../services/userService/userHistory";
import { formatDifferentUpToNow } from "../../../utils/FormatDateTimeHelper";

import Pagination from "../../pagination/Pagination";

const ActivitiesProfile = (props) => {
  const { username, userInfo } = props;

  const [listComment, setListComment] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [orderBy, setOrderBy] = useState("updatedAt");
  const [sortBy, setSortBy] = useState("DESC");

  let currentUser = useSelector((state) => state.auth.login?.currentUser);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const urlAvatarUser = () => {
    if (userInfo.imageUrl) {
      return userInfo.imageUrl;
    }
    if (userInfo.avatar) {
      return fetchImage(userInfo.avatar);
    }
    return null;
  };

  const fetchDataComment = useCallback(async () => {
    let pageData = {
      page: page,
      size: pageSize,
      orderBy: orderBy,
      sort: sortBy,
      username,
    };
    let res = await getAllCommentByUsername(
      pageData,
      axiosJWT,
      currentUser.accessToken,
      navigate,
    );
    if (+res?.status === 200) {
      const { pageSize, totalPages, data } = res.data;
      setListComment(data);
      setPageSize(pageSize);
      setTotalPages(totalPages);
    } else {
      console.log("error", res?.message);
    }
  }, [username, page]);

  const handlePageClick = (event) => {
    setPage(+event.selected + 1);
  };

  useEffect(() => {
    fetchDataComment();
  }, [username]);

  return (
    <div className="card card-activities">
      <div className="card-header">Activities History</div>
      <div className="card-body">
        <div className="table-full-width table-responsive">
          <table className="table">
            <tbody>
              {listComment.length > 0 ? (
                listComment?.map((item) => (
                  <tr key={item.commentId}>
                    <td className="col-1" style={{ verticalAlign: "top" }}>
                      <p className="">
                        <Avatar
                          src={urlAvatarUser()}
                          username=""
                          height={50}
                          width={50}
                        />
                      </p>
                    </td>
                    <td className="col-auto">
                      <p className="">
                        <b>{item?.author}</b>{" "}
                        {item?.firstComment
                          ? " post "
                          : " comment in the thread "}
                        <Link
                          to={`/discussion/${item.discussionId}`}
                          className="text-decoration-none"
                        >
                          {item.discussionTitle}
                        </Link>
                        {item?.vote?.voteValue &&
                          (item.vote?.voteValue > 0
                            ? "with vote"
                            : " with downvote ")}
                      </p>
                      <div
                        className="text-muted"
                        dangerouslySetInnerHTML={{ __html: item?.content }}
                      ></div>
                      <small className="text-muted">
                        {item?.author === username ? (
                          ""
                        ) : (
                          <Link
                            to={"/member-profile/" + item.author}
                            className="text-decoration-none"
                          >
                            {item?.author}{" "}
                          </Link>
                        )}
                        created at:{" "}
                        {item?.createdAt &&
                          formatDifferentUpToNow(item?.createdAt)}
                      </small>
                    </td>
                    {item?.author === currentUser?.username && (
                      <td className="col-1" style={{ verticalAlign: "top" }}>
                        <button
                          className="btn-link"
                          color="info"
                          id="tooltip636901683"
                          title=""
                          type="button"
                        >
                          <i className="fa fa-edit" />
                        </button>
                      </td>
                    )}
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3">
                    <div className="text-center">
                      <h4>No data</h4>
                      <Link
                        to="/list-discussion"
                        className="text-decoration-none"
                      >
                        View more
                      </Link>
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        <Pagination
          handlePageClick={handlePageClick}
          pageSize={+pageSize}
          totalPages={+totalPages}
        />
      </div>
    </div>
  );
};

ActivitiesProfile.propTypes = {
  username: PropTypes.string.isRequired,
  userInfo: PropTypes.object.isRequired,
};

export default ActivitiesProfile;
