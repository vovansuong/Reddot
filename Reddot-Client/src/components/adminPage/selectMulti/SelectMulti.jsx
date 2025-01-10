import Select from "react-select";
import PropTypes from "prop-types";

const SelectMulti = (props) => {
  const { tagOptions, selectedTags, handleTagChange } = props;

  return (
    <Select
      isMulti
      options={tagOptions}
      value={selectedTags}
      onChange={handleTagChange}
    />
  );
};

SelectMulti.propTypes = {
  tagOptions: PropTypes.array.isRequired,
  selectedTags: PropTypes.array.isRequired,
  handleTagChange: PropTypes.func.isRequired,
};

export default SelectMulti;
