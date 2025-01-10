import PropTypes from "prop-types";
import styled from "styled-components";
import { useEffect } from "react";

const StyledInput = styled.input((props) => ({
  color: props.valid ? "green" : "red",
  border: `1px solid ${props.valid ? "green" : "red"}!important`,
  maxWidth: "700px!important",
  padding: 1 + "rem" + 16 + "rem",
}));

import { errorPasswordItem } from "../../utils/validUtils";

const FormInput = ({
  id,
  type,
  value,
  valid,
  focus,
  setFocus,
  setValue,
  validate,
  placeholder,
  errorMsg,
  handleKeyDown,
}) => {
  const showErrMsg = () => {
    let errors = [];
    if (id !== "password") {
      errors.push(
        <li key={id} style={{ listStyleType: "none" }}>
          <i className="fa fa-info-circle" aria-hidden="true"></i> {errorMsg}
        </li>,
      );
    } else {
      const passwordErrors = errorPasswordItem(value);
      if (passwordErrors) {
        passwordErrors.forEach((error, index) => {
          errors.push(
            <li key={index + "_1"} style={{ listStyleType: "none" }}>
              <i className="fa fa-info-circle" aria-hidden="true"></i>{" "}
              <span>{error}</span>
            </li>,
          );
        });
      }
    }
    return errors;
  };

  useEffect(() => {
    validate(value);
    showErrMsg();
  }, [value, validate]);

  return (
    <>
      <StyledInput
        type={type}
        id={id}
        autoComplete="off"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder={placeholder}
        required
        aria-invalid={!valid}
        aria-describedby={`${id}-err`}
        onFocus={() => setFocus(true)}
        onBlur={() => setFocus(false)}
        valid={+(value.length === 0 || valid)}
        className="form-control"
      />
      <small
        id={`${id}-err`}
        className={
          (focus && value) || !valid ? "text-danger" : "invalid-feedback"
        }
        role="alert"
        hidden={valid || !focus}
      >
        <ul>{showErrMsg()}</ul>
      </small>
    </>
  );
};

FormInput.propTypes = {
  id: PropTypes.string.isRequired,
  type: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired,
  valid: PropTypes.bool.isRequired,
  focus: PropTypes.bool.isRequired,
  setFocus: PropTypes.func.isRequired,
  setValue: PropTypes.func.isRequired,
  validate: PropTypes.func.isRequired,
  placeholder: PropTypes.string.isRequired,
  errorMsg: PropTypes.string.isRequired,
  handleKeyDown: PropTypes.func,
};

export default FormInput;
