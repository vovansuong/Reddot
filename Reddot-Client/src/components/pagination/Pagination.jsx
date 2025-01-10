import ReactPaginate from "react-paginate"; // for pagination
import { AiFillLeftCircle, AiFillRightCircle } from "react-icons/ai"; // icons form react-icons
import { IconContext } from "react-icons"; // for customizing icons
import "./style.pagination.scss";
import PropTypes from "prop-types";

const Pagination = (props) => {
  const { pageSize, totalPages, handlePageClick } = props;

  return (
    <div className="page-container">
      <ReactPaginate
        onPageChange={handlePageClick}
        pageRangeDisplayed={pageSize}
        pageCount={totalPages}
        breakLabel="..."
        previousLabel={
          <IconContext.Provider value={{ color: "#B8C1CC", size: "36px" }}>
            <AiFillLeftCircle />
          </IconContext.Provider>
        }
        nextLabel={
          <IconContext.Provider value={{ color: "#B8C1CC", size: "36px" }}>
            <AiFillRightCircle />
          </IconContext.Provider>
        }
        containerClassName={"pagination"}
        pageClassName={"page-item"}
        activeClassName={"active"}
      />
    </div>
  );
};

Pagination.propTypes = {
  pageSize: PropTypes.number,
  totalPages: PropTypes.number,
  handlePageClick: PropTypes.func,
};

export default Pagination;
