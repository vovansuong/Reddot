export function setColor(color) {
  switch (color) {
    case "primary":
      return "text-primary";
    case "info":
      return "text-info";
    case "warning":
      return "text-warning";
    case "danger":
      return "text-danger";
    default:
      return "text-primary";
  }
}
