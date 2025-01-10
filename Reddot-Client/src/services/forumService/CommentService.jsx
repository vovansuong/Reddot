import axios from "axios";

const createComment = async (
  discussionId,
  comment,
  replyToId,
  accessToken,
  axiosJWT,
) => {
  const res = await axiosJWT.post(
    "/comments/add",
    {
      discussionId,
      comment,
      replyToId,
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

const updateComment = async (id, comment, accessToken, axiosJWT) => {
  const res = await axiosJWT.post(`/comments/update/${id}`, comment, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  });
  //
  return res;
};

const getAllComments = async () => {
  return await axios.get("/view/comments/all");
};

const deleteComment = async (id, comment, accessToken, axiosJWT) => {
  const res = await axiosJWT.delete(`/comments/delete/${id}/${comment}`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
  });
  return res;
};

export { getAllComments, createComment, updateComment, deleteComment };
