import { useState, useEffect } from "react";
import _ from "lodash";
import { useSelector, useDispatch } from "react-redux";

//Service
import { getAllForumGroup } from "../../../services/forumService/ForumGroupService";
import { getAllForum } from "../../../services/forumService/ForumService";
import { getForumStat } from "../../../services/forumService/ForumStatService";
import { voteSortByOrder } from "../../../services/forumService/ForumGroupService";
import { loginSuccess } from "../../../redux/authSlice";
import { createAxios } from "../../../services/createInstance";

//Utils
import LastCommentInfo from "../../lastCommentInfo/lastCommentInfo";

//Modal
import ModelAddForumGroup from "./ModelAddForumGroup";
import ModelUpdateForumGroup from "./ModelUpdateForumGroup";
import ModelUpdateForum from "./ModelUpdateForum";
import ModelUpdateActiveForum from "./ModelUpdateActiveForum";
import ModelDeleteForumGroup from "./ModelDeleteForumGroup";
import ModelAddForum from "./ModelAddForum";

//Css
import "./ForumManage.scss";
import { Container, Col, Row, Card, ListGroup, Badge } from "react-bootstrap";

import {
  FaCode,
  FaLaptopCode,
  FaMobileAlt,
  FaServer,
  FaDatabase,
  FaCloud,
  FaRobot,
  FaMicrochip,
  FaWifi,
  FaSatelliteDish,
  FaGamepad,
  FaChess,
  FaDice,
  FaPuzzlePiece,
  FaPlaystation,
  FaXbox,
  FaSteam,
  FaTwitch,
  FaTag,
  FaTags,
  FaBookmark,
  FaBarcode,
  FaBook,
  FaCertificate,
  FaClipboard,
  FaFileAlt,
  FaFolderOpen,
  FaMoneyBill,
  FaMoneyCheck,
  FaChartLine,
  FaChartBar,
  FaWallet,
  FaShoppingCart,
  FaBalanceScale,
  FaCalculator,
  FaMoneyCheckAlt,
} from "react-icons/fa";

