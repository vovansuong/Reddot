import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Tab, Tabs, Col, Row } from "react-bootstrap";
import { toast } from "react-toastify";

import banner from "../../assets/img/damir-bosnjak.jpg";
import BannerTop from "../bannerTop/BannerTop";
import {
  getFollowerByUserId,
  getFollowingByUserId,
  postRegisterFollow,
} from "../../services/followService/followService";

import {
  getUserInfoByUsername,
  postUpdateInfo,
  fetchImage,
} from "../../services/userService/UserService";

import { createAxios } from "../../services/createInstance";
import { loginSuccess } from "../../redux/authSlice";
import noAvatar from "../../assets/img/default-avatar.png";
import { formatDate } from "../../utils/FormatDateTimeHelper";
import { uploadAvatar } from "../../redux/apiUserRequest";
import { getAllBadge } from "../../services/badgeService/badgeService";

import ActivitiesProfile from "./elements/ActivitiesProfile";
import IntroProfile from "./elements/IntroProfile";
import ModalEditImage from "./elements/ModalEditImage";
import ModalEditProfile from "./elements/ModalEditProfile";
import ListBookmark from "./elements/ListBookmarkByUser";

const MemberProfile = () => {
  const { username } = useParams();

  const bannerName = "";
  const breadcrumbs = [
    {
      id: 1,
      name: `Profile - ${username}`,
      link: `/member-profile/${username}`,
    },
  ];

  const [key, setKey] = useState("home"); //for tabs, set tab default home

  const [userInfo, setUserInfo] = useState({});

  const [showModal, setShowModal] = useState(false);
  const [showModalImage, setShowModalImage] = useState(false);

  const [isFollow, setIsFollow] = useState(false);
  const [image, setImage] = useState(null);

  const [followers, setFollowers] = useState([]);
  const [followings, setFollowings] = useState([]);
  const [badges, setBadges] = useState([]);

  let currentUser = useSelector((state) => state.auth.login?.currentUser);
  const dispatch = useDispatch();
  // const navigate = useNavigate();
  const axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  const getDataUserInfo = async () => {
    const accessToken = currentUser?.accessToken;
    const res = await getUserInfoByUsername(username, axiosJWT, accessToken);
    if (res && res?.data) {
      setUserInfo(res.data);
    }
    return true;
  };

  const [userEdit, setUserEdit] = useState({
    username: currentUser.username,
    name: userInfo?.name,
    gender: userInfo?.person?.gender,
    birthDate: userInfo?.person?.birthDate,
    address: userInfo?.person?.address,
    phone: userInfo?.person?.phone,
    bio: userInfo?.person?.bio,
    email: userInfo?.email,
  });

  const handleClose = () => setShowModal(false);
  const handleCloseImage = () => setShowModalImage(false);

  const handleUploadAvatar = async (file) => {
    if (!file) {
      toast.success("Please select file");
      return;
    }
    const fd = new FormData();
    fd.append("file", file);
    let res = await uploadAvatar(
      dispatch,
      currentUser?.accessToken,
      axiosJWT,
      fd,
      currentUser?.username,
    );
    if (res && +res?.status === 200) {
      setImage(res?.data);
      handleCloseImage();
      toast.success("Upload avatar successfully");
    } else {
      toast.error("Upload avatar failed");
    }
    return null;
  };

  const handleUpdateInfo = async (newInfo) => {
    let res = await postUpdateInfo(currentUser.accessToken, axiosJWT, newInfo);
    if (res && +res?.status === 200) {
      handleClose();
      toast.success("Update info user successfully");
      setUserInfo(res?.data);
    } else {
      toast.error("Update info user failed");
      console.log(`Update Error`, res?.message);
    }
    return null;
  };

  //follow user
  const fetchFollowerData = async () => {
    let res = await getFollowerByUserId(
      currentUser?.accessToken,
      axiosJWT,
      username,
    );
    if (+res?.status === 200 || +res?.data?.status === 200) {
      setFollowers(res?.data?.data);
      isCurrentUserFollowerUser(res?.data?.data);
    } else {
      console.log(res?.data?.message);
    }
  };

  const fetchFollowingData = async () => {
    let res = await getFollowingByUserId(
      currentUser?.accessToken,
      axiosJWT,
      username,
    );
    if (+res?.status === 200 || +res?.data?.status === 200) {
      setFollowings(res?.data?.data);
    } else {
      console.log(res?.data?.message);
    }
  };

  const isCurrentUserFollowerUser = (followers) => {
    if (followers.length > 0) {
      let result = followers?.some(
        (follower) => follower.username === currentUser.username,
      );
      setIsFollow(result);
    } else {
      setIsFollow(false);
    }
  };

  const handleFollow = async () => {
    let followData = {
      followerUserId: +userInfo.id,
      followingUserId: +currentUser.id,
    };
    let res = await postRegisterFollow(
      currentUser?.accessToken,
      axiosJWT,
      followData,
    );
    if (+res?.status === 200 || +res?.data?.status === 200) {
      toast.success("Follow user was changed successfully");
      fetchFollowerData();
    } else {
      toast.error("Follow user was changed failed");
    }
  };

  const fetchAllBadge = async () => {
    let res = await getAllBadge(currentUser?.accessToken, axiosJWT);
    if (+res?.status === 200 || +res?.data?.status === 200) {
      setBadges(res?.data?.data);
    } else {
      console.log(res?.data?.message);
    }
  };

  function buttonFollow() {
    if (username === currentUser.username) {
      return (
        <button
          className="btn ml-auto"
          onClick={() => {
            setShowModal(true);
          }}
        >
          <i className="fa-solid fa-pen-to-square fa-xl"></i>
          <span className="mx-2 d-none d-lg-inline-block">Edit Profile</span>
        </button>
      );
    }
    const className =
      "fa-solid fa-xl " + (isFollow ? "fa-minus-square" : "fa-plus-square");
    return (
      <button
        className={"btn ml-auto " + (isFollow ? "" : "btn-primary")}
        onClick={handleFollow}
      >
        <i className={className}></i>
        <span className="d-none d-lg-inline-block mx-2">
          {isFollow ? "Un Follow" : "Follow"}
        </span>
      </button>
    );
  }

  const urlAvatarUser = () => {
    if (image) {
      return fetchImage(image);
    }
    if (userInfo.imageUrl) {
      return userInfo.imageUrl;
    }
    if (userInfo.avatar) {
      return fetchImage(userInfo.avatar);
    }
    return noAvatar;
  };

  const fetchData = async () => {
    await getDataUserInfo();
    urlAvatarUser();
    await fetchFollowerData();
    await fetchFollowingData();
    await fetchAllBadge();
  };

  useEffect(() => {
    fetchData();
  }, [username]);

  useEffect(() => {
    setUserEdit({
      username: userInfo?.username,
      name: userInfo?.name,
      gender: userInfo?.person?.gender,
      birthDate: userInfo?.person?.birthDate,
      address: userInfo?.person?.address,
      phone: userInfo?.person?.phone,
      bio: userInfo?.person?.bio,
      email: userInfo?.email,
    });
  }, [userInfo]);

  return (
    <div className="content">
      <Col md="12">
        <BannerTop bannerName={bannerName} breadcrumbs={breadcrumbs} />
      </Col>
      <Col>
        <div className="card card-user">
          <div className="image">
            <img alt="banner" src={banner} />
          </div>
          <div className="card-body">
            <Row className="py-0">
              <Col md="4" className="author mb-3 h-100">
                <img alt="avatar" className="avatar" src={urlAvatarUser()} />
                {username === currentUser.username && (
                  <div onClick={() => setShowModalImage(true)}>
                    <i className="fa-solid fa-pen-to-square text-danger"></i>
                  </div>
                )}
                <h5 className="title">{username}</h5>
                <p className="description">
                  {userInfo?.person?.bio ?? "Introduce yourself"}
                </p>
              </Col>

              <Col md="8" className="mb-3">
                <h5 className="title">{userInfo?.name ?? userInfo?.email}</h5>

                <div className="row">
                  <div className="col-6">
                    {userInfo?.stat?.badge
                      ? "Badge: " + userInfo?.stat?.badge?.name
                      : "Badge: ..."}
                  </div>
                  <div className="col-6">
                    Start from:{" "}
                    {userInfo?.createdAt ? formatDate(userInfo?.createdAt) : ""}
                  </div>
                </div>

                <div className="row">
                  <div className="col-6">
                    Birthday:{" "}
                    {userInfo?.person?.birthDate
                      ? formatDate(userInfo?.person?.birthDate)
                      : "..."}
                  </div>
                  <div className="col-6">
                    Gender: {userInfo?.person?.gender ?? "..."}
                  </div>
                </div>
                <div className="">
                  {" "}
                  Address: {userInfo?.person?.address ?? "..."}
                </div>

                <div className="d-flex justify-content-between col-10 mt-3">
                  <strong className="text-center">
                    Discussion Count: <br />{" "}
                    {userInfo?.stat?.discussionCount ?? 0}
                  </strong>
                  <strong className="text-center">
                    Comment Count: <br />
                    {userInfo?.stat?.commentCount ?? 0}
                  </strong>
                  <strong className="text-center">
                    Reputation:
                    <br /> {userInfo?.stat?.reputation ?? 0}
                  </strong>
                </div>
                <div className="d-flex justify-content-end mt-3">
                  {buttonFollow()}
                </div>
              </Col>
            </Row>
          </div>
        </div>
      </Col>
      <Col>
        <Tabs
          id="fill-tab-profile"
          activeKey={key}
          onSelect={(k) => setKey(k)}
          className="mb-3"
          fill
        >
          <Tab eventKey="home" title="Save Bookmark">
            <ListBookmark username={username} />
          </Tab>
          <Tab eventKey="activities" title={"Activities"}>
            <ActivitiesProfile username={username} userInfo={userInfo} />
          </Tab>

          <Tab eventKey="intro" title="Intro">
            <IntroProfile
              followers={followers}
              followings={followings}
              badges={badges}
            />
          </Tab>
        </Tabs>
      </Col>

      <ModalEditProfile
        show={showModal}
        handleClose={handleClose}
        handleUpdateInfo={handleUpdateInfo}
        user={userEdit}
      />

      <ModalEditImage
        show={showModalImage}
        handleClose={handleCloseImage}
        handleUpdateAvatar={handleUploadAvatar}
      />
    </div>
  );
};

export default MemberProfile;
