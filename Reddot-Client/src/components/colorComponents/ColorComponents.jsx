import { ChromePicker } from "react-color";
import PropTypes from "prop-types";
const ColorComponents = (props) => {
  const { handleChangeComplete, color } = props;

  return (
    <div className="form-group mb-3">
      <label className="form-label" htmlFor="title">
        Color
      </label>
      <ChromePicker color={color} onChangeComplete={handleChangeComplete} />
    </div>
  );
};

ColorComponents.propTypes = {
  color: PropTypes.string.isRequired,
  handleChangeComplete: PropTypes.func.isRequired,
};

export default ColorComponents;
