//use in comment page

export const upVote = async (vote, accessToken, axiosJWT) => {
  try {
    const res = await axiosJWT.post("vote/vote-up", vote, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (error) {
    return error.response;
  }
};

export const downVote = async (vote, accessToken, axiosJWT) => {
  try {
    const res = await axiosJWT.post("vote/vote-down", vote, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return res;
  } catch (error) {
    return error.response;
  }
};
