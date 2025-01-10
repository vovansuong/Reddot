import { GOOGLE_AUTH_URL, GITHUB_AUTH_URL } from "../../constants";
import googleLogo from "../../assets/img/google-logo.png";
// import fbLogo from "../../assets/img/fb-logo.png";
import githubLogo from "../../assets/img/github-logo.png";
import "./SocialLogin.scss";

const SocialLogin = () => {
  return (
    <div className="social-login">
      <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
        <img src={googleLogo} alt="Google" />
        <span>Log in with Google</span>
      </a>
      <a className="btn btn-block social-btn github" href={GITHUB_AUTH_URL}>
        <img src={githubLogo} alt="Github" />
        <span>Log in with Github</span>
      </a>
    </div>
  );
};

export default SocialLogin;
