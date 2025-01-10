import axios from "../customize-axios";
import { pathParams } from "../../utils/Helper";

// with ignore admin
// use member page
export const getAllUserStats = async (pageData) => {
  try {
    let path = pathParams(pageData);
    let res = await axios.get(`user-stat/members?${path}`);
    return res;
  } catch (err) {
    console.log(err);
  }
};
