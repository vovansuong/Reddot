import NoAvatar from "../../assets/img/default-avatar.png";
import PropTypes from "prop-types";

const Avatar = (props) => {
  const { src, username, height, width } = props;

  return (
    <span className="d-flex-row">
      <img
        src={src && src.length > 0 ? src : NoAvatar}
        alt=""
        style={{
          height: `${height}px`,
          width: `${width}px`,
          objectFit: `cover`,
          borderRadius: `0.5rem`,
        }}
      />
      {username && (
        <span style={{ fontSize: "1rem" }} className="username mx-2">
          {username && username.length > 0 ? username : "No name"}
        </span>
      )}
    </span>
  );
};

Avatar.propTypes = {
  src: PropTypes.string,
  username: PropTypes.string,
  height: PropTypes.number,
  width: PropTypes.number,
};

export default Avatar;
