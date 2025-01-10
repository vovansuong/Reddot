import Table from "react-bootstrap/Table";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import _ from "lodash";

//Service
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import { getPageBannedKeywords } from "../../../services/bannedKeywordService/BannedKeywordService";

//Modal
import ModalAddNewBanned from "./ModalAddNewBanned";
import ModalUpdateBanned from "./ModalUpdateBanned";
import ModalDeleteBanned from "./ModalDeleteBanned";

//Paginate
import Pagination from "../../pagination/Pagination";

//Utils
import { formatDate } from "../../../utils/FormatDateTimeHelper";

const BannedKeyword = () => {
  //Login
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  //All banned keywords
  const [listBanned, setListBanned] = useState([]);

  //Pagination
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const [orderBy, setOrderBy] = useState("id");
  const [sort, setSort] = useState("ASC");
  const [search, setSearch] = useState("");

  const handlePageClick = (event) => {
    setPage(event.selected + 1);
  };

  const getAllBannedData = async () => {
    const res = await getPageBannedKeywords(
      page,
      size,
      orderBy,
      sort,
      search,
      currentUser?.accessToken,
      axiosJWT,
    );
    console.log(res.data.data);
    if (res && +res.data?.status === 201) {
      setListBanned(res.data?.data.data);
      setTotalPages(res.data?.data.totalPages);
    } else {
      toast.error(res?.data?.message);
    }
  };

  //Close Modal
  const handleClose = () => {
    setShowAddNewModal(false);
    setShowEditModalBanned(false);
    setShowDeleteModalBanned(false);
  };

  //Create Tags
  const [showAddNewModal, setShowAddNewModal] = useState(false);
  const handleUpdateAddNewBanned = (bannedKeyword) => {
    setListBanned([...listBanned, bannedKeyword]);
  };

  //Update Tags
  const [showEditModalBanned, setShowEditModalBanned] = useState(false);
  const [dataEditBanned, setDataEditBanned] = useState({});
  const handleEditModalBanned = (tag) => {
    setShowEditModalBanned(true);
    setDataEditBanned(tag);
  };

  const handleUpdateEditBanned = (banned) => {
    console.log(banned);
    let cloneListBanned = _.cloneDeep(listBanned);
    let index = cloneListBanned.findIndex((t) => t.id === banned.id);
    cloneListBanned[index] = listBanned;
    setListBanned(cloneListBanned);
    getAllBannedData();
  };

  //delete
  const [showDeleteModalBanned, setShowDeleteModalBanned] = useState(false);
  const [dataDeleteBanned, setDataDeleteBanned] = useState({});

  const handleDeleteModalBanned = (modal) => {
    setShowDeleteModalBanned(true);
    setDataDeleteBanned(modal);
  };

  const handleUpdateDeleteBanned = (data) => {
    let cloneListBanned = _.cloneDeep(listBanned);
    cloneListBanned = cloneListBanned.filter((t) => t.id !== data.id);
    setListBanned(cloneListBanned);
    getAllBannedData();
  };

  useEffect(() => {
    getAllBannedData();
  }, [page, size, orderBy, sort, search]);

  return (
    <div className="content">
      <div className="d-flex justify-content-between align-items-center">
        <button
          onClick={() => setShowAddNewModal(true)}
          type="button"
          className="btn btn-primary"
        >
          New Banned Keyword
        </button>
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
              Id
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
              Create At
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
            <th className="sort_header">
              keywords
              <span>
                <i
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => {
                    setOrderBy("keyword");
                    setSort("desc");
                  }}
                ></i>
                <i
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => {
                    setOrderBy("keyword");
                    setSort("asc");
                  }}
                ></i>
              </span>
            </th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {listBanned?.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>{formatDate(item.createdAt)}</td>
              <td>{item.keyword}</td>
              <td>
                <button onClick={() => handleEditModalBanned(item)}>
                  <i className="fa-solid fa-pen-to-square"></i>
                </button>
                <button onClick={() => handleDeleteModalBanned(item)}>
                  <i className="fa-solid fa-trash"></i>
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <div className="pagination pagination-end">
        <Pagination
          handlePageClick={handlePageClick}
          pageSize={+page}
          totalPages={+totalPages}
        />
      </div>
      {/* Modal */}
      <ModalAddNewBanned
        show={showAddNewModal}
        handleClose={handleClose}
        handleUpdateAddNewBanned={handleUpdateAddNewBanned}
      />
      <ModalUpdateBanned
        show={showEditModalBanned}
        handleClose={handleClose}
        handleUpdateEditBanned={handleUpdateEditBanned}
        dataEditBanned={dataEditBanned}
      />
      <ModalDeleteBanned
        show={showDeleteModalBanned}
        handleClose={handleClose}
        handleUpdateDeleteBanned={handleUpdateDeleteBanned}
        dataDeleteBanned={dataDeleteBanned}
      />
    </div>
  );
};

export default BannedKeyword;
