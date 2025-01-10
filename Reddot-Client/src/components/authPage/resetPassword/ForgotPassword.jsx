import { Link } from "react-router-dom";
import { useState, useEffect } from "react";

import StyledInput from "../StyledInput";

import { validateEmail } from "../../../utils/validUtils";
import { forgotPassword } from "../../../redux/apiRequest";
import { toast } from "react-toastify";

const ForgotPassword = () => {
  const [emailRP, setEmailRP] = useState("");
  const [validEmailRP, setValidEmailRP] = useState(false);
  const [emailRPFocus, setEmailRPFocus] = useState(false);

  const [errMsg, setErrMsg] = useState("");
  const [success, setSuccess] = useState(false);

  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setValidEmailRP(validateEmail(emailRP));
    setErrMsg("");
  }, [emailRP]);

  const handleResetPassword = async () => {
    setIsLoading(true);

    if (!validEmailRP) {
      return setErrMsg("Please enter a valid email address");
    }

    const res = await forgotPassword(emailRP);
    if (+res?.status === 200) {
      toast.success(res?.message);
      setSuccess(true);
    } else {
      toast.error(res?.data?.message);
      console.log(`Error: `, res?.data?.message);
    }

    setIsLoading(false);
    return true;
  };

  const isAction = () => {
    return validEmailRP;
  };

  return (
    <article className="auth-container container mt-3 col-12 col-sm-8 col-lg-4 mx-auto">
      <h1 className="login-title">Reset Password</h1>
      <p>Please enter your email address to reset your password</p>

      {success && (
        <div className="alert alert-success">
          Register successfully. Redirecting to login page...
        </div>
      )}
      {errMsg && <div className="alert alert-danger">{errMsg}</div>}

      <StyledInput
        type="text"
        id="emailRP"
        placeholder="Email address (*)"
        value={emailRP}
        onChange={(e) => setEmailRP(e.target.value)}
        required
        aria-invalid={!validEmailRP}
        aria-describedby="emailRP-err"
        onFocus={() => setEmailRPFocus(true)}
        onBlur={() => setEmailRPFocus(false)}
        valid={+(emailRP.length === 0 || validEmailRP)}
      />
      <small
        id="emailRP-err"
        className={
          (emailRPFocus && emailRP) || !validEmailRP
            ? "text-danger"
            : "invalid-feedback"
        }
        role="alert"
        hidden={validEmailRP || !emailRPFocus}
      >
        <i className="fa fa-info-circle" aria-hidden="true"></i> Please enter a
        valid email address
      </small>

      <button
        className={isAction() ? "active mx-auto btn-auth" : "mx-auto btn-auth"}
        disabled={isAction() ? +false : +true}
        onClick={() => handleResetPassword()}
      >
        {isLoading && <i className="fas fa-sync fa-spin"></i>}
        &nbsp;Reset Password
      </button>

      <div className="back">
        <Link to="/" className="nav-link">
          <i className="fa-solid fa-angles-left"></i> <></>
          Back to Home
        </Link>
      </div>

      <hr />
      <p>
        If you do not have an account, you can{" "}
        <Link to="/register">register here</Link>
      </p>
    </article>
  );
};

export default ForgotPassword;
