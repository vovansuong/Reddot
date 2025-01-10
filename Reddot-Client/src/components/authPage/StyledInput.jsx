import styled from "styled-components";

const StyledInput = styled.input((props) => ({
  color: props.valid ? "green" : "red",
  border: `1px solid ${props.valid ? "green" : "red"}!important`,
  maxWidth: "700px!important",
}));

export default StyledInput;
