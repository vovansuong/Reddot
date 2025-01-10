import axios from "../customize-axios";
export const getForumStat = async () => {
  return await axios.get("/view/forum-stat");
};
