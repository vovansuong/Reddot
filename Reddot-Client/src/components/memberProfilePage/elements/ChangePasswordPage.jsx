import { useParams } from "react-router-dom";
import { useEffect, useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import BannerTop from "../../bannerTop/BannerTop";

import { postUpdatePassword } from "../../../services/userService/UserService";

import styled from "styled-components";

const StyledInput = styled.input`
  color: ${({ valid }) => (valid ? "green" : "red")};
  border: 1px solid ${({ valid }) => (valid ? "green" : "red")}!important;
  maxwidth: 700px !important;
`;
import { PWD_REGEX } from "../../../constants";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import { toast } from "react-toastify";

const ChangePassword = () => {
  const { username } = useParams();
  const bannerName = "";
  const breadcrumbs = [
    {
      id: 1,
      name: `Profile - ${username}`,
      link: `/member-profile/${username}`,
    },
    { id: 2, name: `Change Password`, link: `` },
  ];

  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const errRef = useRef();

  const [oldPwd, setOldPwd] = useState("");
  const [oldValidPwd, setOldValidPwd] = useState(false);
  const [oldPwdFocus, setOldPwdFocus] = useState(false);

  const [password, setPassword] = useState("");
  const [validPwd, setValidPwd] = useState(false);
  const [pwdFocus, setPwdFocus] = useState(false);

  const [confirm, setConfirm] = useState("");
  const [validConfirm, setValidConfirm] = useState(false);
  const [confirmFocus, setConfirmFocus] = useState(false);

  const [isShowOldPwd, setIsShowOldPwd] = useState(false);
  const [isShowPassword, setIsShowPassword] = useState(false);

  const [errMsg, setErrMsg] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setOldValidPwd(PWD_REGEX.test(oldPwd));
  }, [oldPwd]);

  useEffect(() => {
    if (oldPwd === password) {
      setErrMsg("New password must be different from old password");
    }
  }, [oldPwd, password]);

  useEffect(() => {
    setValidPwd(PWD_REGEX.test(password));
    setValidConfirm(password === confirm);
  }, [password, confirm]);

  useEffect(() => {
    setErrMsg("");
  }, [oldPwd, password, confirm]);

  const handleChangePwd = async () => {
    const v1 = PWD_REGEX.test(oldPwd);
    const v2 = PWD_REGEX.test(password);
    const v3 = PWD_REGEX.test(confirm);
    const v4 = oldPwd === password;
    if (!v1 || !v2 || !v3) {
      setErrMsg("Invalid password");
      return;
    }
    if (v4) {
      setErrMsg("New password must be different from old password");
      return;
    }

    setIsLoading(true);
    console.log(`Change password`);
    const pwdData = {
      username,
      oldPassword: oldPwd,
      newPassword: password,
    };
    const axiosJWT = createAxios(currentUser, dispatch, loginSuccess);
    let res = await postUpdatePassword(
      pwdData,
      axiosJWT,
      currentUser?.accessToken,
    );
    console.log(`Here`, JSON.stringify(res));
    if (res && +res.status === 200) {
      toast.success(`Change password success`);
      toast.success(
        "Next time, you will be able to log in with a new password.",
      );
      setOldPwd("");
      setPassword("");
      setConfirm("");
      // redirect to back page
      navigate(-1);
    } else {
      console.log(`Change password failed`);
      setErrMsg("Change password failed");
    }

    setIsLoading(false);
    return null;
  };

  const handleKeyDown = (e) => {
    if (e.keyCode === 13) {
      // handleRegister();
      console.log(`Click enter`);
    }
  };

  const isAction = () => {
    return validPwd && validConfirm;
  };

  return (
    <div className="content change-pass">
      <div className="col-12">
        <BannerTop bannerName={bannerName} breadcrumbs={breadcrumbs} />
      </div>
      <div className="auth-container container mt-3 col-12 col-sm-8 col-lg-4 mx-auto">
        <h1 className="login-title">Change Password {username}</h1>
        {errMsg && (
          <p
            className="alert alert-danger"
            role="alert"
            ref={errRef}
            aria-live="assertive"
            aria-atomic="true"
          >
            {errMsg}
          </p>
        )}
        <div className="input-password">
          <StyledInput
            type={isShowOldPwd ? "text" : "password"}
            id="email"
            placeholder="Enter old password (*)"
            value={oldPwd}
            onChange={(e) => {
              setOldPwd(e.target.value)
              setOldPwdFocus(true)
            }
            } 
            required
            aria-invalid={!oldValidPwd}
            aria-describedby="email-err"
            onBlur={() => setOldPwdFocus(false)}
            valid={+(oldPwd.length === 0 || oldValidPwd)}
          />
          <button
            className={
              isShowOldPwd ? "fa-solid fa-eye" : "fa-solid fa-eye-slash"
            }
            onClick={() => setIsShowOldPwd(!isShowOldPwd)}
            onKeyDown={() => setIsShowOldPwd(!isShowOldPwd)}
          ></button>
        </div>
        <small
          id="email-err"
          className={
            (oldPwdFocus && oldPwd) || !oldValidPwd
              ? "text-danger"
              : "invalid-feedback"
          }
          role="alert"
          hidden={oldValidPwd || !oldPwdFocus}
        >
          <i className="fa fa-info-circle" aria-hidden="true"></i> Password must
          be 8-24 characters long. <br />
          <i className="fa fa-info-circle" aria-hidden="true"></i> And contain
          at least one lowercase letter, one uppercase letter, one number.{" "}
          <br />
          <i className="fa fa-info-circle" aria-hidden="true"></i> And one
          special character (<span aria-label="exclamation mark">! </span>
          <span aria-label="at symbol">@ </span>
          <span aria-label="hashtag"># </span>
          <span aria-label="dollar sign">$ </span>
          <span aria-label="percent">%</span>)
        </small>
        <div className="input-password">
          <StyledInput
            type={isShowPassword ? "text" : "password"}
            id="password"
            placeholder="Enter new password (*)"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value)
              setPwdFocus(true)
            }
            }
            required
            aria-invalid={!validPwd}
            aria-describedby="password-err"
            onBlur={() => setPwdFocus(false)}
            valid={+(password.length === 0 || validPwd)}
          />
          <button
            className={
              isShowPassword ? "fa-solid fa-eye" : "fa-solid fa-eye-slash"
            }
            onClick={() => setIsShowPassword(!isShowPassword)}
            onKeyDown={() => setIsShowPassword(!isShowPassword)}
          ></button>
        </div>
        <small
          id="password-err"
          className={
            (pwdFocus && password) || !validPwd
              ? "text-danger"
              : "invalid-feedback"
          }
          role="alert"
          hidden={validPwd || !pwdFocus}
        >
          <i className="fa fa-info-circle" aria-hidden="true"></i> Password must
          be 8-24 characters long. <br />
          <i className="fa fa-info-circle" aria-hidden="true"></i> And contain
          at least one lowercase letter, one uppercase letter, one number.{" "}
          <br />
          <i className="fa fa-info-circle" aria-hidden="true"></i> And one
          special character (<span aria-label="exclamation mark">! </span>
          <span aria-label="at symbol">@ </span>
          <span aria-label="hashtag"># </span>
          <span aria-label="dollar sign">$ </span>
          <span aria-label="percent">%</span>)
        </small>
        <div className="input-password">
          <StyledInput
            type={isShowPassword ? "text" : "password"}
            id="confirm"
            placeholder="Confirm new password (*)"
            value={confirm}
            onChange={(e) => {
              setConfirm(e.target.value)
              setConfirmFocus(true) 
            }
            }
            onKeyDown={(e) => handleKeyDown(e)}
            required
            aria-invalid={!validConfirm}
            aria-describedby="confirm-err"
            onBlur={() => setConfirmFocus(false)}
            valid={+(confirm.length === 0 || validConfirm)}
          />
          <button
            className={
              isShowPassword ? "fa-solid fa-eye" : "fa-solid fa-eye-slash"
            }
            onClick={() => setIsShowPassword(!isShowPassword)}
            onKeyDown={() => setIsShowPassword(!isShowPassword)}
          ></button>
        </div>
        <small
          id="confirm-err"
          className={
            (confirmFocus && confirm) || !validConfirm
              ? "text-danger"
              : "invalid-feedback"
          }
          role="alert"
          hidden={validConfirm || !confirmFocus}
        >
          <i className="fa fa-info-circle" aria-hidden="true"></i> Passwords do
          not match
        </small>
        <div className="forgot-password">
          <Link to="/forgot-password" className="nav-link">
            <span>Forget password?</span>
          </Link>
        </div>
        <button
          className={
            isAction() ? "active mx-auto btn-auth" : "mx-auto btn-auth"
          }
          disabled={isAction() ? +false : +true}
          onClick={handleChangePwd}
        >
          {isLoading && <i className="fas fa-sync fa-spin"></i>}
          &nbsp;Update new password
        </button>
        <div className="back">
          <Link to="/" className="nav-link">
            <i className="fa-solid fa-angles-left"></i> <></>
            Back to Home
          </Link>
        </div>
      </div>
    </div>
  );
};

export default ChangePassword;
