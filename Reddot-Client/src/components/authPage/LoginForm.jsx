import { Link, useNavigate, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { toast } from "react-toastify";
import { loginUser } from "../../redux/apiRequest";
import FormInput from "../FormInput";
import { validatePassword, validateNameOrEmail } from "../../utils/validUtils";

import SocialLogin from "./SocialLogin";

const LoginForm = () => {
  const location = useLocation();

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [showErrors, setShowErrors] = useState(false);

  const [username, setUsername] = useState("");
  const [validName, setValidName] = useState(false);
  const [userFocus, setUserFocus] = useState(false);

  const [password, setPassword] = useState("");
  const [validPwd, setValidPwd] = useState(false);
  const [pwdFocus, setPwdFocus] = useState(false);

  const [isShowPassword, setIsShowPassword] = useState(false);

  const isLoading = useSelector((state) => state.auth.login?.isFetching);
  const currentUser = useSelector((state) => state.auth.login?.currentUser);
  const error = useSelector((state) => state.auth.login?.error);

  useEffect(() => {
    let isUsernameOrEmail = validateNameOrEmail(username);
    setValidName(isUsernameOrEmail);
  }, [username]);

  useEffect(() => {
    setValidPwd(validatePassword(password));
  }, [password]);

  const handleLogin = async (e) => {
    e.preventDefault();
  
    setShowErrors(true); // Bật trạng thái hiển thị lỗi khi người dùng nhấn login
  
    // Kiểm tra nếu không hợp lệ thì dừng lại
    if (!username || !password) {
      // errRef.current.focus();
      return toast.error("Please enter email and password");
    }
    if (!validName || !validPwd) {
      // errRef.current.focus();
      return toast.error("Please enter valid information");
    }
  
    // Nếu hợp lệ, tiến hành đăng nhập
    const loginInfo = {
      username: username.trim(),
      password: password,
    };
    loginUser(loginInfo, dispatch);
  };
  
  useEffect(() => {
    // Bật trạng thái hiển thị lỗi mỗi khi người dùng nhập liệu
    setValidName(validateNameOrEmail(username));
    setValidPwd(validatePassword(password));
  }, [username, password]);
  
  

  const handleKeyDown = (e) => {
    if (e.keyCode === 13) {
      handleLogin();
    }
  };

  useEffect(() => {
    if (currentUser?.accessToken && currentUser?.roles?.length > 0) {
      navigate("/");
    }
  }, [currentUser, error, navigate]);

  // If the OAuth2 login encounters an error,
  // the user is redirected to the / login page with an error.
  // Here we display the error and then remove the error query parameter from the location.
  useEffect(() => {
    if (location.state?.error) {
      toast.error(location.state.error);
      navigate(location.pathname, { replace: true, state: {} });
    }
  }, [location, navigate]);

  return (
    <article className="auth-container content container mt-3 col-12 col-sm-8 col-lg-4 mx-auto">
      <h1 className="login-title">Welcome back</h1>
      <form onSubmit={handleLogin}>
        <FormInput
          id="username"
          type="text"
          value={username}
          valid={!showErrors || validName} // Không hiển thị lỗi nếu showErrors = false
          focus={userFocus}
          setFocus={setUserFocus}
          setValue={setUsername}
          validate={validateNameOrEmail}
          placeholder="Username or Email"
          errorMsg="Username must be 5-24 characters long and start with a letter. Letters, numbers, underscores, hyphens allowed. Or email must be format valid."
        />
        <div className="input-password">
        <FormInput
          id="password"
          type={isShowPassword ? "text" : "password"}
          value={password}
          valid={!showErrors || validPwd} // Không hiển thị lỗi nếu showErrors = false
          focus={pwdFocus}
          setFocus={setPwdFocus}
          setValue={setPassword}
          validate={validatePassword}
          placeholder="Password"
          errorMsg="Password is not valid"
          handleKeyDown={handleKeyDown}
        />
         <button
            className={
              isShowPassword
                ? "fa-solid fa-eye eye-login"
                : "fa-solid fa-eye-slash eye-login"
            }
            onClick={() => setIsShowPassword(!isShowPassword)}
            onKeyDown={() => setIsShowPassword(!isShowPassword)}
          ></button>
        </div>

        <div className="forgot-password">
          <Link to="/forgot-password" className="nav-link">
            <span>Forget password?</span>
          </Link>
        </div>

        <button
          type="submit"
          className={
            username && password
              ? "active mx-auto btn-auth"
              : "mx-auto btn-auth"
          }
          disabled={username && password ? "" : "disabled"}
        >
          {isLoading && <i className="fas fa-sync fa-spin"></i>}
          &nbsp;Login
        </button>
      </form>
      <div>
        <p className="login-subtitle">
          Do not have an account?
          <Link to="/register"> Register</Link>
        </p>
      </div>

      <div className="back">
        <Link to="/" className="nav-link">
          <i className="fa-solid fa-angles-left"></i> <></>
          Back to Home
        </Link>
      </div>
      <hr />

      <SocialLogin />
    </article>
  );
};

export default LoginForm;
