import axios from "axios";

const searchForum = async (keyword) => {
  return await axios.get(`/view/forums/search/${keyword}`);
};

const searchDiscussion = async (keyword) => {
  return await axios.get(`/view/discussions/search/${keyword}`);
};

const searchComment = async (keyword) => {
  return await axios.get(`/view/comments/search/${keyword}`);
};

export { searchForum, searchDiscussion, searchComment };
