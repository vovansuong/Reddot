export function addDefaultProps(defaultProps, props) {
  if (!defaultProps || !props) return props;

  let result = { ...props };

  Object.keys(defaultProps).forEach((key) => {
    if (result[key] === undefined) {
      result[key] = defaultProps[key];
    }
    if (
      Object.keys(defaultProps[key] || {}).length > 0 &&
      typeof defaultProps[key] === "object"
    ) {
      addDefaultProps(defaultProps[key], result);
    }
  });

  return result;
}
