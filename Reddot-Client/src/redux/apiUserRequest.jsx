import {
  deleteUserFailed,
  deleteUsersSuccess,
  deleteUserStart,
  getUsersFailed,
  getUsersStart,
  getUsersSuccess,
} from "./userSlice";

import {
  uploadAvatarStart,
  uploadAvatarSuccess,
  uploadAvatarFailed,
} from "./avatarSlice";

import { pathParams } from "../utils/Helper";

export const getAllUsers = async (
  accessToken,
  dispatch,
  axiosJWT,
  pageData,
) => {
  dispatch(getUsersStart());
  try {
    let path = pathParams(pageData);
    let res = await axiosJWT.get(`admin/users?${path}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
      withCredentials: true,
    });
    dispatch(getUsersSuccess(res.data));
    return res.data;
  } catch (err) {
    dispatch(getUsersFailed());
    console.log(`Error`, JSON.stringify(err?.message));
  }
};

export const deleteUser = async (accessToken, dispatch, id, axiosJWT) => {
  dispatch(deleteUserStart());
  try {
    const res = await axiosJWT.delete("/users/" + id, {
      headers: { token: `Bearer ${accessToken}` },
    });
    dispatch(deleteUsersSuccess(res.data));
  } catch (err) {
    dispatch(deleteUserFailed(err.message));
  }
};

export const uploadAvatar = async (
  dispatch,
  accessToken,
  axiosJWT,
  data,
  username,
) => {
  dispatch(uploadAvatarStart());
  try {
    const res = await axiosJWT.post(
      `account-info/update-avatar/${username}`,
      data,
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "multipart/form-data",
        },
      },
    );
    if (+res?.data?.status === 200) {
      dispatch(uploadAvatarSuccess(res?.data?.data));
    }
    return res.data;
  } catch (err) {
    dispatch(uploadAvatarFailed());
  }
};
