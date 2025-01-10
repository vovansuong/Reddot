import { pathParams } from "../../utils/Helper";
//user profile page

//get list bookmark by username
export const getAllBookmarkByUsername = async (
  pageData,
  axiosJWT,
  accessToken,
) => {
  try {
    let path = pathParams(pageData);
    return await axiosJWT.get(`user-history/bookmarks?${path}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
  } catch (err) {
    console.log(`Error:`, JSON.stringify(err?.message));
  }
};

//get list comment by username
export const getAllCommentByUsername = async (
  pageData,
  axiosJWT,
  accessToken,
) => {
  try {
    let path = pathParams(pageData);
    return await axiosJWT.get(`user-history/comments?${path}`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
  } catch (err) {
    console.log(`Error:`, JSON.stringify(err));
    console.log(`Error:`, JSON.stringify(err?.message));
  }
};
