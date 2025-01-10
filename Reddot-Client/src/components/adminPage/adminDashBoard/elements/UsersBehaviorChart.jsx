import { Card } from "react-bootstrap";
import PropTypes from "prop-types";

import {
  ComposedChart,
  Line,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";

// import CustomTooltip from '../chartCustom/customContentOfTooltip';

const UsersBehaviorChart = (props) => {
  const { dataChart } = props;

  return (
    <Card>
      <Card.Header>
        <Card.Title tag="h5">Users Behavior</Card.Title>
        <p className="card-category">Composed Chart</p>
      </Card.Header>
      <Card.Body
        className="d-flex justify-content-center align-items-center"
        style={{ maxHeight: "500px", width: "100%", overflow: "hidden" }}
      >
        <ComposedChart
          width={1000}
          height={400}
          data={dataChart}
          margin={{
            top: 5,
            right: 30,
            bottom: 20,
            left: 5,
          }}
        >
          <CartesianGrid stroke="#f5f5f5" strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="comments" barSize={19} fill="#413ea0" />
          <Line type="monotone" dataKey="users" stroke="#ff7300" />
        </ComposedChart>
      </Card.Body>
      <Card.Footer>
        <hr />
        <div className="stats">
          <i className="fa fa-history" /> Updated 3 minutes ago
        </div>
      </Card.Footer>
    </Card>
  );
};

UsersBehaviorChart.propTypes = {
  dataChart: PropTypes.array,
  updateData: PropTypes.func,
};

export default UsersBehaviorChart;
