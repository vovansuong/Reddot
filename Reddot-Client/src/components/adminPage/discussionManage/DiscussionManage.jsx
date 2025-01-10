import Table from "react-bootstrap/Table";
import { useState, useEffect } from "react";
import { debounce } from "lodash";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

//Service
import { getPageDiscussion } from "../../../services/forumService/DiscussionService";
import { getAllForum } from "../../../services/forumService/ForumService";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import { checkRoleDiscussion } from "../../../services/forumService/DiscussionService";

//Paginate
import Pagination from "../../pagination/Pagination";

//Modal
import LastCommentInfo from "../../lastCommentInfo/lastCommentInfo";

//Utils
import {
  formatDifferentUpToNow,
  formatDate,
} from "../../../utils/FormatDateTimeHelper";

const DiscussionManage = () => {
  //Pagination
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const [orderBy, setOrderBy] = useState("id");
  const [sort, setSort] = useState("ASC");
  const [search, setSearch] = useState("");
  const [forumId, setForumId] = useState(null);

  const handlePageClick = (event) => {
    setPage(event.selected + 1);
  };

  //Discussion
  const [discussionList, setDiscussionList] = useState([]);
  const listDiscussions = async () => {
    const res = await getPageDiscussion(
      page,
      size,
      orderBy,
      sort,
      search,
      forumId,
    );
    if (res && res.data) {
      setDiscussionList(res.data);
      setTotalPages(res.totalPages);
    } else {
      setDiscussionList([]);
    }
  };

  //Filter
  const [isFilterVisible, setIsFilterVisible] = useState(false);

  const handleFilterDiscussionByForum = () => {
    setIsFilterVisible(!isFilterVisible);
  };

  //Forum
  const [forumList, setForumList] = useState([]);
  const listForums = async () => {
    const res = await getAllForum();
    if (res && res.data) {
      setForumList(res.data);
    } else {
      setForumList([]);
    }
  };

  const handleFilterDiscussionByForumChange = debounce((e) => {
    const keyId = e.target.value;
    if (keyId === "") {
      setForumId(null);
      listDiscussions();
    }
    setForumId(keyId);
  }, 500);

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const handleCheckRole = async (discussionId) => {
    const res = await checkRoleDiscussion(
      discussionId,
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.status === 200) {
      if (
        currentUser?.username === res.data.data.roleName ||
        currentUser?.username === "admin"
      ) {
        navigate(`/admin/discussion/${discussionId}`);
      } else {
        toast.error("You don't have permission");
      }
    }
  };

  useEffect(() => {
    listDiscussions();
    listForums();
  }, [page, size, orderBy, sort, search, forumId]);

  return (
    <div className="content">
      <div className="d-flex justify-content-between align-items-center">
        <div className="">
          <button
            className="btn btn-primary"
            onClick={() => {
              setForumId(null);
              listDiscussions();
            }}
          >
            All Discussion
          </button>
          <button
            className="btn btn-primary"
            onClick={handleFilterDiscussionByForum}
          >
            Filter by Forum
          </button>
          {isFilterVisible && (
            <select
              className="form-control"
              onChange={(e) => handleFilterDiscussionByForumChange(e)}
            >
              <option value="">Select Forum</option>
              {forumList.map((forum) => (
                <option key={forum.id} value={forum.id}>
                  {forum.title}
                </option>
              ))}
              {/* Add more options as needed */}
            </select>
          )}
        </div>

        <div className="d-flex col-6">
          <input
            type="text"
            className="form-control"
            placeholder="Search....."
            onChange={(e) => setSearch(e.target.value)}
          />
          <div className="ms-2 col-2">
            <select
              className="form-control"
              onChange={(e) => setSize(e.target.value)}
              value={size}
            >
              <option value="5">5 per page</option>
              <option value="8">8 per page</option>
              <option value="10">10 per page</option>
              <option value="15">15 per page</option>
            </select>
          </div>
        </div>
      </div>
      <Table striped bordered hover size="sm">
        <thead>
          <tr>
            <th className="sort_header">
              Discussion Title
              <span>
                <i
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => {
                    setOrderBy("id");
                    setSort("desc");
                  }}
                ></i>
                <i
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => {
                    setOrderBy("id");
                    setSort("asc");
                  }}
                ></i>
              </span>
            </th>
            <th className="sort_header">
              Comments
              <span>
                <i
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => {
                    setOrderBy("stat.commentCount");
                    setSort("desc");
                  }}
                ></i>
                <i
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => {
                    setOrderBy("stat.commentCount");
                    setSort("asc");
                  }}
                ></i>
              </span>
            </th>
            <th className="sort_header">
              Started
              <span>
                <i
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => {
                    setOrderBy("createdAt");
                    setSort("desc");
                  }}
                ></i>
                <i
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => {
                    setOrderBy("createdAt");
                    setSort("asc");
                  }}
                ></i>
              </span>
            </th>
            <th>Last Comment</th>
          </tr>
        </thead>
        <tbody>
          {discussionList.map((discussion) => (
            <tr key={discussion.id}>
              <td>
                <div
                  onClick={() => handleCheckRole(discussion.id)}
                  style={{ cursor: "pointer", color: "blue" }}
                >
                  {discussion.title}
                </div>
                <span>
                  Started by <b>{discussion.createdBy}</b> about{" "}
                  {discussion.createdAt
                    ? formatDifferentUpToNow(discussion.createdAt)
                    : ""}
                </span>
              </td>
              <td>{discussion.stat?.commentCount}</td>
              <td>{formatDate(discussion.createdAt)}</td>
              <td style={{ maxWidth: "300px" }}>
                <LastCommentInfo id={discussion.id} type="discussion" />
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <div className="pagination pagination-end">
        <Pagination
          handlePageClick={handlePageClick}
          pageSize={size}
          totalPages={totalPages}
        />
      </div>
    </div>
  );
};

export default DiscussionManage;