const ForumManage = () => {
  const [forumGroup, setForumGroup] = useState([]);
  const [forum, setForum] = useState([]);

  const [showModelNewForumGroup, setShowModelNewForumGroup] = useState(false);

  const [dataEditForumGroup, setDataEditForumGroup] = useState({});
  const [showModelUpdateForumGroup, setShowModelUpdateForumGroup] =
    useState(false);

  const [dataDeleteForumGroup, setDataDeleteForumGroup] = useState({});
  const [showModelDeleteForumGroup, setShowModelDeleteForumGroup] =
    useState(false);

  const [showModelAddForum, setShowModelAddForum] = useState(false);
  const [idForumGroup, setIdForumGroup] = useState(0);

  const [showModelUpdateForum, setShowModelUpdateForum] = useState(false);
  const [dataUpdateForum, setDataUpdateForum] = useState({});

  const [forumIsActive, setForumIsActive] = useState(true);

  const [showModelUpdateActiveForum, setShowModelUpdateActiveForum] =
    useState(false);

  const [forumStat, setForumStat] = useState({});

  const handleClose = () => {
    setShowModelNewForumGroup(false);
    setShowModelUpdateForumGroup(false);
    setShowModelDeleteForumGroup(false);
    setShowModelAddForum(false);
    setShowModelUpdateForum(false);
    setShowModelUpdateActiveForum(false);
  };

  const handleUpdateTable = (listForumGroup) => {
    setForumGroup([listForumGroup, ...forumGroup]);
    ObjectForumStat();
  };

  const listForumGroup = async () => {
    let res = await getAllForumGroup();
    if (res && res.data) {
      //sort by sortByOrder
      let cloneListForumGroup = _.cloneDeep(res.data);
      cloneListForumGroup = _.orderBy(
        cloneListForumGroup,
        ["sortOrder"],
        ["asc"],
      );
      setForumGroup(cloneListForumGroup);
    }
  };

  const listForums = async () => {
    let res = await getAllForum();
    if (res && res.data) {
      setForum(res.data);
    }
  };

  const ObjectForumStat = async () => {
    let res = await getForumStat();
    if (res) {
      setForumStat(res);
    }
  };

  const handleEditForumGroup = (listForumGroup) => {
    setDataEditForumGroup(listForumGroup);
    setShowModelUpdateForumGroup(true);
  };

  const handleUpdateForumGroup = (listForumGroup) => {
    let cloneListForumGroup = _.cloneDeep(forumGroup);
    let index = cloneListForumGroup.findIndex(
      (forumGroup) => forumGroup.id === listForumGroup.id,
    );
    cloneListForumGroup[index] = listForumGroup;
    setForumGroup(cloneListForumGroup);
  };

  const handleDeleteForumGroup = (listForumGroup) => {
    setDataDeleteForumGroup(listForumGroup);
    setShowModelDeleteForumGroup(true);
  };

  const handleConfirmDeleteForumGroup = (listForumGroup) => {
    let cloneListForumGroup = _.cloneDeep(forumGroup);
    cloneListForumGroup = cloneListForumGroup.filter(
      (item) => item.id !== listForumGroup.id,
    );
    setForumGroup(cloneListForumGroup);
    ObjectForumStat();
  };

  const handleUpdateForum = (listForum) => {
    setForum([listForum, ...forum]);
    listForums();
    ObjectForumStat();
  };

  const handleAddForum = (idForumGroup) => {
    setIdForumGroup(idForumGroup);
    setShowModelAddForum(true);
  };

  const handleEditForumFromModel = (forum, isActive) => {
    setShowModelUpdateForum(true);
    setDataUpdateForum(forum);
    setForumIsActive(isActive);
  };

  const handleUpdateForumFromModel = (listForum) => {
    let cloneListForum = _.cloneDeep(forum);
    let index = cloneListForum.findIndex((forum) => forum.id === listForum.id);
    cloneListForum[index] = listForum;
    setForum(cloneListForum);
    listForums();
    ObjectForumStat();
  };

  const handleEditActionForumFromModel = (forum, isActive) => {
    setShowModelUpdateActiveForum(true);
    setDataUpdateForum(forum);
    setForumIsActive(isActive);
  };

  const handleSort = (sortBy, sortField) => {
    let cloneListForumGroup = _.cloneDeep(forumGroup);
    cloneListForumGroup = _.orderBy(
      cloneListForumGroup,
      [sortField],
      [sortBy === "asc" ? "asc" : "desc"],
    );
    setForumGroup(cloneListForumGroup);
  };

  const renderIcon = (iconName) => {
    const iconMapping = {
      FaCode: <FaCode />,
      FaLaptopCode: <FaLaptopCode />,
      FaMobileAlt: <FaMobileAlt />,
      FaServer: <FaServer />,
      FaDatabase: <FaDatabase />,
      FaCloud: <FaCloud />,
      FaRobot: <FaRobot />,
      FaMicrochip: <FaMicrochip />,
      FaWifi: <FaWifi />,
      FaSatelliteDish: <FaSatelliteDish />,
      FaGamepad: <FaGamepad />,
      FaChess: <FaChess />,
      FaDice: <FaDice />,
      FaPuzzlePiece: <FaPuzzlePiece />,
      FaPlaystation: <FaPlaystation />,
      FaXbox: <FaXbox />,
      FaSteam: <FaSteam />,
      FaTwitch: <FaTwitch />,
      FaTag: <FaTag />,
      FaTags: <FaTags />,
      FaBookmark: <FaBookmark />,
      FaBarcode: <FaBarcode />,
      FaBook: <FaBook />,
      FaCertificate: <FaCertificate />,
      FaClipboard: <FaClipboard />,
      FaFileAlt: <FaFileAlt />,
      FaFolderOpen: <FaFolderOpen />,
      FaMoneyBill: <FaMoneyBill />,
      FaMoneyCheck: <FaMoneyCheck />,
      FaChartLine: <FaChartLine />,
      FaChartBar: <FaChartBar />,
      FaWallet: <FaWallet />,
      FaShoppingCart: <FaShoppingCart />,
      FaBalanceScale: <FaBalanceScale />,
      FaCalculator: <FaCalculator />,
      FaMoneyCheckAlt: <FaMoneyCheckAlt />,
    };

    return iconMapping[iconName] || null;
  };

  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);
  const handleVote = async (id, type) => {
    console.log(currentUser?.accessToken);
    try {
      const res = await voteSortByOrder(
        id,
        type,
        currentUser?.accessToken,
        axiosJWT,
      );
      console.log(res);
      if (res?.data) {
        listForumGroup();
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    listForumGroup();
    listForums();
    ObjectForumStat();
  }, []);

  return (
    <div className="content">
      <Container>
        <Row className="col-12">
          <Col xs={12} lg={9} className="forum-list">
            <div className="d-flex justify-content-between mb-3">
              <div>
                <button
                  className="btn btn-success"
                  onClick={() => handleSort("desc", "id")}
                >
                  <i className="fa-solid fa-up-long"></i>
                </button>
                <button
                  className="btn btn-success"
                  onClick={() => handleSort("asc", "id")}
                >
                  <i className="fa-solid fa-down-long"></i>
                </button>
              </div>
              <button
                className="btn btn-success"
                onClick={() => setShowModelNewForumGroup(true)}
              >
                <i className="fa-solid fa-plus"></i> Forum Group
              </button>
            </div>
            {forumGroup?.map((forumGroup, index) => {
              return (
                <Card key={(forumGroup.id, index)}>
                  <Card.Header style={{ backgroundColor: forumGroup.color }}>
                    <Card.Title className="d-flex">
                      <div className="vote-container">
                        <button
                          className="fa-solid fa-caret-up"
                          onClick={() => handleVote(forumGroup.id, "up")}
                        ></button>
                        <button
                          className="fa-solid fa-caret-down"
                          onClick={() => handleVote(forumGroup.id, "down")}
                        ></button>
                      </div>
                      {renderIcon(forumGroup.icon)}
                      <h4>{forumGroup.title}</h4>
                      <button onClick={() => handleEditForumGroup(forumGroup)}>
                        <i className="fa-solid fa-pencil"></i>
                      </button>
                    </Card.Title>

                    <button
                      className="btn btn-success"
                      onClick={() => handleAddForum(forumGroup.id)}
                    >
                      <i className="fa-solid fa-plus"></i> Forum
                    </button>
                  </Card.Header>
                  <Card.Body>
                    <ListGroup as="ol" numbered>
                      {forum?.map((forum) => {
                        if (forum.idForumGroup == forumGroup.id) {
                          return (
                            <Row key={forum.id}>
                              <Col>
                                <ListGroup.Item
                                  key={forum.id}
                                  className="d-flex justify-content-between align-items-start"
                                >
                                  <div className="col-12 col-md-12 col-lg-5 d-block">
                                    <div className="d-flex justify-content-start align-items-center">
                                      {renderIcon(forum.icon)}
                                      <div className="link-body d-flex">
                                        {forum.title}
                                        <span
                                          style={{
                                            color: "green",
                                            fontSize: "12px",
                                            paddingLeft: "5px",
                                          }}
                                        >
                                          <span
                                            className={
                                              forum.active
                                                ? "bg-success badge"
                                                : "bg-danger badge"
                                            }
                                          >
                                            {" "}
                                            {forum.active === true
                                              ? "active"
                                              : "inactive"}
                                          </span>
                                        </span>
                                      </div>
                                    </div>

                                    <div className="col-12 text-muted">
                                      {forum.description}
                                    </div>
                                  </div>
                                  <div className="col-6 col-md-2">
                                    discussions: {forum?.stat?.discussionCount}{" "}
                                    <br />
                                    comments: {forum?.stat?.commentCount}
                                  </div>
                                  <div className="col-md-4">
                                    {forum?.stat?.lastComment && (
                                      <LastCommentInfo
                                        id={forum?.id}
                                        type="forum"
                                      />
                                    )}
                                  </div>
                                  <div className="btn-forum">
                                    <input
                                      checked={forum.active}
                                      readOnly
                                      type="checkbox"
                                      onChange={() =>
                                        handleEditActionForumFromModel(
                                          forum,
                                          forum.active == true ? false : true,
                                        )
                                      }
                                    />
                                    <button
                                      onClick={() =>
                                        handleEditForumFromModel(
                                          forum,
                                          forum.active,
                                        )
                                      }
                                    >
                                      <i className="fa-solid fa-pen-to-square"></i>
                                    </button>
                                  </div>
                                </ListGroup.Item>
                              </Col>
                            </Row>
                          );
                        }
                      })}
                    </ListGroup>
                  </Card.Body>
                </Card>
              );
            })}
          </Col>

          <Col xs={12} lg={3}>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Forum Info</Card.Title>
              </Card.Header>
              <Card.Body>
                <ListGroup as="ol" numbered>
                  <ListGroup.Item
                    as="li"
                    className="d-flex justify-content-between align-items-start"
                  >
                    <div className="ms-2 me-auto">
                      <div className="fw-bold">Forum</div>
                    </div>
                    <Badge bg="primary" pill>
                      {forumStat.totalForums}
                    </Badge>
                  </ListGroup.Item>
                  <ListGroup.Item
                    as="li"
                    className="d-flex justify-content-between align-items-start"
                  >
                    <div className="ms-2 me-auto">
                      <div className="fw-bold">Discussions</div>
                    </div>
                    <Badge bg="primary" pill>
                      {forumStat.totalDiscussions}
                    </Badge>
                  </ListGroup.Item>
                  <ListGroup.Item
                    as="li"
                    className="d-flex justify-content-between align-items-start"
                  >
                    <div className="ms-2 me-auto">
                      <div className="fw-bold">Discussion Tags</div>
                    </div>
                    <Badge bg="primary" pill>
                      {forumStat.totalTags}
                    </Badge>
                  </ListGroup.Item>
                  <ListGroup.Item
                    as="li"
                    className="d-flex justify-content-between align-items-start"
                  >
                    <div className="ms-2 me-auto">
                      <div className="fw-bold">Comments</div>
                    </div>
                    <Badge bg="primary" pill>
                      {forumStat.totalComments}
                    </Badge>
                  </ListGroup.Item>
                </ListGroup>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
      <ModelAddForumGroup
        show={showModelNewForumGroup}
        handleClose={handleClose}
        handleUpdateTable={handleUpdateTable}
      />
      <ModelUpdateForumGroup
        dataEditForumGroup={dataEditForumGroup}
        show={showModelUpdateForumGroup}
        handleClose={handleClose}
        handleUpdateForumGroup={handleUpdateForumGroup}
      />
      <ModelDeleteForumGroup
        dataDeleteForumGroup={dataDeleteForumGroup}
        show={showModelDeleteForumGroup}
        handleClose={handleClose}
        handleConfirmDeleteForumGroup={handleConfirmDeleteForumGroup}
      />
      <ModelAddForum
        show={showModelAddForum}
        handleClose={handleClose}
        handleUpdateForum={handleUpdateForum}
        idForumGroup={idForumGroup}
      />
      <ModelUpdateForum
        dataUpdateForum={dataUpdateForum}
        show={showModelUpdateForum}
        forumIsActive={forumIsActive}
        handleClose={handleClose}
        handleUpdateForumFromModel={handleUpdateForumFromModel}
      />
      <ModelUpdateActiveForum
        dataUpdateForum={dataUpdateForum}
        show={showModelUpdateActiveForum}
        forumIsActive={forumIsActive}
        handleClose={handleClose}
        handleUpdateForumFromModel={handleUpdateForumFromModel}
      />
    </div>
  );
};

export default ForumManage;
