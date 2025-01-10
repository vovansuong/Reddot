import { Link } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";

import BannerTop from "../bannerTop/BannerTop";
import Pagination from "../pagination/Pagination";

import noAvatar from "../../assets/img/default-avatar.png";
import Avatar from "../avatar/Avatar";

import { getAllUserStats } from "../../services/userService/UserStatService";
import { fetchImage } from "../../services/userService/UserService";
import { formatDifferentUpToNow } from "../../utils/FormatDateTimeHelper";

const MemberList = () => {
  const bannerName = "Member List";
  const breadcrumbs = [{ id: 1, name: "Members", link: "/members" }];

  const [userStatList, setUserStatList] = useState([]);
  const [totalUsers, setTotalUsers] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [orderBy, setOrderBy] = useState("id"); //default orderBy userId
  const [sortBy, setSortBy] = useState("ASC");

  const handlePageClick = (event) => {
    setPage(+event.selected + 1);
    // getDataUserStat();
    return true;
  };

  const handleSort = (sortBy, orderBy) => {
    setSortBy(sortBy);
    setOrderBy(orderBy);
    // getDataUserStat();
    return true;
  };

  const getDataUserStat = useCallback(async () => {
    let pageData = {
      page: page,
      size: pageSize,
      orderBy: orderBy,
      sort: sortBy,
    };
    let res = await getAllUserStats(pageData);
    if (res?.data.length > 0) {
      const { pageSize, totalPages, totalItems, data } = res;
      setUserStatList(data);
      setPageSize(pageSize);
      setTotalPages(totalPages);
      setTotalUsers(totalItems);
    }
    return true;
  }, [page, pageSize, orderBy, sortBy]);

  useEffect(() => {
    getDataUserStat();
  }, [page, pageSize, orderBy, sortBy, getDataUserStat]);

  const tableList = (memberList) => {
    if (memberList.length == 0) {
      return (
        <div className="text-center">
          <span className="d-flex justify-content-center">
            <i className="fas fa-sync fa-spin fa-5x"></i>
            <br />
          </span>
          <h5>No data</h5>
        </div>
      );
    }
    return (
      <table className="table responsive">
        <thead className="text-primary">
          <tr>
            <th>
              <span>Member&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "username")}
                  onKeyDown={() => {
                    handleSort("DESC", "username");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "username")}
                  onKeyDown={() => {
                    handleSort("ASC", "username");
                  }}
                ></button>
              </span>
            </th>
            <th style={{ textAlign: "right" }}>
              <span>Discussion&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "stat.discussionCount")}
                  onKeyDown={() => {
                    handleSort("DESC", "stat.discussionCount");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "stat.discussionCount")}
                  onKeyDown={() => {
                    handleSort("ASC", "stat.discussionCount");
                  }}
                ></button>
              </span>
            </th>
            <th style={{ textAlign: "right" }}>
              <span>Comments&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "stat.commentCount")}
                  onKeyDown={() => {
                    handleSort("DESC", "stat.commentCount");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "stat.commentCount")}
                  onKeyDown={() => {
                    handleSort("ASC", "stat.commentCount");
                  }}
                ></button>
              </span>
            </th>
            <th style={{ textAlign: "right" }}>
              <span>Join Forum&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "createdAt")}
                  onKeyDown={() => {
                    handleSort("DESC", "createdAt");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "createdAt")}
                  onKeyDown={() => {
                    handleSort("ASC", "createdAt");
                  }}
                ></button>
              </span>
            </th>
            <th style={{ textAlign: "right" }}>
              <span>Reputation&nbsp;</span>
              <span className="d-inline-block">
                <button
                  className="fa-solid fa-arrow-down-long"
                  onClick={() => handleSort("DESC", "stat.reputation")}
                  onKeyDown={() => {
                    handleSort("DESC", "stat.reputation");
                  }}
                ></button>
                <button
                  className="fa-solid fa-arrow-up-long"
                  onClick={() => handleSort("ASC", "stat.reputation")}
                  onKeyDown={() => {
                    handleSort("ASC", "stat.reputation");
                  }}
                ></button>
              </span>
            </th>
          </tr>
        </thead>
        <tbody>
          {memberList?.map((item) => {
            return (
              <tr key={item?.userId}>
                <td>
                  <Link
                    to={"/member-profile/" + item.username}
                    className="text-decoration-none"
                  >
                    <Avatar
                      src={
                        item?.avatar !== null
                          ? fetchImage(item?.avatar)
                          : (item?.imageUrl ?? noAvatar)
                      }
                      username={item?.name ?? item?.username}
                      height={50}
                      width={50}
                    />
                  </Link>
                </td>
                <td style={{ textAlign: "right" }}>
                  {item.userStat?.discussionCount}
                </td>
                <td style={{ textAlign: "right" }}>
                  {item.userStat?.commentCount}
                </td>
                <td style={{ textAlign: "right" }}>
                  {item?.joinDate ? formatDifferentUpToNow(item.joinDate) : ""}
                </td>
                <td style={{ textAlign: "right" }}>
                  {" "}
                  {item.userStat?.reputation}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    );
  };

  return (
    <section className="members-container content">
      <div className="col-12">
        <BannerTop bannerName={bannerName} breadcrumbs={breadcrumbs} />
      </div>
      <div className="col-12">
        <div className="card px-2">
          <div className="card-header">
            <div className="row d-flex justify-content-around">
              <span className="col-md-4 mb-2 mb-lg-0">
                <h5>
                  Total: {totalUsers} user(s)/page{page}
                </h5>
              </span>

              <span className="col-md-2 d-flex align-items-center">
                <label htmlFor="page" className="col-auto">
                  Page size:
                </label>
                <select
                  id="page"
                  name="page"
                  className="form-select"
                  onChange={(e) => setPageSize(e.currentTarget.value)}
                  style={{ minWidth: 120 }}
                >
                  <option value="5">05 per page</option>
                  <option value="10">10 per page</option>
                  <option value="20">20 per page</option>
                  <option value="50">50 per page</option>
                </select>
              </span>
            </div>
          </div>
          <div className="card-body">
            {tableList(userStatList)}

            <Pagination
              handlePageClick={handlePageClick}
              pageSize={pageSize}
              totalPages={totalPages}
            />
          </div>
        </div>
      </div>
    </section>
  );
};

export default MemberList;
