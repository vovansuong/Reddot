import axios from "../customize-axios";

const createDiscussion = async (
  forumId,
  discussion,
  content,
  accessToken,
  axiosJWT,
) => {
  const res = await axiosJWT.post(
    "/discussions/add",
    {
      forumId,
      discussion,
      content,
    },
    {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    },
  );
  //
  return res;
};

const updateDiscussion = async (
  id,
  forumId,
  discussion,
  content,
  accessToken,
  axiosJWT,
) => {
  const res = await axiosJWT.put(
    `/discussions/update/${id}`,
    {
      forumId,
      discussion,
      content,
    },
    {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    },
  );
  //
  return res;
};

const getAllDiscussion = async () => {
  return await axios.get("/view/discussions/all");
};

const getPageDiscussion = async (
  page,
  size,
  orderBy,
  sort,
  search,
  forumId,
) => {
  try {
    const response = await axios.get("/view/discussions", {
      params: {
        page,
        size,
        orderBy,
        sort,
        search,
        forumId,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching discussions:", error);
    throw error; // Re-throw the error to handle it in the calling code if necessary
  }
};

const getDiscussionById = async (id) => {
  return await axios.get(`/view/discussions/byId/${id}`);
};

const updateDetailsDiscussion = async (
  id,
  discussion,
  accessToken,
  axiosJWT,
) => {
  const res = await axiosJWT.put(
    `/discussions/updateDetails/${id}`,
    discussion,
    {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    },
  );
  //
  return res;
};

const updateViews = async (id, accessToken, axiosJWT) => {
  const res = await axiosJWT.put(
    `/discussions/updateViews/${id}`,
    {},
    {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    },
  );
  //
  return res;
};

const setDataListTags = async (
  discussionId,
  selectedTags,
  accessToken,
  axiosJWT,
) => {
  const tagIds = selectedTags.map((tag) => tag.value);
  const res = await axiosJWT.put(`/discussions/${discussionId}/tags`, tagIds, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  });
  //
  return res;
};

const deleteDiscussion = async (id, accessToken, axiosJWT) => {
  const res = await axiosJWT.delete(`/discussions/delete/${id}`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  });
  //
  return res;
};

const checkRoleDiscussion = async (id, accessToken, axiosJWT) => {
  const res = await axiosJWT.get(`/discussions/checkRole/${id}`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  });
  //
  return res;
};

export {
  createDiscussion,
  getAllDiscussion,
  updateDiscussion,
  getDiscussionById,
  getPageDiscussion,
  updateDetailsDiscussion,
  updateViews,
  setDataListTags,
  deleteDiscussion,
  checkRoleDiscussion,
};
