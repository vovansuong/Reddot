import { Link } from "react-router-dom";

const NotFound = () => {
  return (
    <article className="content text-center">
      <h1>Oops!</h1>
      <p>Page Not Found</p>
      <div className="flexGrow">
        <Link to="/">Visit Our Homepage</Link>
      </div>
    </article>
  );
};

export default NotFound;
