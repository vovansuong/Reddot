import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";

import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import {
  getAllBadge,
  putUpdateBadge,
  setBadgeForAllUser,
} from "../../../services/badgeService/badgeService";
import ModalEditBadge from "./elements/ModalEditBadge";

const BadgeManage = () => {
  const [badges, setBadges] = useState([]);
  const [badgeEdit, setBadgeEdit] = useState({});
  const [showModal, setShowModal] = useState(false);

  let currentUser = useSelector((state) => state.auth.login?.currentUser);
  const dispatch = useDispatch();
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const fetchAllBadge = async () => {
    let res = await getAllBadge(currentUser?.accessToken, axiosJWT);
    if (+res?.status === 200 || +res?.data?.status === 200) {
      setBadges(res?.data?.data);
    } else {
      console.log(res?.data?.message);
    }
  };

  const handleClose = () => setShowModal(!showModal);
  const handleShowModel = (badge) => {
    setShowModal(true);
    setBadgeEdit(badge);
  };

  const handleEditBadge = async (newBadges) => {
    console.log(newBadges);
    let res = await putUpdateBadge(
      newBadges,
      currentUser.accessToken,
      axiosJWT,
    );
    if (+res?.status === 200 || +res?.data?.status === 200) {
      toast.success("Update badge success");
      fetchAllBadge();
    } else {
      toast.error(res?.data?.message);
    }
  };

  const handleSetBadgeAllUser = async () => {
    let res = await setBadgeForAllUser(currentUser.accessToken, axiosJWT);
    if (+res?.status === 200 || +res?.data?.status === 200) {
      toast.success("Set badge success");
    } else {
      toast.error(res?.data?.message);
    }
  };

  useEffect(() => {
    fetchAllBadge();
  }, []);

  return (
    <div className="content">
      <h1>Badge List</h1>

      <div className="">
        <button className="btn btn-primary" onClick={handleSetBadgeAllUser}>
          Set badge All User
        </button>
        <table className="table table-striped borderless">
          <thead>
            <tr>
              <th>Badge Name</th>
              <th>Active</th>
              <th>Total Score</th>
              <th>Total Discussion</th>
              <th>Total Comment</th>
              <th>Description</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {badges.length > 0 ? (
              badges.map((badge) => {
                return (
                  <tr key={badge?.id} style={{ color: badge?.color }}>
                    <td>
                      <i className={badge.icon} />
                      {badge.name}
                    </td>
                    <td>{badge.action ? "Active" : "Inactive"}</td>
                    <td>{badge.totalScore}</td>
                    <td>{badge.totalDiscussion}</td>
                    <td>{badge.totalComment}</td>
                    <td>{badge.description}</td>
                    <td>
                      <button onClick={() => handleShowModel(badge)}>
                        <i className="fa-solid fa-pen-to-square" />
                      </button>
                    </td>
                  </tr>
                );
              })
            ) : (
              <tr>
                <td colSpan={7} className="text-center">
                  <span className="d-flex justify-content-center">
                    <i className="fas fa-sync fa-spin fa-5x"></i>
                  </span>
                  <h5>Loading...</h5>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <ModalEditBadge
        show={showModal}
        handleClose={handleClose}
        badge={badgeEdit}
        handleEditBadge={handleEditBadge}
      />
    </div>
  );
};

export default BadgeManage;
