import PropTypes from "prop-types";
import { Row, Table } from "react-bootstrap";
//service
import { fetchImage } from "../../../services/userService/UserService";
import { Link } from "react-router-dom";
import Avatar from "../../avatar/Avatar";
import { useState } from "react";

const IntroProfile = (props) => {
  const { followers, followings, badges } = props;

  const [showFollower, setShowFollower] = useState(false);
  const [showFollowing, setShowFollowing] = useState(false);

  function getAvatar(item) {
    if (item?.imageUrl) {
      return item?.imageUrl;
    }
    if (item?.avatar) {
      return fetchImage(item?.avatar);
    }
    return "";
  }

  const cardUser = (item) => {
    return (
      <div
        className="col-12 col-xs-6 col-md-4 card m-2 d-flex justify-content-center border"
        style={{ minWidth: 400 }}
      >
        <Row className="p-2">
          <div className="col-3 py-2">
            <div className="mx-auto">
              <Avatar
                src={getAvatar(item)}
                username=""
                height={50}
                width={50}
              />
            </div>
          </div>
          <div className="col-7 py-2">
            <span>{item?.name ?? item?.username ?? "Anonymous"}</span> <br />
            <span className="text-muted row d-block">
              <small className="col-6 row d-inline-block">
                {item?.totalFollowers ?? 0} followers;
              </small>
              <small className="col-6 d-inline-block">
                {item?.totalFollowing ?? 0} following;
              </small>
            </span>
          </div>
          <div className="col-1 py-2">
            <Link
              to={`/member-profile/${item?.username}`}
              className="btn-round btn-icon"
              color="success"
              outline
              size="sm"
            >
              <i className="fa-solid fa-circle-info"></i>
            </Link>
          </div>
        </Row>
      </div>
    );
  };

  const galleryUser = (followers, showAll) => {
    if (showAll) {
      followers = followers.slice(0, 6);
    }
    return followers.map((item) => (
      <span key={item?.id}>{cardUser(item)}</span>
    ));
  };

  const badgeList = (badges) => {
    return (
      <Table responsive>
        <tbody>
          {badges?.map((item) => (
            <tr key={item.id}>
              <td>
                <h6 style={{ color: item?.color }}>
                  <i className={item?.icon}></i>
                </h6>
              </td>
              <td>
                <div style={{ color: item?.color }}>
                  <h6>{item?.name}</h6> {item?.description}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    );
  };

  return (
    <section className="w-100 px-3">
      <h4>Follower:</h4>
      <Row className="w-100 d-flex justify-content-around">
        {followers?.length > 0 ? (
          galleryUser(followers, showFollower)
        ) : (
          <h4 className="text-center">No data</h4>
        )}
      </Row>
      <button
        className="btn btn-default btn-link ml-auto me-0"
        onClick={() => setShowFollower(!showFollower)}
      >
        View all
      </button>
      <hr />
      <h4>Following:</h4>
      <Row className="w-100 d-flex justify-content-around">
        {followings?.length > 0 ? (
          galleryUser(followings, showFollowing)
        ) : (
          <h4 className="text-center">No data</h4>
        )}
      </Row>
      <button
        className="btn btn-default btn-link ml-auto me-0"
        onClick={() => setShowFollowing(!showFollowing)}
      >
        View all
      </button>
      <hr />
      <h5>Note about badges:</h5>
      <div className="px-md-3">{badgeList(badges)}</div>
    </section>
  );
};

IntroProfile.propTypes = {
  followers: PropTypes.array.isRequired,
  followings: PropTypes.array.isRequired,
  badges: PropTypes.array.isRequired,
};

export default IntroProfile;
