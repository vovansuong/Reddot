import { useState, useCallback } from "react";
import { Card } from "react-bootstrap";
import { PieChart, Pie, Sector } from "recharts";
import PropTypes from "prop-types";

const renderActiveShape = (props) => {
  const RADIAN = Math.PI / 180;
  const {
    cx,
    cy,
    midAngle,
    innerRadius,
    outerRadius,
    startAngle,
    endAngle,
    fill,
    payload,
    percent,
    value,
  } = props;
  const sin = Math.sin(-RADIAN * midAngle);
  const cos = Math.cos(-RADIAN * midAngle);
  const sx = cx + (outerRadius + 10) * cos;
  const sy = cy + (outerRadius + 10) * sin;
  const mx = cx + (outerRadius + 30) * cos;
  const my = cy + (outerRadius + 30) * sin;
  const ex = mx + (cos >= 0 ? 1 : -1) * 22;
  const ey = my;
  const textAnchor = cos >= 0 ? "start" : "end";

  return (
    <g>
      <text x={cx} y={cy} dy={8} textAnchor="middle" fill={fill}>
        {payload.name}
      </text>
      <Sector
        cx={cx}
        cy={cy}
        innerRadius={innerRadius}
        outerRadius={outerRadius}
        startAngle={startAngle}
        endAngle={endAngle}
        fill={fill}
      />
      <Sector
        cx={cx}
        cy={cy}
        startAngle={startAngle}
        endAngle={endAngle}
        innerRadius={outerRadius + 6}
        outerRadius={outerRadius + 10}
        fill={fill}
      />
      <path
        d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`}
        stroke={fill}
        fill="none"
      />
      <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none" />
      <text
        x={ex + (cos >= 0 ? 1 : -1) * 12}
        y={ey}
        textAnchor={textAnchor}
        fill="#333"
      >{`Post cmt ${value}`}</text>
      <text
        x={ex + (cos >= 0 ? 1 : -1) * 12}
        y={ey}
        dy={18}
        textAnchor={textAnchor}
        fill="#999"
      >
        {`(Rate ${(percent * 100).toFixed(2)}%)`}
      </text>
    </g>
  );
};

const ForumsStatisticsPieChart = (props) => {
  const { dataChart, updateData } = props;

  const [activeIndex, setActiveIndex] = useState(0);
  const onPieEnter = useCallback(
    (_, index) => {
      setActiveIndex(index);
    },
    [setActiveIndex],
  );

  return (
    <Card className="card-chart">
      <Card.Header>
        <Card.Title tag="h5">Forums Statistics</Card.Title>
        <p className="card-category">Number of post comment per forum group</p>
      </Card.Header>
      <Card.Body
        className="d-flex justify-content-center align-items-center"
        style={{ maxHeight: "300px", width: "100%", overflow: "hidden" }}
      >
        <PieChart width={500} height={500}>
          <Pie
            activeIndex={activeIndex}
            activeShape={renderActiveShape}
            data={dataChart}
            cx={240}
            cy={240}
            innerRadius={60}
            outerRadius={80}
            fill="#8884d8"
            dataKey="comments"
            onMouseEnter={onPieEnter}
          />
        </PieChart>
      </Card.Body>
      <Card.Footer>
        {/* <div className="legend">
          <i className="fa fa-circle text-primary" /> Opened{" "}
          <i className="fa fa-circle text-warning" /> Read{" "}
          <i className="fa fa-circle text-danger" /> Deleted{" "}
          <i className="fa fa-circle text-gray" /> Unopened
        </div> */}
        <hr />
        <button className="stats" onClick={updateData}>
          <i className="fa fa-history" />
          Updated
        </button>
      </Card.Footer>
    </Card>
  );
};

ForumsStatisticsPieChart.propTypes = {
  dataChart: PropTypes.array,
  updateData: PropTypes.func,
};

export default ForumsStatisticsPieChart;
