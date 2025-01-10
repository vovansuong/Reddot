import PropTypes from "prop-types";

import Breadcrumb from "react-bootstrap/Breadcrumb";

const BannerTop = ({ bannerName = "", breadcrumbs = [] }) => {
  return (
    <div className="banner-top-container my-3">
      {breadcrumbs && breadcrumbs.length > 0 && (
        <Breadcrumb className="col-12" aria-label="breadcrumb">
          <Breadcrumb.Item href="/">
            <i className="fa-solid fa-house"></i>
          </Breadcrumb.Item>

          {breadcrumbs?.map((item) => {
            return (
              <Breadcrumb.Item key={item.id} href={item.link}>
                {item.name}
              </Breadcrumb.Item>
            );
          })}
        </Breadcrumb>
      )}
      {bannerName?.length > 0 && (
        <span
          className="banner-page-item w-100"
          style={{ backgroundColor: "#FFA500" }}
        >
          <div className="title-page">
            <h3 style={{ color: "#white" }}>{bannerName}</h3>
          </div>
        </span>
      )}
    </div>
  );
};

// Rest of the component code...
BannerTop.propTypes = {
  bannerName: PropTypes.string.isRequired,
  breadcrumbs: PropTypes.array.isRequired,
};

export default BannerTop;
