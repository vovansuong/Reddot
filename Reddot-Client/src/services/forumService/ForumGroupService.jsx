import axios from "../customize-axios";

const getAllForumGroup = async () => {
  return await axios.get("/view/forums/get-child-forums-and-forum-groups");
};

const addForumGroup = async (forumGroup, roleName, accessToken, axiosJWT) => {
  const res = await axiosJWT.post(
    "/admin/forum-groups",
    {
      forumGroup,
      roleName,
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

const updateForumGroup = async (
  id,
  forumGroup,
  roleName,
  accessToken,
  axiosJWT,
) => {
  const res = await axiosJWT.put(
    `/admin/forum-groups/update/${id}`,
    {
      forumGroup,
      roleName,
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

const deleteForumGroup = async (id, accessToken, axiosJWT) => {
  const res = await axiosJWT.delete(`/admin/forum-groups/delete/${id}`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  });
  //
  return res;
};

const voteSortByOrder = async (id, type, accessToken, axiosJWT) => {
  const res = await axiosJWT.post(
    `/admin/forum-groups/vote-sort-by-order-forum-group/${id}/${type}`,
    id,
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

export {
  getAllForumGroup,
  addForumGroup,
  updateForumGroup,
  deleteForumGroup,
  voteSortByOrder,
};
