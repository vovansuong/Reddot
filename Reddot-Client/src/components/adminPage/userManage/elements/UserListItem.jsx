import PropTypes from 'prop-types';
import Avatar from '../../../avatar/Avatar';
import { Link } from 'react-router-dom';
import { convertListNameRole } from '../../../../utils/Helper';
import { fetchImage } from '../../../../services/userService/UserService';
import noAvatar from "../../../../assets/img/default-avatar.png";

const UserListItem = (props) => {
  const { user,
    handleShowHideEdit,
    handleSetEditUser,
    handleShowHide,
    handleSetDeleteUser,
    handleShowEditRole
  } = props;

  const handleDelete = () => {
    handleSetDeleteUser(user);
    handleShowHide();
  }

  const handleUpdate = () => {
    handleSetEditUser(user);
    handleShowHideEdit();
  }

  const handleUpdateRole = () => {
    handleSetEditUser(user);
    handleShowEditRole();
  }


  return (
    <tr key={user.id}>
      <td>
        <div className='ml-0 me-auto'>
          <Avatar
            src={(user?.avatar ? fetchImage(user?.avatar) : (user?.imageUrl ?? noAvatar))}
            username={user?.username}
            height={50} width={50} />
        </div>
      </td>
      <td>
        <Link to={`/admin/member-profile/${user?.username}`} state={{ user }}>
          <p>{user.email}</p>
        </Link>
      </td>
      <td>
        <button onClick={handleUpdateRole}>{convertListNameRole(user.roles.map(x => x.name))}</button>
      </td>
      <td>
        <i className="fas fa-sync-alt mx-2"
          onClick={handleUpdate}
        ></i>
        {user.accountStatus}
      </td>
    </tr>
  );
}


UserListItem.propTypes = {
  user: PropTypes.object.isRequired,
  handleShowHideEdit: PropTypes.func.isRequired,
  handleSetEditUser: PropTypes.func.isRequired,
  handleShowHide: PropTypes.func.isRequired,
  handleSetDeleteUser: PropTypes.func.isRequired,
  handleShowEditRole: PropTypes.func.isRequired,
};

export default UserListItem;