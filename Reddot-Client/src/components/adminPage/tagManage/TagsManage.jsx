import Table from "react-bootstrap/Table";
import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import _ from "lodash";

//Service
import { createAxios } from "../../../services/createInstance";
import { getAllTags } from "../../../services/tagService/tagService";
import { loginSuccess } from "../../../redux/authSlice";

//Modal
import ModalAddNewTags from "./ModalAddNewTags";
import ModalUpdateTags from "./ModalUpdateTags";
import ModalSetStatusTags from "./ModalSetStatusTags";

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
import { IoGameController } from "react-icons/io5";

//Paginate
import Pagination from "../../pagination/Pagination";

//Scss
// import "./Tags.scss";

const TagsManage = () => {
  //Login
  const dispatch = useDispatch();
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

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
      IoGameController: <IoGameController />,
    };

    return iconMapping[iconName] || null;
  };

  //All Tags
  const [listTags, setListTags] = useState([]);

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

  const getAllTagsData = async () => {
    const res = await getAllTags(
      page,
      size,
      orderBy,
      sort,
      search,
      currentUser?.accessToken,
      axiosJWT,
    );
    if (res && +res.status === 201) {
      setListTags(res.data.data);
      setTotalPages(res.data.totalPages);
      toast.success(res?.data?.message);
    } else {
      toast.error(res?.data?.message);
    }
  };

  //Close Modal
  const handleClose = () => {
    setShowAddNewModal(false);
    setShowEditModalTag(false);
    setShowSetStatusModal(false);
  };

  //Create Tags
  const [showAddNewModal, setShowAddNewModal] = useState(false);
  const handleUpdateAddNewTags = (tag) => {
    setListTags([...listTags, tag]);
  };

  //Update Tags
  const [showEditModalTag, setShowEditModalTag] = useState(false);
  const [dataEditTag, setDataEditTag] = useState({});
  const handleEditModalTag = (tag) => {
    setShowEditModalTag(true);
    setDataEditTag(tag);
  };

  const handleUpdateEditTags = (tag) => {
    console.log(tag);
    let cloneListTags = _.cloneDeep(listTags);
    let index = cloneListTags.findIndex((t) => t.id === tag.id);
    cloneListTags[index] = tag;
    setListTags(cloneListTags);
  };

  //Set Status
  const [showSetStatusModal, setShowSetStatusModal] = useState(false);
  const handleSetStatusModal = (tag) => {
    setShowSetStatusModal(true);
    setDataEditTag(tag);
  };

  useEffect(() => {
    getAllTagsData(1);
  }, [page, size, orderBy, sort, search]);

  return (
    <div className="content">
      <div className="d-flex justify-content-between align-items-center">
        <button
          onClick={() => setShowAddNewModal(true)}
          type="button"
          className="btn btn-primary"
        >
          Add New Tag
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
              Label
              <span>
                <i
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => {
                    setOrderBy("label");
                    setSort("desc");
                  }}
                ></i>
                <i
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => {
                    setOrderBy("label");
                    setSort("asc");
                  }}
                ></i>
              </span>
            </th>
            <th>Icon</th>
            <th>Color</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {listTags?.map((tag) => (
            <tr key={tag.id}>
              <td>{tag.id}</td>
              <td>{tag.label}</td>
              <td>{renderIcon(tag.icon)}</td>
              <td>
                <div
                  style={{
                    backgroundColor: tag.color,
                    width: 20,
                    height: 20,
                  }}
                ></div>
              </td>
              <td
                className={
                  tag.disabled
                    ? "text-success"
                    : "text-danger" + " fw-bold btn-sm"
                }
              >
                {tag.disabled ? "Enable" : "Disable"}
              </td>
              <td>
                <button onClick={() => handleEditModalTag(tag)}>
                  <i className="fa-solid fa-pencil"></i>
                </button>
                <button
                  onClick={() => handleSetStatusModal(tag)}
                  className="btn-sm btn-primary"
                >
                  Set status
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
      <ModalAddNewTags
        handleClose={handleClose}
        show={showAddNewModal}
        handleUpdateAddNewTags={handleUpdateAddNewTags}
      />
      <ModalUpdateTags
        handleClose={handleClose}
        show={showEditModalTag}
        dataEditTag={dataEditTag}
        handleUpdateEditTags={handleUpdateEditTags}
      />
      <ModalSetStatusTags
        handleClose={handleClose}
        show={showSetStatusModal}
        dataEditTag={dataEditTag}
        handleUpdateEditTags={handleUpdateEditTags}
      />
    </div>
  );
};

export default TagsManage;
