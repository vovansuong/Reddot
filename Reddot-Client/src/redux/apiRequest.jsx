// import axios from "axios";
import axios from "../services/customize-axios";
import {
  loginFailed,
  loginStart,
  loginSuccess,
  logOutFailed,
  logOutStart,
  logOutSuccess,
  registerFailed,
  registerStart,
  registerSuccess,
} from "./authSlice";
import { clearAvatar } from "./avatarSlice";
import { clearUserList } from "./userSlice";

import { toast } from "react-toastify";

export const loginUser = async (user, dispatch) => {
  dispatch(loginStart());
  try {
    let res = await axios.post("auth/signin", user, { withCredentials: true });
    if (res?.accessToken) {
      toast.success("Login successfully!");
      dispatch(loginSuccess(res));
    } else if (+res?.status !== 200) {
      toast.error("Username or password incorrect!");
      dispatch(loginFailed());
    }
  } catch (err) {
    toast.error(err.data.message);
    dispatch(loginFailed());
  }
};

export const registerUser = async (user, dispatch, navigate) => {
  dispatch(registerStart());
  try {
    let res = await axios.post("/auth/signup", user);
    if (+res?.status === 201) {
      dispatch(registerSuccess());
      navigate("/login");
      toast.success("Register successfully! Please check your email to confirm account registration.");
    } else if (+res?.data?.status === 400) {
      toast.error(res?.data?.message);
      dispatch(registerFailed());
    }
  } catch (err) {
    toast.error(err?.data?.message);
    dispatch(registerFailed());
  }
};

export const logOut = async (dispatch, id, navigate, accessToken, axiosJWT) => {
  dispatch(logOutStart());
  try {
    await axiosJWT.post("/auth/signout", id, {
      headers: { Authorization: `Bearer ${accessToken}` },
      withCredentials: true,
    });
    toast.success("Logout Successfully!");
  } catch (err) {
    console.log(`Something went wrong: ${err.message}`);
    toast.error("Something went wrong trying to logout!");
    dispatch(logOutFailed());
    navigate("/login");
  } finally {
    dispatch(logOutSuccess());
    dispatch(clearAvatar());
    dispatch(clearUserList());
  }
};

export const getCurrentUser = async (dispatch, accessToken) => {
  dispatch(loginStart());
  try {
    const res = await axios.get("/auth/user/me", {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (res?.accessToken) {
      dispatch(loginSuccess(res));
    } else if (+res?.data?.status === 500) {
      toast.error(res.data?.message);
      dispatch(loginFailed());
    }
  } catch (err) {
    toast.error(err?.data?.message);
    dispatch(loginFailed());
  }
};

export const forgotPassword = async (email) => {
  return await axios.post(`reset-password/request?email=${email}`);
};

export const resetPassword = async (passwordInfo) => {
  return await axios.post("reset-password/reset", passwordInfo);
};
