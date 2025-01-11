import { useEffect, useState, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";

import _debounce from "lodash/debounce";

import { Row, Col } from "reactstrap";

import { Tab, Tabs, Table } from "react-bootstrap";

import { getAllUsers } from "../../../redux/apiUserRequest";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import {
  updateStatusUser,
  updateNewRoleForUser,
  createNewUser,
} from "../../../services/userService/UserService";

import UserCardItem from "./elements/UserCardItem";
import UserListItem from "./elements/UserListItem";
import Pagination from "../../pagination/Pagination";
import ModalConfirmDeleteUser from "./elements/ModalConfirmDelete";
import ModalEditRole from "./elements/ModalEditRole";
import ModalEditUser from "./elements/ModalEditStatus";
import AddNewUser from "./elements/AddNewUser";

function UserListManage() {
  const [key, setKey] = useState("userList");
  const [showAdd, setShowAdd] = useState(false);

  const [userList, setUserList] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [orderBy, setOrderBy] = useState("createdAt");
  const [sortBy, setSortBy] = useState("ASC");

  const [keyword, setKeyword] = useState("");
  const [msg, setMsg] = useState("");

  const [userDelete, setUserDelete] = useState({});
  const [showModal, setShowModal] = useState(false);

  const [userEdit, setUserEdit] = useState({});
  const [showEdit, setShowEdit] = useState(false);

  const [showModalRole, setShowModalRole] = useState(false);

  const handleShowAdd = () => setShowAdd(!showAdd);
  const handleShowHide = () => setShowModal(!showModal);
  const handleSetDeleteUser = (user) => setUserDelete(user);

  const handleShowHideEdit = () => setShowEdit(!showEdit);
  const handleSetEditUser = (user) => setUserEdit(user);

  const handleShowEditRole = () => setShowModalRole(!showModalRole);

  const handlePageClick = (event) => {
    setPage(+event.selected + 1);
  };

  const handleSort = (sortBy, orderBy) => {
    setSortBy(sortBy);
    setOrderBy(orderBy);
    return true;
  };

  let currentUser = useSelector((state) => state.auth.login?.currentUser);
  // const usersData = useSelector((state) => state.users.users?.allUsers);

  const dispatch = useDispatch();
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const getAllUsersData = async (value) => {
    let pageData = {
      search: value,
      page: page,
      size: pageSize,
      orderBy: orderBy,
      sort: sortBy,
    };
    let res = await getAllUsers(
      currentUser?.accessToken,
      dispatch,
      axiosJWT,
      pageData,
    );
    if (res?.data?.length > 0) {
      setUserList(res.data);
      setPageSize(res.pageSize);
      setTotalPages(res.totalPages);
      setMsg("");
    } else if (value.length > 0) {
      setMsg(`Can't find user by ${value}`);
    }
    return true;
  };

  const handleAddUser = async (registerInfo) => {
    let res = await createNewUser(registerInfo);
    if (+res?.status === 201) {
      setShowAdd(false);
      toast.success("Added successfully!");
      getAllUsersData();
      return true;
    } else {
      toast.error(res?.data?.message);
      return false;
    }
  };

  const handleDelete = async (user) => {
    if (user.id === null) {
      return;
    }
    if (user.id === currentUser.id || +user.id === 1) {
      return toast.error("Can't delete admin");
    }
    let res = await deleteUser(currentUser?.accessToken, user.id, axiosJWT);
    if (+res?.status === 200 || +res?.data?.status === 200) {
      setShowModal(false);
      toast.success("Deleted successfully!");
      setUserDelete({});
      await getAllUsersData("");
    } else {
      toast.error(res?.message);
    }
  };

  const handleUpdateStatus = async (id, accountStatus) => {
    let res = await updateStatusUser(
      id,
      accountStatus,
      axiosJWT,
      currentUser.accessToken,
    );
    if (+res.data.status === 200) {
      toast.success("Update status successfully");
      setShowEdit(false);
      setUserEdit({});
      getAllUsersData("");
    } else {
      toast.error(res?.data?.message);
    }
  };

  const handleUpdateRole = async (dataRole) => {
    let res = await updateNewRoleForUser(
      dataRole,
      axiosJWT,
      currentUser?.accessToken,
    );
    if (+res?.status === 200 || +res?.data?.status === 200) {
      toast.success("Updated new role successfully");
      setShowModalRole(false);
      setUserEdit({});
      getAllUsersData("");
    } else {
      toast.error("Can not update new role for User");
    }
  };

  const debounceFn = useCallback(_debounce(handleSearch, 1000), []);

  function handleSearch(value) {
    getAllUsersData(value);
  }

  const handleChange = (event) => {
    setMsg("");
    setKeyword(event.target.value);
    debounceFn(event.target.value);
  };

  useEffect(() => {
    getAllUsersData("");
  }, [page, pageSize, orderBy, sortBy]);

  const tableUserList = (users) => {
    if (users.length == 0) {
      return (
        <div className="text-center">
          <span className="d-flex justify-content-center">
            <i className="fas fa-sync fa-spin fa-5x"></i>
            <br />
          </span>
          <h5>Loading...</h5>
        </div>
      );
    }
    return (
      <Table responsive striped borderless hover>
        <thead>
          <tr>
            <th>
              <span>Username&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "username")}
                  onKeyDown={() => {
                    handleSort("DESC", "username");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "username")}
                  onKeyDown={() => {
                    handleSort("ASC", "username");
                  }}
                ></button>
              </span>
            </th>
            <th>
              <span>Email&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "email")}
                  onKeyDown={() => {
                    handleSort("DESC", "email");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "email")}
                  onKeyDown={() => {
                    handleSort("ASC", "email");
                  }}
                ></button>
              </span>
            </th>
            <th>
              <span>Role&nbsp;</span>
            </th>
            <th>
              <span>Status&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "accountStatus")}
                  onKeyDown={() => {
                    handleSort("DESC", "accountStatus");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "accountStatus")}
                  onKeyDown={() => {
                    handleSort("ASC", "accountStatus");
                  }}
                ></button>
              </span>
            </th>
          </tr>
        </thead>
        <tbody>
          {users?.map((user) => (
            <UserListItem
              key={user.id}
              user={user}
              handleShowHide={handleShowHide}
              handleShowHideEdit={handleShowHideEdit}
              handleSetEditUser={handleSetEditUser}
              handleShowEditRole={handleShowEditRole}
            />
          ))}
        </tbody>
      </Table>
    );
  };

  return (
    <div className="content">
      <section>
        <h3>User List</h3>
        <Col className="my-3">
          {!showAdd && (
            <div className="ml-auto me-0 col-md-4">
              <button
                onClick={() => setShowAdd(!showAdd)}
                className="btn btn-success"
              >
                {showAdd ? "Close" : "Add New"}
              </button>
            </div>
          )}
          {showAdd && (
            <AddNewUser
              handleShowAdd={handleShowAdd}
              registerUser={handleAddUser}
            />
          )}
          <Row className="d-flex">
            <div className="ml-0 me-auto col-ms-2 d-flex align-items-center mb-3">
              <label htmlFor="page" className="col-2">
                Page size:
              </label>
              <select
                id="page"
                name="page"
                className="form-input-select col-1"
                onChange={(e) => setPageSize(e.currentTarget.value)}
              >
                <option value="5">05</option>
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="50">50</option>
              </select>
            </div>

            <div className="col-ms-4 mb-3">
              <input
                className="mx-2 form-control col-10 p-3"
                value={keyword}
                onChange={handleChange}
                id="search_user"
                name="keyword"
                placeholder="Search user by username or email"
              />
            </div>
          </Row>
          <div className="text-danger">{msg}</div>
        </Col>

        <div>
          <Tabs
            id="controlled-tab-user"
            activeKey={key}
            onSelect={(k) => setKey(k)}
            justify
          >
            <Tab
              eventKey="userList"
              title={<i className="fa-solid fa-list fa-2x"></i>}
            >
              {tableUserList(userList)}
            </Tab>
            <Tab
              eventKey="userGrid"
              title={<i className="fa-solid fa-grip fa-2x"></i>}
            >
              <Col>
                <Row>
                  {userList.length === 0 ? (
                    <div className="text-center">
                      <span className="d-flex justify-content-center">
                        <i className="fas fa-sync fa-spin fa-5x"></i>
                        <br />
                      </span>
                      <h5>Loading...</h5>
                    </div>
                  ) : (
                    userList?.map((user) => (
                      <UserCardItem
                        key={user.id}
                        user={user}
                        handleShowHide={handleShowHide}
                        handleShowHideEdit={handleShowHideEdit}
                        handleSetEditUser={handleSetEditUser}
                        handleShowEditRole={handleShowEditRole}
                      />
                    ))
                  )}
                </Row>
              </Col>
            </Tab>
          </Tabs>
          <Pagination
            handlePageClick={handlePageClick}
            pageSize={+pageSize}
            totalPages={+totalPages}
          />
        </div>
      </section>

      <ModalEditUser
        show={showEdit}
        handleClose={handleShowHideEdit}
        handleUpdateStatus={handleUpdateStatus}
        user={userEdit}
      />

      <ModalEditRole
        show={showModalRole}
        handleClose={handleShowEditRole}
        handleUpdateRole={handleUpdateRole}
        user={userEdit}
      />
    </div>
  );
}

export default UserListManage;
