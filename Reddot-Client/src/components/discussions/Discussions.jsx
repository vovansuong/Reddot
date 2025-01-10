import Table from "react-bootstrap/Table";
import { Link } from "react-router-dom";
import BannerTop from "../bannerTop/BannerTop";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import LastCommentInfo from "../lastCommentInfo/lastCommentInfo";
import { useNavigate } from "react-router-dom";

//Model
import ForumInfo from "./ForumInfo";
import ModalAddDiscussion from "./ModalAddDiscussion";

//Services
import { getForumById } from "../../services/forumService/ForumService";
import { getPageDiscussion } from "../../services/forumService/DiscussionService";

//Util
import { formatDate } from "../../utils/FormatDateTimeHelper";

//Paginate
import Pagination from "../pagination/Pagination";

//Scss
import "./Discussion.scss";

import { Card, Row, Col } from "reactstrap";
const Discussion = () => {
  const { forumId } = useParams();

  const [forum, setForum] = useState({});
  const [listDiscussions, setListDiscussions] = useState([]);

  const [showModelAddDiscussion, setShowModelAddDiscussion] = useState(false);

  const navigate = useNavigate();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);

  const handleClose = () => {
    setShowModelAddDiscussion(false);
  };

  const listForums = async () => {
    let res = await getForumById(forumId);
    if (res && res.data) {
      setForum(res.data);
    }
  };

  //Pagination
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);

  const handlePageClick = (event) => {
    setPage(event.selected + 1);
  };

  const listDiscussionsByForum = async () => {
    const res = await getPageDiscussion(page, 5, "id", "ASC", "", forumId);
    if (res && res.data) {
      const filterClosed = res.data.filter(
        (discussion) => discussion.closed === true,
      );
      setListDiscussions(filterClosed);
      setTotalPages(res.totalPages);
    } else {
      setListDiscussions([]);
    }
  };

  const handelCheckLoginAndAddNewDiscussion = () => {
    if (currentUser?.accessToken) {
      setShowModelAddDiscussion(true);
    } else {
      navigate("/login");
    }
  };

  const handleUpdateAddDiscussion = (discussion) => {
    setListDiscussions([discussion, ...listDiscussions]);
    listForums();
  };

  const breadcrumbs = [
    { id: 1, name: `${forum.forumGroup?.title}`, link: `/forumGroup` },
    { id: 2, name: `${forum.title}`, link: `/forum/${forum.id}` },
  ];

  useEffect(() => {
    listDiscussionsByForum();
    listForums();
  }, []);

  return (
    <section className="discussion-container content mb-3">
      <Col>
        <BannerTop bannerName={forum.title} breadcrumbs={breadcrumbs} />
      </Col>
      <Col className="mx-auto row">
        <Row>
          <Col md="8" lg="9">
            <Card>
              <Table striped hover>
                <thead>
                  <tr>
                    <th>Discussion Title</th>
                    <th>Replies</th>
                    <th>Views</th>
                    <th>Last Post</th>
                  </tr>
                </thead>
                <tbody>
                  {listDiscussions?.map((item) => {
                    return (
                      <tr key={item.id} className="m-2">
                        <td>
                          <h4>
                            <Link
                              to={`/discussion/${item.id}`}
                              className="text-decoration-none text-dark text-title"
                            >
                              {item.title}
                            </Link>
                          </h4>
                          <span>{item.createdBy} </span>
                          <span>{formatDate(item.createdAt)}</span>
                          {item?.tags?.map((tag) => (
                            <span key={tag.id}>
                              <button
                                className="btn btn-sm mx-2"
                                style={{ backgroundColor: tag.color }}
                              >
                                <i className="fa-solid fa-tag"></i>{" "}
                                <span style={{ color: "white" }}>
                                  {tag?.label}
                                </span>
                              </button>
                            </span>
                          ))}
                        </td>
                        <td>{item.comments?.length}</td>
                        <td>{item.stat.viewCount}</td>
                        <td style={{ maxWidth: "300px" }}>
                          <LastCommentInfo id={item.id} type="discussion" />
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </Table>
              <div className="pagination pagination-end">
                <Pagination
                  handlePageClick={handlePageClick}
                  pageSize={5}
                  totalPages={totalPages}
                />
              </div>
            </Card>
          </Col>
          <Col md="4" lg="3">
            <Card>
              <button
                className="btn btn-success w-100 h-100 m-0"
                onClick={() => handelCheckLoginAndAddNewDiscussion()}
              >
                <i className="fa-solid fa-circle-plus fa-xl"></i>{" "}
                <span>Open New Discussion</span>
              </button>
            </Card>
            <ForumInfo forum={forum} listDiscussions={listDiscussions} />
          </Col>
        </Row>
      </Col>

      <ModalAddDiscussion
        show={showModelAddDiscussion}
        handleClose={handleClose}
        forumId={forumId}
        handleUpdateAddDiscussion={handleUpdateAddDiscussion}
      />
    </section>
  );
};

export default Discussion;
