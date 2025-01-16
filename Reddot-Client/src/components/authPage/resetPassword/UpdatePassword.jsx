import { Link, useNavigate, useLocation } from "react-router-dom";
import { useState, useRef, useEffect } from "react";
import { toast } from "react-toastify";

import { validatePassword, validateConfirm } from "../../../utils/validUtils";
import FormInput from "../../FormInput";
import { resetPassword } from "../../../redux/apiRequest";
import { getUrlParameter } from "../../../utils/Helper";

const UpdatePassword = () => {
  const location = useLocation();
  const navigate = useNavigate();

  // const dispatch = useDispatch();

  const key = getUrlParameter("key", location);

  const errRef = useRef();

  const [password, setPassword] = useState("");
  const [validPwd, setValidPwd] = useState(false);
  const [pwdFocus, setPwdFocus] = useState(false);

  const [confirm, setConfirm] = useState("");
  const [validConfirm, setValidConfirm] = useState(false);
  const [confirmFocus, setConfirmFocus] = useState(false);

  const [isShowPassword, setIsShowPassword] = useState(false);

  const [errMsg, setErrMsg] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setValidPwd(validatePassword(password));
    setValidConfirm(validateConfirm(password, confirm));
  }, [password, confirm]);

  useEffect(() => {
    setErrMsg("");
  }, [password, confirm]);

  const handleUpdatePassword = async () => {
    setIsLoading(true);

    if (!validPwd) {
      return setErrMsg("Please enter a valid password");
    }

    if (!validConfirm) {
      return setErrMsg("Password and confirm password should match");
    }

    setErrMsg("");

    const passwordInfo = {
      key: key,
      newPassword: password,
    };
    const res = await resetPassword(passwordInfo);
    if (+res?.status === 200) {
      navigate("/login");
      toast.success(res?.message);
    } else {
      toast.error(res?.data?.message);
      console.log(`Error: `, res?.data?.message);
    }
    setIsLoading(false);
    return true;
  };

  const handleKeyDown = (e) => {
    if (e.keyCode === 13) {
      handleUpdatePassword();
    }
  };

  const isAction = () => {
    return validPwd && validConfirm;
  };

  return (
    <article className="auth-container container mt-3 col-12 col-sm-8 col-lg-4 mx-auto">
      <h1 className="login-title">Update Password</h1>
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
        <FormInput
          id="password"
          type={isShowPassword ? "text" : "password"}
          value={password}
          valid={validPwd}
          focus={pwdFocus}
          setFocus={setPwdFocus}
          setValue={setPassword}
          validate={validatePassword}
          placeholder="Password (*)"
          errorMsg="Password is not valid"
          handleKeyDown={handleKeyDown}
        />
        <button
          className={
            isShowPassword ? "fa-solid fa-eye" : "fa-solid fa-eye-slash"
          }
          onClick={() => setIsShowPassword(!isShowPassword)}
          onKeyDown={() => setIsShowPassword(!isShowPassword)}
        ></button>
      </div>

      <div className="input-password">
        <FormInput
          id="confirm"
          type={isShowPassword ? "text" : "password"}
          value={confirm}
          valid={validConfirm}
          focus={confirmFocus}
          setFocus={setConfirmFocus}
          setValue={setConfirm}
          validate={validateConfirm}
          placeholder="Confirm Password (*)"
          errorMsg="Passwords do not match"
        />
        <button
          className={
            isShowPassword ? "fa-solid fa-eye" : "fa-solid fa-eye-slash"
          }
          onClick={() => setIsShowPassword(!isShowPassword)}
          onKeyDown={() => setIsShowPassword(!isShowPassword)}
        ></button>
      </div>

      <button
        className={isAction() ? "active mx-auto btn-auth" : "mx-auto btn-auth"}
        disabled={isAction() ? +false : +true}
        onClick={() => handleUpdatePassword()}
      >
        {isLoading && <i className="fas fa-sync fa-spin"></i>}
        &nbsp;Save change
      </button>

      <p className="login-subtitle">
        Do you have an account? <Link to="/login">Sign in</Link>
      </p>

      <div className="back">
        <Link to="/" className="nav-link">
          <i className="fa-solid fa-angles-left"></i> <></>
          Back to Home
        </Link>
      </div>
    </article>
  );
};

export default UpdatePassword;
