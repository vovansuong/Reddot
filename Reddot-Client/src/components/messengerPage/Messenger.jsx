import Avatar from "../avatar/Avatar";
import PropTypes from "prop-types";

const Messenger = (props) => {
  const { first } = props;

  return (
    <div className="messenger">
      <div className="messenger__header">
        <Avatar height={50} width={50} />
        <div className="messenger__headerInfo">
          <h3>{first?.username ?? "No name"}</h3>
          <p>Active {new Date(first?.createdAt?.toDate()).toUTCString()}</p>
        </div>
      </div>
      <div className="messenger__body">
        <div className="messenger__message">
          <p>{first?.message ?? "No message"}</p>
        </div>
      </div>
    </div>
  );
};

Messenger.propTypes = {
  first: PropTypes.object.isRequired,
};

export default Messenger;
