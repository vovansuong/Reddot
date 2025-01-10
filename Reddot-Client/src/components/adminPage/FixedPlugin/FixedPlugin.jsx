import React from "react";
import PropTypes from "prop-types";

function FixedPlugin(props) {
  const { bgColor, activeColor, handleBgClick, handleActiveClick } = props;

  const [classes, setClasses] = React.useState("dropdown");

  const handleClick = () => {
    if (classes === "dropdown") {
      setClasses("dropdown show");
    } else {
      setClasses("dropdown");
    }
  };

  return (
    <div className="fixed-plugin">
      <div className={classes}>
        <button onClick={handleClick}>
          <i className="fa fa-cog fa-2x" />
        </button>
        <ul className="dropdown-menu show">
          <li className="header-title">SIDEBAR BACKGROUND</li>
          <li className="adjustments-line">
            <div className="badge-colors text-center">
              <button
                className={
                  bgColor === "black"
                    ? "badge filter badge-dark bg-dark active"
                    : "badge filter badge-dark bg-dark"
                }
                data-color="black"
                onClick={() => {
                  handleBgClick("black");
                }}
              />
              <button
                className={
                  bgColor === "white"
                    ? "badge filter badge-light active"
                    : "badge filter badge-light"
                }
                data-color="white"
                onClick={() => {
                  handleBgClick("white");
                }}
              />
            </div>
          </li>
          <li className="header-title">SIDEBAR ACTIVE COLOR</li>
          <li className="adjustments-line">
            <div className="badge-colors text-center">
              <button
                className={
                  activeColor === "primary"
                    ? "badge filter badge-primary active"
                    : "badge filter badge-primary"
                }
                data-color="primary"
                onClick={() => {
                  handleActiveClick("primary");
                }}
              />
              <button
                className={
                  activeColor === "info"
                    ? "badge filter badge-info active"
                    : "badge filter badge-info"
                }
                data-color="info"
                onClick={() => {
                  handleActiveClick("info");
                }}
              />
              <button
                className={
                  activeColor === "success"
                    ? "badge filter badge-success active"
                    : "badge filter badge-success"
                }
                data-color="success"
                onClick={() => {
                  handleActiveClick("success");
                }}
              />
              <button
                className={
                  activeColor === "warning"
                    ? "badge filter badge-warning active"
                    : "badge filter badge-warning"
                }
                data-color="warning"
                onClick={() => {
                  handleActiveClick("warning");
                }}
              />
              <button
                className={
                  activeColor === "danger"
                    ? "badge filter badge-danger active"
                    : "badge filter badge-danger"
                }
                data-color="danger"
                onClick={() => {
                  handleActiveClick("danger");
                }}
              />
            </div>
          </li>
        </ul>
      </div>
    </div>
  );
}

FixedPlugin.propTypes = {
  bgColor: PropTypes.string.isRequired,
  activeColor: PropTypes.string.isRequired,
  handleBgClick: PropTypes.func.isRequired,
  handleActiveClick: PropTypes.func.isRequired,
};

export default FixedPlugin;
