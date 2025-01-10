export function setBgColor(color) {
  switch (color) {
    case "primary":
      return "bg-primary";
    case "info":
      return "bg-info";
    case "warning":
      return "bg-warning";
    case "danger":
      return "bg-danger";
    default:
      return "bg-primary";
  }
}
