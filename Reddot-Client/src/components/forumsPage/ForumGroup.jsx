import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import BannerTop from "../bannerTop/BannerTop";
import { Col, Row, Card, ListGroup } from "react-bootstrap";
import _ from "lodash";

//Service
import { getAllForumGroup } from "../../services/forumService/ForumGroupService";
import { getAllForum } from "../../services/forumService/ForumService";

//Page
import ForumInfo from "./ForumInfo";
import LastCommentInfo from "../lastCommentInfo/lastCommentInfo";
import "./ForumGroup.scss";

//Icon
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

const ForumGroup = () => {
  const bannerName = "Forums Index";
  const breadcrumbs = [{ id: 1, name: "Forums", link: "/forums" }];

  const [forumGroup, setForumGroup] = useState([]);
  const [forum, setForum] = useState([]);

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

  const [fetchForumStat, setFetchForumStat] = useState([]);
  const getListForumStat = async () => {
    let res = await getForumStat();
    if (res && res.data) {
      setFetchForumStat(res.data);
    }
  };

  useEffect(() => {
    listForums();
    listForumGroup();
    getListForumStat();
  }, []);

  return (
    <section className="forums-container content">
      <Col md="12">
        <BannerTop bannerName={bannerName} breadcrumbs={breadcrumbs} />
      </Col>

      <Col md="12">
        <Row>
          <Col md={8}>
            <Col className="forum-list">
              {forumGroup?.map((forumGroup, index) => {
                return (
                  <Card key={(forumGroup.id, index)}>
                    <Card.Header
                      style={{
                        backgroundColor: forumGroup.color,
                        color: "white",
                      }}
                    >
                      <Card.Title className="d-flex">
                        {renderIcon(forumGroup.icon)}
                        <h4>{forumGroup.title}</h4>
                      </Card.Title>
                    </Card.Header>
                    <Card.Body>
                      <ListGroup as="ol" numbered>
                        {forum?.map((forum) => {
                          if (forum.idForumGroup == forumGroup.id) {
                            if (forum.active == true) {
                              return (
                                <ListGroup.Item
                                  className="row d-flex"
                                  key={forum.id}
                                >
                                  <div className="col-12 col-md-12 col-lg-5 d-block">
                                    <div className="d-flex justify-content-start align-items-center">
                                      {renderIcon(forum.icon)}
                                      <div className="link-body">
                                        <Link to={`/forum/${forum.id}`}>
                                          {forum.title}
                                        </Link>
                                      </div>
                                    </div>

                                    <div className="col-12 text-muted">
                                      {forum.description}
                                    </div>
                                  </div>
                                  <div className="col-12 col-md-12 col-lg-2">
                                    {fetchForumStat?.map((stat) => {
                                      if (forum?.id == stat?.id) {
                                        return (
                                          <>
                                            <div
                                              style={{
                                                fontSize: "15px",
                                                paddingTop: "10px",
                                              }}
                                            >
                                              Discussions:{" "}
                                              {stat?.discussionCount} Comments:{" "}
                                              {stat?.commentCount}
                                            </div>
                                          </>
                                        );
                                      }
                                    })}
                                  </div>
                                  <div className="col-12 col-md-12 col-lg-5">
                                    {forum?.stat?.lastComment && (
                                      <LastCommentInfo
                                        id={forum?.id}
                                        type="forum"
                                      />
                                    )}
                                  </div>
                                </ListGroup.Item>
                              );
                            }
                          }
                        })}
                      </ListGroup>
                    </Card.Body>
                  </Card>
                );
              })}
            </Col>
          </Col>
          <Col md={4}>
            <ForumInfo />
          </Col>
        </Row>
      </Col>
    </section>
  );
};

export default ForumGroup;
