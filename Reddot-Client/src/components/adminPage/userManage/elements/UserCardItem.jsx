import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { Card, Row, Col } from "react-bootstrap";

import Avatar from "../../../avatar/Avatar";

import { convertListNameRole } from "../../../../utils/Helper";
import { fetchImage } from "../../../../services/userService/UserService";
import noAvatar from "../../../../assets/img/default-avatar.png";

const UserCardItem = (props) => {
  const {
    user,
    handleShowHideEdit,
    handleSetEditUser,
    handleShowHide,
    handleSetDeleteUser,
    handleShowEditRole,
  } = props;

  const handleDelete = () => {
    handleSetDeleteUser(user);
    handleShowHide();
  };

  const handleUpdate = () => {
    handleSetEditUser(user);
    handleShowHideEdit();
  };

  const handleUpdateRole = () => {
    handleSetEditUser(user);
    handleShowEditRole();
  };

  return (
    <Col
      key={user.id}
      lg="4"
      md="6"
      sm="6"
      className="my-3"
      style={{ minWidth: `400px` }}
    >
      <Card
        color={user.active ? "primary" : ""}
        className="card-stats text-dark"
      >
        <Card.Body>
          <Row>
            <Col md="4" xs="5">
              <div className="icon-big text-center icon-warning">
                <Avatar
                  src={
                    user?.imageUrl ??
                    (user?.avatar ? fetchImage(user?.avatar) : noAvatar)
                  }
                  username=""
                  height={100}
                  width={100}
                />
              </div>
            </Col>
            <Col md="8" xs="7">
              <div style={{ color: "black" }}>
                <Link
                  to={`/admin/member-profile/${user?.username}`}
                  className="text-decoration-none"
                >
                  <strong>{user.name ?? user.username}</strong> <br />
                  <span>Email: {user.email}</span> <br />
                </Link>
                <button className="" onClick={handleUpdateRole}>
                  Role: {convertListNameRole(user.roles.map((x) => x.name))}
                </button>
              </div>
            </Col>
          </Row>
        </Card.Body>
        <hr />
        <Card.Footer className="d-flex justify-content-around">
          <span className="stats">
            <button onClick={handleUpdate}>
              <i className="fas fa-sync-alt" /> {user.accountStatus}
            </button>
          </span>
          <span className="stats mx-2 text-danger">
            <button
              className="d-block"
              onClick={handleDelete}
              onKeyDown={handleDelete}
            >
              <i className="fa-solid fa-delete-left text-danger"></i> Delete
            </button>
          </span>
        </Card.Footer>
      </Card>
    </Col>
  );
};

UserCardItem.propTypes = {
  user: PropTypes.object.isRequired,
  handleShowHideEdit: PropTypes.func.isRequired,
  handleSetEditUser: PropTypes.func.isRequired,
  handleShowHide: PropTypes.func.isRequired,
  handleSetDeleteUser: PropTypes.func.isRequired,
  handleShowEditRole: PropTypes.func.isRequired,
};

export default UserCardItem;
