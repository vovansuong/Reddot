import { useEffect, useState, useCallback } from "react";
import { useDispatch, useSelector } from "react-redux";
import { createAxios } from "../../../services/createInstance";
import { loginSuccess } from "../../../redux/authSlice";
import {
  getTotalData,
  getDataChart,
} from "../../../services/adminDashboardService/dashboardService";

import { Row, Col } from "react-bootstrap";

import CardInfo from "./elements/CardInfo";
import UsersBehaviorChart from "./elements/UsersBehaviorChart";
import ForumsStatisticsPieChart from "./elements/ForumsStatisticsPieChart";
import LineChartComponent from "./elements/LineChartComponent";

const DashBoard = () => {
  const dispatch = useDispatch();
  let currentUser = useSelector((state) => state.auth.login?.currentUser);
  let axiosJWT = createAxios(currentUser, dispatch, loginSuccess);

  //{totalUsers: 4, totalForums: 2, totalDiscussions: 0, totalComments: 0, totalTags: 0}
  const [totalData, setTotalData] = useState({
    totalUsers: 0,
    totalForums: 0,
    totalDiscussions: 0,
    totalComments: 0,
    totalTags: 0,
  });

  const [dataChart, setDataChart] = useState([]);

  const fetchData = useCallback(async () => {
    let res = await getTotalData(currentUser?.accessToken, axiosJWT);
    if (res.status === 200 || res?.data?.status === 200) {
      setTotalData(res.data?.data);
    } else {
      console.log(res?.message);
    }
  }, [currentUser, axiosJWT]);

  const fetchDataChart = useCallback(async () => {
    let res = await getDataChart(currentUser?.accessToken, axiosJWT);
    if (res?.status === 200 || res?.data?.status === 200) {
      setDataChart(res.data?.data);
      console.log(res?.data?.data);
    } else {
      console.log(res?.message);
    }
  }, [currentUser, axiosJWT]);

  const handleUpdateNumber = () => {
    fetchData();
  };

  const handleUpdateDataChart = () => {
    fetchDataChart();
  };

  useEffect(() => {
    fetchData();
    fetchDataChart();
  }, []);

  return (
    <article className="dashboard content">
      <Row>
        <Col lg="3" md="4" sm="6">
          <CardInfo
            title={"users"}
            icon={"fa-solid fa-user text-danger"}
            number={totalData?.totalUsers}
            updateNumber={handleUpdateNumber}
          />
        </Col>
        <Col lg="3" md="4" sm="6">
          <CardInfo
            title={"Discussions"}
            icon={"fa-solid fa-signs-post text-warning"}
            number={totalData?.totalDiscussions}
            updateNumber={handleUpdateNumber}
          />
        </Col>
        <Col lg="3" md="4" sm="6">
          <CardInfo
            title={"comments"}
            icon={"fa-solid fa-comment text-info"}
            number={totalData.totalComments}
            updateNumber={handleUpdateNumber}
          />
        </Col>
        <Col lg="3" md="4" sm="6">
          <CardInfo
            title={"tags"}
            icon={"fa-solid fa-tags text-success"}
            number={totalData.totalTags}
            updateNumber={handleUpdateNumber}
          />
        </Col>
      </Row>
      <Row>
        <Col md="12">
          <UsersBehaviorChart
            dataChart={dataChart}
            updateData={handleUpdateDataChart}
          />
        </Col>
      </Row>
      <Row>
        <Col md="6" lg="5" xl="4">
          <ForumsStatisticsPieChart
            dataChart={dataChart}
            updateData={handleUpdateDataChart}
          />
        </Col>
        <Col md="6" lg="7" xl="8">
          <LineChartComponent
            dataChart={dataChart}
            updateData={handleUpdateDataChart}
          />
        </Col>
      </Row>
    </article>
  );
};

export default DashBoard;
