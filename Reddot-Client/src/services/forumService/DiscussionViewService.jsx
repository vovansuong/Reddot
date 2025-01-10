import { pathParams } from "../../utils/Helper";
import axios from "../customize-axios";

export const getFirstCommentByDiscussionId = async (discussionId) => {
  return await axios.get(`view/discussions/first-comment/${discussionId}`);
};

export const getAllCommentByDiscussionId = async (pageData) => {
  let path = pathParams(pageData);
  return await axios.get(`view/discussions/details?${path}`);
};

export const viewDiscussionsByTagId = async (tagId) => {
  return await axios.get(`view/discussions/tag/${tagId}`);
};

export const getLastCommentServiceResponseDiscussion = async (id) => {
  return await axios.get(
    `/view/discussions/getLastCommentServiceResponseDiscussion/${id}`,
  );
};
