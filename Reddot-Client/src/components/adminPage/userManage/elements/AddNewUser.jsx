import { useState, useRef, useEffect } from "react";
import { useSelector } from "react-redux";
import PropTypes from "prop-types";
import { ROLES } from "../../../../constants";

import {
  validateName,
  validateEmail,
  validatePassword,
  validateConfirm,
} from "../../../../utils/validUtils";

import "./style.scss";
import FormInput from "../../../formInput/FormInput";

const convertRoleToArray = (role) => {
  const roles = [];
  switch (role) {
    case ROLES.ADMIN:
      roles.push("admin", "mod", "user");
      break;
    case ROLES.MOD:
      roles.push("mod", "user");
      break;
    default:
      roles.push("user");
      break;
  }
  return roles;
};

const AddNewUser = (props) => {
  const { handleShowAdd, registerUser } = props;

  const errRef = useRef();

  const [username, setUsername] = useState("");
  const [validName, setValidName] = useState(false);
  const [userFocus, setUserFocus] = useState(false);

  const [email, setEmail] = useState("");
  const [validEmail, setValidEmail] = useState(false);
  const [emailFocus, setEmailFocus] = useState(false);

  const [password, setPassword] = useState("");
  const [validPwd, setValidPwd] = useState(false);
  const [pwdFocus, setPwdFocus] = useState(false);

  const [confirm, setConfirm] = useState("");
  const [validConfirm, setValidConfirm] = useState(false);
  const [confirmFocus, setConfirmFocus] = useState(false);

  const [role, setRole] = useState(null);
  // const [roles, setRoles] = useState([]);

  const [isShowPassword, setIsShowPassword] = useState(false);

  const [errMsg, setErrMsg] = useState("");

  const isLoading = useSelector((state) => state.auth.login?.isFetching);

  useEffect(() => {
    setValidName(validateName(username));
  }, [username]);

  useEffect(() => {
    setValidEmail(validateEmail(email));
  }, [email]);

  useEffect(() => {
    setValidPwd(validatePassword(password));
    setValidConfirm(validateConfirm(password, confirm));
  }, [password, confirm]);

  useEffect(() => {
    setErrMsg("");
  }, [username, email, password, confirm]);

  const handleRegister = async () => {
    if (
      !validateName(username) ||
      !validateEmail(email) ||
      !validatePassword(password)
    ) {
      setErrMsg("Please enter valid information");
      return;
    }
    const registerInfo = {
      username,
      email,
      password,
      roles: convertRoleToArray(role),
    };
    console.log(`Check role`, JSON.stringify(registerInfo));
    let result = await registerUser(registerInfo);
    if (result) {
      setUsername("");
      setEmail("");
      setPassword("");
      setConfirm("");
    }
  };

  const handleKeyDown = (e) => {
    if (e.keyCode === 13) {
      handleRegister();
    }
  };

  const isAction = () => {
    return validName && validEmail && validPwd && validConfirm;
  };

  return (
    <article className="card col-12 mx-auto p-3">
      <h3 className="login-title">Create an account</h3>

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

      <div className="row mb-3">
        <div className="form-group col-md-4">
          <FormInput
            id="username"
            type="text"
            value={username}
            valid={validName}
            focus={userFocus}
            setFocus={setUserFocus}
            setValue={setUsername}
            validate={validateName}
            placeholder="Username (*)"
            errorMsg="Username must be 5-24 characters long and start with a letter. Letters, numbers, underscores, hyphens allowed."
          />
        </div>
        <div className="form-group col-md-4">
          <FormInput
            id="email"
            type="text"
            value={email}
            valid={validEmail}
            focus={emailFocus}
            setFocus={setEmailFocus}
            setValue={setEmail}
            validate={validateEmail}
            placeholder="Email address (*)"
            errorMsg="Please enter a valid email address"
          />
        </div>

        <div className="row col-md-4">
          <select
            className="form-control"
            id="role"
            data-placeholder="Choose role"
            onChange={(e) => setRole(e.target.value)}
          >
            <option value={ROLES.ADMIN}>Role - Admin</option>
            <option value={ROLES.MOD}>Role - Mod</option>
            <option value={ROLES.USER}>Role - User</option>
          </select>
        </div>
      </div>

      <div className="row mb-3">
        <div className="form-group col-md-6">
          <div className="input-password-eye">
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
            />
            <button
              className={
                isShowPassword ? "fa-solid fa-eye" : "fa-solid fa-eye-slash"
              }
              onClick={() => setIsShowPassword(!isShowPassword)}
              onKeyDown={() => setIsShowPassword(!isShowPassword)}
            ></button>
          </div>
        </div>
        <div className="form-group col-md-6">
          <div className="input-password-eye">
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
        </div>
      </div>

      <div>
        <button className="btn btn-secondary mx-2" onClick={handleShowAdd}>
          Cancel
        </button>
        <button
          className={isAction() ? "active btn mx-auto" : "btn mx-auto"}
          disabled={isAction() ? +false : +true}
          onClick={handleRegister}
        >
          {isLoading && <i className="fas fa-sync fa-spin"></i>}
          &nbsp;Register
        </button>
      </div>
    </article>
  );
};

AddNewUser.propTypes = {
  handleShowAdd: PropTypes.func.isRequired,
  registerUser: PropTypes.func.isRequired,
};
export default AddNewUser;
