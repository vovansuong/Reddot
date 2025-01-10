//use in comment page
export const registerBookmark = async (bookmarkData, accessToken, axiosJWT) => {
  return await axiosJWT.post(`bookmarks/register`, bookmarkData, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
};

/**
 * commentId
 * bookmarkBy: username
 * bookmarkStatus: true/false
 */
