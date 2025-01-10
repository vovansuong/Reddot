import { useNavigate } from "react-router-dom";

const Unauthorized = () => {
  const navigate = useNavigate();

  const goBack = () => navigate(-1);
  const login = () => navigate("/login");

  return (
    <section className="content text-center">
      <h1>Unauthorized</h1>
      <br />
      <p>You do not have access to the requested page.</p>
      <div className="flexGrow">
        <button className="btn btn-primary" onClick={goBack}>
          Go Back
        </button>
        <button className="btn btn-primary" onClick={login}>
          Login
        </button>
      </div>
    </section>
  );
};

export default Unauthorized;
