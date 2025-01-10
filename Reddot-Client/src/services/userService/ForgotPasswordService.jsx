import axios from "../customize-axios";

export const forgotPasswordApi = async (email) => {
  return await axios.post("resetpassword/request", email);
};
