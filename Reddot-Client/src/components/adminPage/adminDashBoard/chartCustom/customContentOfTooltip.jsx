const getIntroOfForum = {
  "Forum A": "Forum A is about men's clothing",
  "Forum B": "Forum B is about women's dress",
  "Forum C": "Forum C is about women's bag",
  "Forum D": "Forum D is about household goods",
  "Forum E": "Forum E is about food",
  "Forum F": "Forum F is about baby food",
  "Forum G": "Forum G is about baby clothing",
};

import PropTypes from "prop-types";
const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    return (
      <div className="custom-tooltip">
        <p className="label">{`${label} : ${payload[0].value}`}</p>
        <p className="intro">{getIntroOfForum[label]}</p>
        <p className="desc">Anything you want can be displayed here.</p>
      </div>
    );
  }

  return null;
};

CustomTooltip.propTypes = {
  active: PropTypes.bool,
  payload: PropTypes.array,
  label: PropTypes.string,
};

export default CustomTooltip;
